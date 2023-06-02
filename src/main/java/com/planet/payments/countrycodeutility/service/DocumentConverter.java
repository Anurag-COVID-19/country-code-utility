package com.planet.payments.countrycodeutility.service;

import org.springframework.web.multipart.MultipartFile;

public interface DocumentConverter {

    public boolean convertXLSX_to_CSV(MultipartFile file);
}
