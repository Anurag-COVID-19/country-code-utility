package com.planet.payments.countrycodeutility.service_impl;

import com.planet.payments.countrycodeutility.service.DocumentConverter;
import com.planet.payments.countrycodeutility.utility.FileValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DocumentConverterService implements DocumentConverter {

    private static final Logger logger = LoggerFactory.getLogger(DocumentConverterService.class);

    @Override
    public boolean convertXLSX_to_CSV(MultipartFile file) {
        try {
            FileValidator.convertXlsxToCsv(file);
        } catch (Exception e) {
            logger.error("Error occurred while processing the document...."+ e.getMessage());
            return false;
        }
        return true;
    }
}
