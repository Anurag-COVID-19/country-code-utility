package com.planet.payments.countrycodeutility.utility;

import jakarta.annotation.PostConstruct;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
public class CountryCodeMapper {

    public static final Map<Integer, String> countryCodeNameMapper = new HashMap<>();

    @PostConstruct
    public static void initializeCountryJson() {
        String jsonString = jsonAsString();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("countries");
            for (int i = 0 ; i < jsonArray.length(); i++) {
                JSONObject keyValue = jsonArray.getJSONObject(i);
                int key = keyValue.getInt("code");
                String value = keyValue.getString("country");
                CountryCodeMapper.initialize(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String jsonAsString() {
        ClassPathResource classPathResource = new ClassPathResource("country_code.json");
        try (Reader reader = new InputStreamReader(classPathResource.getInputStream(), StandardCharsets.UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    public static void initialize(Integer key, String value) {
        if (!countryCodeNameMapper.containsKey(key)) {
            countryCodeNameMapper.put(key, value);
        }
    }

}
