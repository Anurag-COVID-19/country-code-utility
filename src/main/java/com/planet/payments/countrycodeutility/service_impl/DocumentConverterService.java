package com.planet.payments.countrycodeutility.service_impl;

import com.planet.payments.countrycodeutility.entity.Order;
import com.planet.payments.countrycodeutility.repository.OrdersRepository;
import com.planet.payments.countrycodeutility.service.DocumentConverter;
import com.planet.payments.countrycodeutility.utility.DocumentConverterServiceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class DocumentConverterService implements DocumentConverter {

    private static final Logger logger = LoggerFactory.getLogger(DocumentConverterService.class);

    @Autowired
    private OrdersRepository ordersRepository;

    /**
     * Convert XLSX to CSV
     * @param file multipart file
     * @return status conversion success/fail
     */
    @Override
    public boolean convertXLSX_to_CSV(MultipartFile file) {
        try {
            DocumentConverterServiceHelper.convertXlsxToCsv(file);
        } catch (Exception e) {
            logger.error("Error occurred while processing the document...."+ e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Persist csv data into DB
     * @param file multipart file
     */
    @Override
    public void saveAllOrdersFromCsv(MultipartFile file) {
        try {
            List<Order> orders = DocumentConverterServiceHelper.csvToOrders(file.getInputStream());
            ordersRepository.saveAll(orders);
        }catch (IOException exception) {
            throw new RuntimeException("failed to store csv data ..."+ exception.getMessage());
        }
    }
}
