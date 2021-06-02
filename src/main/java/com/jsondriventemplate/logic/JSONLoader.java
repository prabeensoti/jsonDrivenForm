package com.jsondriventemplate.logic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public final class JSONLoader {


    public static String laodJSONDefinition(File jsonFile) throws IOException {
        return FileUtils.readFileToString(jsonFile, StandardCharsets.UTF_8);
    }

    protected static Map<String, Object> mapper(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {
        });
    }


}

