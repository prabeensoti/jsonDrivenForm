package com.jsondriventemplate.app;

import com.jsondriventemplate.AppInject;
import com.jsondriventemplate.constant.AppConst;
import com.jsondriventemplate.exception.ValidationException;
import com.jsondriventemplate.logic.JDTScript;
import com.jsondriventemplate.logic.JsonTemplateService;
import com.jsondriventemplate.repo.MongoClientProvider;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
public abstract class AbstractJDTScriptTest {

    private static boolean mockEnable;

    public AbstractJDTScriptTest(boolean mockEnableBool) {
        mockEnable=mockEnableBool;
    }

    @BeforeClass
    public static void setUp() throws Exception {
        if(!mockEnable){
            return;
        }
        AppInject.templateService=mock(JsonTemplateService.class);
        AppInject.mongoClientProvider=mock(MongoClientProvider.class);
        AppInject.mongoClientProvider=mock(MongoClientProvider.class);
        AppInject.passwordEncoder=mock(PasswordEncoder.class);
        AppInject.jdtScript=new JDTScript();
        when(AppInject.mongoClientProvider.search(any(), any())).thenReturn(Collections.EMPTY_LIST);
        when(AppInject.templateService.getJSONOnlyFromURI("employee")).thenReturn(TestData.readEmployeeJSON());
        when(AppInject.passwordEncoder.encode(any())).thenReturn("encodedPassword");
    }

    @Test(expected = Exception.class)
    public void with_invalid_type_request() throws Exception {
        setUp();
        Map<String,Object> requestDTO=new HashMap<>();
        requestDTO.put("type","abcse");
        requestDTO.put("uri", "employee");
        requestDTO.put(AppConst.ID, "");
        AppInject.jdtScript.processAndReturn(requestDTO);
    }

    @Test
    public void with_valid_request() throws Exception {
        setUp();
        Map<String,Object> requestDTO=new HashMap<>();
        requestDTO.put("type","search");
        requestDTO.put("uri", "employee");
        requestDTO.put(AppConst.ID, "");
        AppInject.jdtScript.processAndReturn(requestDTO);
    }

    @Test(expected = ValidationException.class)
    public void with_empty_type() throws Exception {
        setUp();
        Map<String,Object> requestDTO=new HashMap<>();
        requestDTO.put("type","");
        requestDTO.put("uri", "employee");
        requestDTO.put(AppConst.ID, "");
        AppInject.jdtScript.processAndReturn(requestDTO);
    }

    @Test(expected = ValidationException.class)
    public void with_empty_uri() throws Exception {
        setUp();
        Map<String,Object> requestDTO=new HashMap<>();
        requestDTO.put("type","search");
        requestDTO.put("uri", "");
        requestDTO.put(AppConst.ID, "");
        AppInject.jdtScript.processAndReturn(requestDTO);
    }

    @Test(expected = ValidationException.class)
    public void with_null() throws Exception {
        setUp();
        AppInject.jdtScript.processAndReturn(null);
    }


    @Test(expected = ValidationException.class)
    public void with_create_invalid_data() throws Exception {
        setUp();
        Map<String,Object> requestDTO=new HashMap<>();
        requestDTO.put("type","create");
        requestDTO.put("uri", "employee");
        requestDTO.put(AppConst.ID, "");
        AppInject.jdtScript.processAndReturn(requestDTO);
    }

    @Test(expected = ValidationException.class)
    public void with_update_empty_id() throws Exception {
        setUp();
        Map<String,Object> requestDTO=new HashMap<>();
        requestDTO.put("type","update");
        requestDTO.put("uri", "employee");
        requestDTO.put(AppConst.ID, "");
        AppInject.jdtScript.processAndReturn(requestDTO);
    }

    @Test(expected = ValidationException.class)
    public void with_update_random_id() throws Exception {
        setUp();
        Map<String,Object> requestDTO=new HashMap<>();
        requestDTO.put("type","update");
        requestDTO.put("uri", "employee");
        requestDTO.put(AppConst.ID, "asdfasfdasf123123");
        AppInject.jdtScript.processAndReturn(requestDTO);
    }

    //-------------- Complete cycle ------------------------------------
    public static String recordId;
    @Test
    public void with_create_valid_data() throws Exception {
        setUp();
        recordId = AppInject.jdtScript.processAndReturn(TestData.employeeCreateData());
    }

    @Test
    public void with_get_create_data() throws Exception {
        setUp();
        Map<String, Object> getRecord = getRecord();

        Map<String, Object> savedRecord = TestData.employeeCreateData();
        getRecord.equals(savedRecord);
    }

    @Test
    public void with_modified_valid_data() throws Exception {
        setUp();
        AppInject.jdtScript.processAndReturn(TestData.employeeUpdateData(recordId));
        verifyUpdateRecord();
    }

    private void verifyUpdateRecord() throws Exception {
        Map<String, Object> getRecord = getRecord();

        Map<String, Object> updatedRecord = TestData.employeeUpdateData(recordId);
        getRecord.equals(updatedRecord);
    }

    private Map<String, Object> getRecord() throws Exception {
        Map<String, Object> recordMap= AppInject.jdtScript.processAndReturn(TestData.employeeGetData(recordId));
        if(MapUtils.isEmpty(recordMap)){
            throw new Exception("no record found");
        }
        return recordMap;
    }

    @Test
    public void with_remove() throws Exception {
        setUp();
        AppInject.jdtScript.processAndReturn(TestData.employeeRemoveData(recordId));
    }

    @Test(expected = Exception.class)
    public void with_remove_verify() throws Exception {
        getRecord();
    }
}
