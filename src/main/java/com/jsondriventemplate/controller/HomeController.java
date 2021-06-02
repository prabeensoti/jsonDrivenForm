package com.jsondriventemplate.controller;

import com.jsondriventemplate.AppInject;
import com.jsondriventemplate.constant.AppConst;
import com.jsondriventemplate.constant.Endpoints;
import com.jsondriventemplate.constant.JSONTemplateConst;
import com.jsondriventemplate.constant.ViewResolver;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping(value = Endpoints.CONTEXT)
    public String indexPage() {
        return ViewResolver.INDEX;
    }

    @GetMapping(value = Endpoints.LOGIN)
    public String loginPage(Model model) throws Exception {
        String login = AppConst.LOGIN;
        String jsonData = AppInject.templateService.getJSONOnlyFromURI(login);
        model.addAttribute(JSONTemplateConst.TEMPLATE, AppInject.templateParser.pageDefinition(login, jsonData, AppConst.BLANK));
        return ViewResolver.AUTH_INDEX;
    }
}
