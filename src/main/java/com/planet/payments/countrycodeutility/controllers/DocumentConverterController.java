package com.planet.payments.countrycodeutility.controllers;

import com.planet.payments.countrycodeutility.service.DocumentConverter;
import com.planet.payments.countrycodeutility.utility.Constants;
import com.planet.payments.countrycodeutility.utility.DocumentValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/country-code-utility")
public class DocumentConverterController {

    private static final Logger logger = LoggerFactory.getLogger(DocumentConverterController.class);

    @Autowired
    private DocumentConverter documentConverter;

    /**
     * This API will be used to read the CSV file and persist the record in DB
     * @param csvFile
     * @return Success/Fail Message
     */
    @PostMapping(value = "/upload/csv/{csvFile}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<com.planet.payments.countrycodeutility.entity.ResponseEntity> uploadCSVFile(@RequestParam("csvFile") MultipartFile csvFile) {
        String message = "";
        logger.info("CSV API execution Started...");
        if (DocumentValidator.isValidFormat(csvFile)) {
            try {
                documentConverter.saveAllOrdersFromCsv(csvFile);
                message = Constants.UPLOAD_SUCCESS + " : " + csvFile.getOriginalFilename();
                logger.info("CSV API execution Ended...");
                return ResponseEntity.status(HttpStatus.OK).body(new com.planet.payments.countrycodeutility.entity.ResponseEntity(message));
            } catch (Exception e) {
                message = Constants.UPLOAD_FAILED + " : " + csvFile.getOriginalFilename() + " "+ e.getMessage();
                logger.error("CSV API execution Ended... "+ message);
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new com.planet.payments.countrycodeutility.entity.ResponseEntity(message));
            }
        }
        logger.error("CSV API execution Ended... "+ Constants.UPLOAD_FILE);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new com.planet.payments.countrycodeutility.entity.ResponseEntity(Constants.UPLOAD_FILE));
    }

    /**
     * This API is used to convert xlsx to csv document
     * @param multipartFile input xlsx file
     * @return Success/Fail message
     */
    @PostMapping(value = "/upload/xlsx/{xlsxFile}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<com.planet.payments.countrycodeutility.entity.ResponseEntity> upload(@RequestParam("xlsxFile")MultipartFile multipartFile,
                         HttpServletResponse response) {
        boolean status;
        logger.info("XLSX API execution Started...");
        if (DocumentValidator.validateUploadedFile(multipartFile)) {
            status = documentConverter.convertXLSX_to_CSV(multipartFile);
            if (status == true) {
                return ResponseEntity.status(HttpStatus.OK).body(new com.planet.payments.countrycodeutility.entity.ResponseEntity("File Processed Successfully"));
            } else {
                logger.info("XLSX API execution Ended... "+ "Failed to convert Xlsx to Csv");
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new com.planet.payments.countrycodeutility.entity.ResponseEntity("Failed to convert Xlsx to Csv"));
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new com.planet.payments.countrycodeutility.entity.ResponseEntity(Constants.UPLOAD_FILE));
    }
}
