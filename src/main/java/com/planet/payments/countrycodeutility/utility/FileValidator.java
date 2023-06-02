package com.planet.payments.countrycodeutility.utility;

import com.github.pjfanning.xlsx.StreamingReader;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Iterator;

public class FileValidator {

    public static boolean validateUploadedFile(MultipartFile inputMultipartFile) {
        if (inputMultipartFile.isEmpty() || inputMultipartFile.getSize() == 0) {
            return false;
        }
        if (inputMultipartFile.getContentType().toLowerCase().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") || inputMultipartFile.getContentType().toLowerCase().equals("application/octet-stream") || inputMultipartFile.getContentType().toLowerCase().equals("application/vnd.ms-excel")){
            return true;
        }
        return false;
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
}
