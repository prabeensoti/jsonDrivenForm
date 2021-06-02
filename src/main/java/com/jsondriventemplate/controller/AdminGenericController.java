package com.jsondriventemplate.controller;

import com.jsondriventemplate.AppInject;
import com.jsondriventemplate.constant.AppConst;
import com.jsondriventemplate.constant.Endpoints;
import com.jsondriventemplate.constant.JSONTemplateConst;
import com.jsondriventemplate.constant.ViewResolver;
import com.jsondriventemplate.exception.ValidationException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = Endpoints.ADMIN)
public class AdminGenericController {


    @GetMapping(value = Endpoints.URI)
    public String globalPage(Model model, @PathVariable String uri, @RequestParam(required = false) String type,@RequestParam(required = false) String id) throws Exception {
        String jsonData = AppInject.templateService.getJSONOnlyFromURI(uri);
        model.addAttribute(AppConst.URI, uri);
        model.addAttribute(AppConst.TYPE, type);
        model.addAttribute(AppConst.ID, id);
        model.addAttribute(JSONTemplateConst.TEMPLATE, AppInject.templateParser.pageDefinitionInnerBody(uri, jsonData, type, id, true));
        return ViewResolver.ADMIN_INDEX;
    }


    @GetMapping(value = Endpoints.PREVIEW + Endpoints.URI)
    public String previewPage(Model model, @PathVariable String uri,@RequestParam(required = false) String type,@RequestParam(required = false) String id) throws Exception {
        String jsonData = AppInject.templateService.getJSONOnlyFromURI(uri);
        model.addAttribute(AppConst.URI, uri);
        model.addAttribute(AppConst.TYPE, type);
        model.addAttribute(JSONTemplateConst.TEMPLATE, AppInject.templateParser.pageDefinition(uri, jsonData, true, type, id));
        return ViewResolver.AUTH_INDEX;
    }

    //--------------------------------------------------------------------------------------------
    @PostMapping(value = Endpoints.PROCESS+Endpoints.SAVE)
    public String saveRecord(@RequestBody MultiValueMap map, RedirectAttributes redirectAttributes) throws Exception {
        Map<String, Object> map1 = map.toSingleValueMap();
        String uri = (String) map1.get(AppConst.URI);
        map1.put(AppConst.TYPE, AppConst.CREATE);
        try {
            AppInject.jdtScript.process(map1);
            redirectAttributes.addFlashAttribute(AppConst.SUCCESS_MESSAGE, "Record Created Successfully");
        } catch (ValidationException ex) {
            redirectAttributes.addFlashAttribute(AppConst.ERROR_MESSAGE, ex.getMessage());
            redirectAttributes.addFlashAttribute(AppConst.RECORD, new JSONObject(map.toSingleValueMap()).toString());
            return Endpoints.REDIRECT + Endpoints.ADMIN + Endpoints.SEPERATOR + uri + Endpoints.QUERY + Endpoints.QUERY_TYPE + AppConst.CREATE;

        }
        return Endpoints.REDIRECT + Endpoints.ADMIN + Endpoints.SEPERATOR + uri + Endpoints.QUERY + Endpoints.QUERY_TYPE + AppConst.LIST;
    }

    @PostMapping(value = Endpoints.PROCESS+Endpoints.UPDATE)
    public String updateRecord(@RequestBody MultiValueMap map, RedirectAttributes redirectAttributes) throws Exception {
        Map<String, Object> map1 = map.toSingleValueMap();
        String uri = (String) map1.get(AppConst.URI);
        map1.put(AppConst.TYPE, AppConst.UPDATE);
        try {
            AppInject.jdtScript.process(map1);
            redirectAttributes.addFlashAttribute(AppConst.SUCCESS_MESSAGE, "Record Updated Successfully");
        } catch (ValidationException ex) {
            redirectAttributes.addFlashAttribute(AppConst.ERROR_MESSAGE, ex.getMessage());
            return Endpoints.REDIRECT + Endpoints.ADMIN + Endpoints.SEPERATOR + uri + Endpoints.QUERY + Endpoints.QUERY_TYPE + AppConst.UPDATE + Endpoints.QUERY_AND + Endpoints.QUERY_ID + map1.get(AppConst.ID);

        }

        return Endpoints.REDIRECT + Endpoints.ADMIN + Endpoints.SEPERATOR + uri + Endpoints.QUERY + Endpoints.QUERY_TYPE + AppConst.LIST;
    }

    @PostMapping(value = Endpoints.PROCESS+Endpoints.SEARCH)
    public @ResponseBody String searchRecord(@RequestBody MultiValueMap map) throws Exception {
        Map<String, Object> map1 = map.toSingleValueMap();
        map1.put(AppConst.TYPE, AppConst.SEARCH);
        String uri = (String) map1.get(AppConst.URI);
        String jsonData = AppInject.templateService.getJSONOnlyFromURI(uri);
        return AppInject.templateParser.pageDefinition(uri, jsonData, false, true, AppConst.LIST, null, AppInject.jdtScript.processAndReturn(map1));
    }

    @GetMapping(value = Endpoints.PROCESS+Endpoints.DELETE)
    public String deleteRecord(@RequestParam String id,@RequestParam String uri, RedirectAttributes redirectAttributes) throws Exception {
        Map<String, Object> map1 = new HashMap();
        map1.put(AppConst.TYPE, AppConst.DELETE);
        map1.put(AppConst.ID, id);
        map1.put(AppConst.URI, uri);

        try {
            AppInject.jdtScript.process(map1);
            redirectAttributes.addFlashAttribute(AppConst.SUCCESS_MESSAGE, "Record Deleted Successfully");
        } catch (ValidationException ex) {
            redirectAttributes.addFlashAttribute(AppConst.ERROR_MESSAGE, "Record Cannot Be Deleted.");
        }
        return Endpoints.REDIRECT + Endpoints.ADMIN + Endpoints.SEPERATOR + uri + Endpoints.QUERY + Endpoints.QUERY_TYPE + AppConst.LIST;
    }

    @GetMapping(value = Endpoints.PROCESS+Endpoints.GET_ID)
    public @ResponseBody Map getById(@RequestParam String id,@RequestParam String uri) throws Exception {
        Map<String, Object> map1 = new HashMap();
        map1.put(AppConst.TYPE, AppConst.RETRIEVE_BY_ID);
        map1.put(AppConst.ID, id);
        map1.put(AppConst.URI, uri);
        return (Map) AppInject.jdtScript.processAndReturn(map1);
    }
    @GetMapping(value = Endpoints.PROCESS+Endpoints.GET)
    public void getAll(@RequestParam String uri) throws Exception {
        Map<String, Object> map1 = new HashMap();
        map1.put(AppConst.TYPE, AppConst.RETRIEVE);
        map1.put(AppConst.URI, uri);
        AppInject.jdtScript.process(map1);
    }

}
