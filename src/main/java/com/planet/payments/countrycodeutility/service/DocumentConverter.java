package com.planet.payments.countrycodeutility.service;

import org.springframework.web.multipart.MultipartFile;

public interface DocumentConverter {

    /**
     * Convert XLSX to CSV
     * @param file multipart file
     * @return status conversion success/fail
     */
    public boolean convertXLSX_to_CSV(MultipartFile file);

    /**
     * Persist csv data into DB
     * @param file multipart file
     */
    public void saveAllOrdersFromCsv(MultipartFile file);
}
