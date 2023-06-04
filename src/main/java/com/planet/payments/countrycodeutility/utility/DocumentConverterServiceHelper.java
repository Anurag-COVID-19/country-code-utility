package com.planet.payments.countrycodeutility.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pjfanning.xlsx.StreamingReader;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.planet.payments.countrycodeutility.entity.Order;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DocumentConverterServiceHelper {

    public static List<Order> csvToOrders(InputStream inputStream) {
        try (BufferedReader inputFileReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
             CSVParser csvParser = new CSVParser(inputFileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withAllowMissingColumnNames(true).withTrim());) {
            List<Order> orders = new ArrayList<>();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            List<String> headers = csvParser.getHeaderNames();
            for (CSVRecord csvRecord : csvRecords) {
                Order order = new Order();
                if (csvRecord.isMapped("id") || csvRecord.isMapped("email") || csvRecord.isMapped("phone_number") || csvRecord.isMapped("parcel_weight")) {
                    order.setId(csvRecord.get("id"));
                    order.setEmail(csvRecord.get("email"));
                    order.setPhoneNumber(csvRecord.get("phone_number"));
                    order.setCountry(DocumentConverterServiceHelper.getCountryName(order.getPhoneNumber()));
                    order.setParcelWeight(csvRecord.get("parcel_weight"));
                }
                orders.add(order);
            }
            return orders;
        } catch (IOException exception) {
            throw new RuntimeException("fail to parse the csv file :" + exception.getMessage());
        }
    }

    public static boolean convertXlsxToCsv(MultipartFile file) throws IOException {
        File outputFile = new File(file.getName().toLowerCase());

        IOUtils.setByteArrayMaxOverride(175040776);
        InputStream inputStream = file.getInputStream();
        try (Workbook workbook = StreamingReader.builder().rowCacheSize(100).bufferSize(4096).open(inputStream)){
            int sheets = workbook.getNumberOfSheets();
            Cell cells;
            Row rows;
            StringBuffer data;
            for (int i = 0; i < sheets; i++ ){
                data = new StringBuffer();
                Iterator<Row> rowIterator = workbook.getSheetAt(i).iterator();
                while (rowIterator.hasNext()) {
                    rows = rowIterator.next();
                    for (int j = 0; j < rows.getLastCellNum(); j++) {
                        cells = rows.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        switch (cells.getCellType()){
                            case FORMULA:
                                switch (cells.getCachedFormulaResultType()) {
                                    case NUMERIC:
                                        data.append(getFormattedNumericValue(cells) + ",");
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            case BOOLEAN:
                                data.append(cells.getBooleanCellValue()+ ",");
                                break;
                            case NUMERIC:
                                data.append(getFormattedNumericValue(cells)+ ",");
                                break;
                            case STRING:
                                data.append(cells.getStringCellValue()+ ",");
                                break;
                            case BLANK:
                                data.append(" "+ ",");
                                break;
                            default:
                                data.append(cells+ ",");
                        }
                    }
                    data.append("\n");
                }
                FileOutputStream outputStream = new FileOutputStream(outputFile.getName()+".csv");
                outputStream.write(data.toString().getBytes());
                outputStream.close();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static String getFormattedNumericValue(Cell cells) {
        String values;
        if (DateUtil.isCellDateFormatted(cells)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            values = simpleDateFormat.format(cells.getDateCellValue());
        } else if (cells.getCellStyle().getDataFormatString().contains("%")) {
            Double value = cells.getNumericCellValue()*100;
            values = value.toString()+ "%";
        } else {
            values = "" + cells.getNumericCellValue();
        }
        return values;
    }

    private static String getCountryName(String phoneNumber) throws JsonProcessingException {
        String plus = "+";
        phoneNumber.replaceAll("\\s", "");
        if (!phoneNumber.startsWith(plus)) {
            phoneNumber = plus+phoneNumber;
        }
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber number = phoneNumberUtil.parse(phoneNumber, "");
            return CountryCodeMapper.countryCodeNameMapper.containsKey(number.getCountryCode()) ? CountryCodeMapper.countryCodeNameMapper.get(number.getCountryCode()) : String.valueOf(number.getCountryCode());
        } catch (NumberParseException e) {
            throw new RuntimeException("Unable to parse given number " + e.getMessage());
        }
    }
}
