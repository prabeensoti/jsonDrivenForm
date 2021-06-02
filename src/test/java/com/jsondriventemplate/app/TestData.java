package com.jsondriventemplate.app;

import com.jsondriventemplate.constant.JSONTemplateConst;
import org.apache.commons.io.IOUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class TestData {

    public static String readEmployeeJSON() throws IOException {
        File file = ResourceUtils.getFile("classpath:" + JSONTemplateConst.JSON_SCHEMA_ATTR + "employee.json");
        FileInputStream fisTargetFile = new FileInputStream(file);
        return IOUtils.toString(fisTargetFile, StandardCharsets.UTF_8);
    }

    public static Map<String,Object> employeeCreateData(){
        Map<String,Object> requestDTO=new HashMap<>();
        requestDTO.put("type","create");
        requestDTO.put("uri", "employee");
        requestDTO.put("firstname", "Stephanie");
        requestDTO.put("lastname", "Prescott");
        requestDTO.put("address", "56 McDowall Street");
        requestDTO.put("gender", "female");
        requestDTO.put("phone", "(07) 5348 8664");
        requestDTO.put("designation", "SUT S");
        requestDTO.put("username", "tierra_purdy");
        requestDTO.put("password", "opa7lae6I");
        requestDTO.put("salary", "300000");
        requestDTO.put("age", "25");
        return requestDTO;
    }

    public static Map<String,Object> employeeUpdateData(String id){
        Map<String,Object> requestDTO=employeeCreateData();
        requestDTO.put("firstname","Stephani");
        requestDTO.put("type","update");
        requestDTO.put("_id",id);
        return requestDTO;
    }

    public static Map<String,Object> employeeGetData(String id){
        Map<String,Object> requestDTO=new HashMap<>();
        requestDTO.put("type","retrieveById");
        requestDTO.put("uri", "employee");
        requestDTO.put("_id",id);
        return requestDTO;
    }

    public static Map<String,Object> employeeRemoveData(String id){
        Map<String,Object> requestDTO=new HashMap<>();
        requestDTO.put("type","delete");
        requestDTO.put("uri", "employee");
        requestDTO.put("_id",id);
        return requestDTO;
    }

}
