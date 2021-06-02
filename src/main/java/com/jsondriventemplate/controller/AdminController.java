package com.jsondriventemplate.controller;

import com.jsondriventemplate.AppInject;
import com.jsondriventemplate.constant.AppConst;
import com.jsondriventemplate.constant.Endpoints;
import com.jsondriventemplate.constant.JSONTemplateConst;
import com.jsondriventemplate.constant.ViewResolver;
import com.jsondriventemplate.exception.JSONValidationException;
import com.jsondriventemplate.repo.DBConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = Endpoints.ADMIN)
public class AdminController {

    @GetMapping(value = Endpoints.EDITOR)
    public String editor(Model model, @RequestParam(name = JSONTemplateConst.query, defaultValue = AppConst.BLANK, required = false) String query) throws Exception {
        Map<String, Object> jsonData = null;
        String previewURL = AppConst.BLANK;
        if (StringUtils.isNotBlank(query)) {
            jsonData = AppInject.templateService.getJSONFromURIEditorView(query);
            previewURL = ViewResolver.ADMIN_PREVIEW + query;
        }

        if (jsonData == null) {
            jsonData = new HashMap();
            jsonData.put(AppConst.ID, AppInject.templateService.getURIID(query));
            jsonData.put(AppConst.JSON, AppConst.BLANK_JSON);
        }

        model.addAttribute(AppConst.QUERY, query);
        model.addAttribute(JSONTemplateConst.PREVIEW_URL, previewURL);
        model.addAttribute(JSONTemplateConst.unProcessedJSON, jsonData);
        model.addAttribute(JSONTemplateConst.jsonList, AppInject.mongoClientProvider.findAll(DBConstant.TEMPLATE_INFORMATION));
        return ViewResolver.ADMIN_EDITOR;
    }

    @PostMapping(value = Endpoints.EDITOR)
    public @ResponseBody
    String pageUpdate(@RequestBody MultiValueMap jsonMap) {
        String jsonData = (String) jsonMap.toSingleValueMap().get(AppConst.JSON);
        if (StringUtils.isBlank(jsonData)) {
            return AppConst.NO_DISPLAY;
        }
        try{
            return AppInject.templateParser.pageDefinition(null, jsonData, AppConst.BLANK);
        }catch (Exception x){
            return AppConst.BLANK;
        }

    }

    //------------------------------------------------------------------------------------------------------------------
    @GetMapping(value = Endpoints.JSON_TEMPLATE)
    public String jsonTemplate(Model model) {
        List all = AppInject.mongoClientProvider.findAll(DBConstant.TEMPLATE_INFORMATION);
        model.addAttribute(AppConst.TEMPLATE_LIST, all);
        return ViewResolver.ADMIN_JSON_TEMPLATE;
    }

    @PostMapping(value = Endpoints.JSON_TEMPLATE)
    public String saveJsonTemplateInfo(@RequestBody MultiValueMap valueMap) {
        Map dataMap = valueMap.toSingleValueMap();
        Map data = AppInject.mongoClientProvider.findByAtt(AppConst.URL, (String) dataMap.get(AppConst.URL), DBConstant.TEMPLATE_INFORMATION);
        if (data == null) {
            AppInject.mongoClientProvider.save(dataMap, DBConstant.TEMPLATE_INFORMATION);
        } else if (dataMap.containsKey(AppConst.ID) && dataMap.get(AppConst.ID).toString().equals(data.get(AppConst.ID).toString())) {
            AppInject.mongoClientProvider.save(dataMap, DBConstant.TEMPLATE_INFORMATION);
        }
        return Endpoints.REDIRECT + Endpoints.ADMIN + Endpoints.JSON_TEMPLATE;
    }

    @GetMapping(value = Endpoints.JSON_TEMPLATE + Endpoints.ID)
    public String deleteJsonTemplateInfo(@PathVariable String id) {
        AppInject.mongoClientProvider.delete(id, DBConstant.TEMPLATE_INFORMATION);
        AppInject.mongoClientProvider.delete(id, DBConstant.JSON_TEMPLATE_DEFINITION);
        return Endpoints.REDIRECT + Endpoints.ADMIN + Endpoints.JSON_TEMPLATE;
    }

    @GetMapping(value = Endpoints.JSON_TEMPLATE +Endpoints.EDIT+ Endpoints.ID)
    public String editJsonTemplateInfo(@PathVariable String id,Model model) {
        Map data = AppInject.mongoClientProvider.findById(id,DBConstant.TEMPLATE_INFORMATION);
        model.addAttribute(AppConst.TEMPLATE, data);
        return ViewResolver.ADMIN_JSON_TEMPLATE_EDIT;
    }

    @PostMapping(value = Endpoints.JSON_DEFINITION)
    public @ResponseBody String saveJsonDefintion(@RequestBody MultiValueMap<String, Object> map) throws JSONValidationException {
        Map<String, Object> stringObjectMap = map.toSingleValueMap();
        String json = AppInject.jsonValidator.validJSONFormat((String) stringObjectMap.get(AppConst.JSON));
        stringObjectMap.put(AppConst.JSON, json);
        AppInject.mongoClientProvider.save(stringObjectMap, DBConstant.JSON_TEMPLATE_DEFINITION);
        return AppConst.SUCCESS;
    }


}
