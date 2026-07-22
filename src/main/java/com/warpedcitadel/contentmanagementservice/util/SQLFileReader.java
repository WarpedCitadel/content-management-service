package com.warpedcitadel.contentmanagementservice.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class SQLFileReader {

    private static final Logger log = LoggerFactory.getLogger(SQLFileReader.class);

    public String loadSQL(String filename) {
        try {
            ClassPathResource resource = new ClassPathResource("sql" + filename);
            InputStreamReader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
            return FileCopyUtils.copyToString(reader);
        } catch (IOException exception) {
            log.error("Failed to read SQL file Reason: ({})", exception.toString());
            throw new RuntimeException("Failed to reach database");
        }
    }
}