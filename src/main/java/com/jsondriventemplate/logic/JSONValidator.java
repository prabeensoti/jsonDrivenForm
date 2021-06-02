package com.jsondriventemplate.logic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsondriventemplate.exception.JSONValidationException;
import org.springframework.stereotype.Component;

@Component
public final class JSONValidator {

    public String validJSONFormat(String json) throws JSONValidationException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(json);
            return jsonNode.toString();
        } catch (Exception x) {
            x.printStackTrace();
            throw new JSONValidationException("not a valid json data");
        }

    }

}
