package com.jsondriventemplate.logic;

import com.jayway.jsonpath.JsonPath;
import com.jsondriventemplate.AppInject;
import com.jsondriventemplate.constant.AppConst;
import com.jsondriventemplate.constant.AuthConst;
import com.jsondriventemplate.exception.ValidationException;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public final class JDTScript {

    private static final String MASK_PASSWORD = "passwordCannotBeVisible";

    public void process(@NotNull Map<String, Object> requestDTO) throws Exception {
        processAndReturn(requestDTO);
    }

    public <T> T processAndReturn(@NotNull Map<String, Object> requestDTO) throws Exception {
        if (CollectionUtils.isEmpty(requestDTO)) {
            throw new ValidationException("request is not valid");
        }
        String type = (String) requestDTO.get(AppConst.TYPE);
        if (StringUtils.isBlank(type)) {
            throw new ValidationException("type is required");
        }
        boolean isScriptProcessAble = AppConst.JDT_SCRIPT.contains(type);
        if (!isScriptProcessAble) {
            throw new Exception("provided script is not available within this system");
        }
        AppInject.templateService.getURIID((String) requestDTO.get(AppConst.URI));
        return (T) scriptProcess(requestDTO);
    }

    private Object scriptProcess(Map<String, Object> requestDTO) throws Exception {
        String uri = (String) requestDTO.get(AppConst.URI);
        if (StringUtils.isBlank(uri)) {
            throw new ValidationException("uri is required");
        }
        String type = (String) requestDTO.get(AppConst.TYPE);
        String id = (String) requestDTO.get(AppConst.ID);
        removeBeforeOperation(requestDTO);
        switch (type) {
            case AppConst.DELETE:
                AppInject.mongoClientProvider.delete(id, uri);
                break;
            case AppConst.SEARCH:
                validation(uri, type, requestDTO);
                return AppInject.mongoClientProvider.search(requestDTO, uri);
            case AppConst.RETRIEVE:
                return AppInject.mongoClientProvider.findAll(uri);
            case AppConst.RETRIEVE_BY_ID:
                Map record = AppInject.mongoClientProvider.findById(id, uri);
                if (record.containsKey(AuthConst.PASSWORD)) {
                    record.put(AuthConst.PASSWORD, MASK_PASSWORD);
                }
                return record;
            case AppConst.CREATE:
                validation(uri, type, requestDTO);
                checkUserNamePassword(uri, type, requestDTO);
                return AppInject.mongoClientProvider.save(requestDTO, uri);
            case AppConst.UPDATE:
                if (StringUtils.isBlank(id)) {
                    throw new ValidationException("id is required");
                }
                validation(uri, type, requestDTO);
                checkUserNamePassword(uri, type, requestDTO);
                AppInject.mongoClientProvider.save(requestDTO, uri);
                break;
        }
        return null;
    }

    private void removeBeforeOperation(Map<String, Object> requestDTO) {
        requestDTO.remove(AppConst.TYPE);
        requestDTO.remove(AppConst.URI);
    }

    private void checkUserNamePassword(String uri, String type, Map<String, Object> dataMap) throws ValidationException {

        if (type.equals(AppConst.CREATE)) {
            if (dataMap.containsKey(AuthConst.USERNAME)) {

                Map user = AppInject.mongoClientProvider.findByAtt(AuthConst.USERNAME, (String) dataMap.get(AuthConst.USERNAME), uri);
                if (user != null && !user.isEmpty()) {
                    throw new ValidationException("username already exits");
                }
            }
            if (dataMap.containsKey(AuthConst.PASSWORD)) {
                String encodedPassword = AppInject.passwordEncoder.encode((String) dataMap.get(AuthConst.PASSWORD));
                dataMap.put(AuthConst.PASSWORD, encodedPassword);
            }
        }
        if (type.equals(AppConst.UPDATE)) {
            Map user = null;
            if (dataMap.containsKey(AuthConst.USERNAME)) {
                user = AppInject.mongoClientProvider.findByAtt(AuthConst.USERNAME, (String) dataMap.get(AuthConst.USERNAME), uri);
                if (user != null && !user.isEmpty()) {
                    String _id = (String) user.get(AppConst.ID);
                    if (!dataMap.containsKey(AppConst.ID) || !_id.equals(dataMap.get(AppConst.ID))) {
                        throw new ValidationException("username already exits");
                    }
                }
            }
            if (dataMap.containsKey(AuthConst.PASSWORD)) {
                if (user != null && !user.isEmpty()) {
                    String dbpassword = (String) user.get(AuthConst.PASSWORD);
                    if (!dbpassword.equals(dataMap.get(AuthConst.PASSWORD)) && !StringUtils.equalsAnyIgnoreCase((String) dataMap.get(AuthConst.PASSWORD), MASK_PASSWORD)) {
                        String encodedPassword = AppInject.passwordEncoder.encode((String) dataMap.get(AuthConst.PASSWORD));
                        dataMap.put(AuthConst.PASSWORD, encodedPassword);
                    } else {
                        dataMap.put(AuthConst.PASSWORD, dbpassword);
                    }
                }
            }
            if (user.containsKey(AuthConst.ROLE)) {
                dataMap.put(AuthConst.ROLE, user.get(AuthConst.ROLE));
            }
        }


    }

    private void validation(String uri, String type, Map<String, Object> dataMap) throws Exception {
        String jsonData = AppInject.templateService.getJSONOnlyFromURI(uri);
        LinkedHashMap jsonSchema = JsonPath.parse(jsonData).read("$['definitions']['" + type + "']['definitions']['fields']");

        JSONObject schema = new JSONObject(jsonSchema);
        StringBuilder error = new StringBuilder();
        for (String key : schema.keySet()) {
            JSONObject value = (JSONObject) schema.get(key);
            if (value.has("required")) {
                Boolean required = value.getBoolean("required");
                if ((required && StringUtils.isBlank((CharSequence) dataMap.get(key)))) {
                    error.append(key).append(" is a required field.");
                }
            }
            if (value.has("validation_regx") && StringUtils.isNotBlank(value.getString("validation_regx"))) {
                String validation_regx = value.getString("validation_regx");
                String actualData = (String) dataMap.get(key);
                if (StringUtils.isNotBlank(actualData)) {
                    boolean matches = actualData.matches(validation_regx);
                    if (!matches) {
                        error.append(key).append(" does not contain valid data.");
                    }
                }
            }
        }
        if (StringUtils.isNotBlank(error.toString())) {
            throw new ValidationException(error.toString());
        }
    }
}
