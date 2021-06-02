package com.jsondriventemplate.logic;

import com.jsondriventemplate.AppInject;
import com.jsondriventemplate.constant.AppConst;
import com.jsondriventemplate.constant.AuthConst;
import com.jsondriventemplate.constant.JSONTemplateConst;
import com.jsondriventemplate.repo.DBConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
public final class DefaultLoader {

    @Value("${default.username}")
    private String userName;
    @Value("${default.password}")
    private String password;
    @Value("${default.firstname}")
    private String firstName;
    @Value("${database.createupdate}")
    private String createUpdate;

    public void defaultJSONLoader() throws IOException {
        loadDefaultJSON(JSONTemplateConst.LOGIN, JSONTemplateConst.LOGIN_NAME, JSONTemplateConst.LOGIN_JSON);
        loadDefaultJSON(JSONTemplateConst.DASHBOARD, JSONTemplateConst.DASHBOARD_NAME, JSONTemplateConst.DASHBOARD_JSON);
        loadDefaultJSON(JSONTemplateConst.EMPLOYEEE, JSONTemplateConst.EMPLOYEEE_NAME, JSONTemplateConst.EMPLOYEEE_JSON);
        loadDefaultJSON(JSONTemplateConst.EMPLOYEE_DASHBOARD, JSONTemplateConst.EMPLOYEE_DASHBOARD_NAME, JSONTemplateConst.EMPLOYEE_DASHBOARD_JSON);
    }

    public void createUpdateDefaultJSON() throws IllegalAccessException {
        //create necessary collection if not exist
        if (createUpdate.equalsIgnoreCase(AppConst.CREATE)) {
            Field[] fields = DBConstant.class.getDeclaredFields();
            for (Field field : fields) {
                String value = (String) field.get(new DBConstant());
                AppInject.mongoClientProvider.dropCollection(value);
            }
        }
        AppInject.mongoClientProvider.collection(DBConstant.TEMPLATE_INFORMATION);
        AppInject.mongoClientProvider.collection(DBConstant.JSON_TEMPLATE_DEFINITION);
    }

    public void loadDefaultUser() {
        Map login = AppInject.mongoClientProvider.findByAtt(AuthConst.USERNAME, userName, DBConstant.EMPLOYEE);
        if (login == null || StringUtils.isBlank((CharSequence) login.get(AppConst.ID))) {
            Map<String, Object> user = new HashMap<>();
            user.put(AuthConst.USERNAME, userName);
            user.put(AuthConst.PASSWORD, AppInject.passwordEncoder.encode(password));
            user.put(AuthConst.FIRST_NAME, firstName);
            user.put(AuthConst.ROLE, AuthConst.SUPER_ADMIN_ROLE);
            AppInject.mongoClientProvider.save(user, DBConstant.EMPLOYEE);
        }
    }

    private void loadDefaultJSON(String url, String name, String fileName) throws IOException {
        {
            Map login = AppInject.mongoClientProvider.findByURL(url, DBConstant.TEMPLATE_INFORMATION);
            if (login == null || StringUtils.isBlank((CharSequence) login.get(AppConst.URL))) {
                Map<String, Object> loginTemplateInformation = new HashMap<>();
                loginTemplateInformation.put(AppConst.URL, url);
                loginTemplateInformation.put(JSONTemplateConst.NAME, name);
                AppInject.mongoClientProvider.save(loginTemplateInformation, DBConstant.TEMPLATE_INFORMATION);
            }
        }
        {
            Map login = AppInject.mongoClientProvider.findByURL(url, DBConstant.TEMPLATE_INFORMATION);
            Map templateDef = AppInject.mongoClientProvider.findById((String) login.get(AppConst.ID), DBConstant.JSON_TEMPLATE_DEFINITION);
            if (templateDef == null || StringUtils.isBlank((CharSequence) templateDef.get(AppConst.JSON))) {
                Map<String, Object> loginTemplateInformation = new HashMap<>();
                loginTemplateInformation.put(AppConst.ID, login.get(AppConst.ID));
                Resource resource = new ClassPathResource("classpath:" + JSONTemplateConst.JSON_SCHEMA_ATTR + fileName);
                InputStream inputStream = resource.getInputStream();
                byte[] bdata = FileCopyUtils.copyToByteArray(inputStream);
                loginTemplateInformation.put(AppConst.JSON, new String(bdata, StandardCharsets.UTF_8));
                AppInject.mongoClientProvider.save(loginTemplateInformation, DBConstant.JSON_TEMPLATE_DEFINITION);
            }
        }
    }

}
