package com.planet.payments.countrycodeutility.controllers;

import com.planet.payments.countrycodeutility.service.DocumentConverter;
import com.planet.payments.countrycodeutility.utility.FileValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class DocumentConverterController {

    @Autowired
    private DocumentConverter documentConverter;

    @PostMapping(value = "/upload/{file}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String upload(@RequestParam("file")MultipartFile multipartFile,
                         HttpServletRequest request,
                         HttpServletResponse response) {
        boolean status;
        if (FileValidator.validateUploadedFile(multipartFile)) {
            status = documentConverter.convertXLSX_to_CSV(multipartFile);
            if (status == true) {
                return "File Processed Successfully";
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return "Failed to convert Xlsx to Csv";
            }
        } else {
            return String.valueOf(Boolean.FALSE);
        }
    }
}
