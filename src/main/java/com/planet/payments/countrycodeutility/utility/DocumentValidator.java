package com.planet.payments.countrycodeutility.utility;

import org.springframework.web.multipart.MultipartFile;

public class DocumentValidator {

    public static boolean isValidFormat(MultipartFile file ){
        return  (!Constants.TYPE.contains(file.getContentType())) ? false : true;
    }


    public static boolean validateUploadedFile(MultipartFile inputMultipartFile) {
        if (inputMultipartFile.isEmpty() || inputMultipartFile.getSize() == 0) {
            return false;
        }
        if (inputMultipartFile.getContentType().toLowerCase().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") || inputMultipartFile.getContentType().toLowerCase().equals("application/octet-stream") || inputMultipartFile.getContentType().toLowerCase().equals("application/vnd.ms-excel")){
            return true;
        }
        return false;
    }
}
