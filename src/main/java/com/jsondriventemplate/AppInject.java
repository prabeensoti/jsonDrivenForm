package com.jsondriventemplate;

import com.jsondriventemplate.config.MessageReader;
import com.jsondriventemplate.logic.*;
import com.jsondriventemplate.repo.MongoClientProvider;
import freemarker.template.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AppInject {

    public static TemplateParser templateParser;
    public static Configuration configuration;
    public static MessageReader messageReader;
    public static MongoClientProvider mongoClientProvider;
    public static JsonTemplateService templateService;
    public static JSONValidator jsonValidator;
    public static JDTScript jdtScript;
    public static PasswordEncoder passwordEncoder;
    public static DefaultLoader defaultLoader;

    public AppInject(TemplateParser templateParser, MessageReader messageReader, MongoClientProvider mongoClientProvider, JsonTemplateService templateService,
                     Configuration configuration, JSONValidator jsonValidator,JDTScript jdtScript,PasswordEncoder passwordEncoder,
                     DefaultLoader defaultLoader
    ) {
        AppInject.templateParser = templateParser;
        AppInject.messageReader = messageReader;
        AppInject.configuration = configuration;
        AppInject.mongoClientProvider = mongoClientProvider;
        AppInject.templateService = templateService;
        AppInject.jsonValidator = jsonValidator;
        AppInject.jdtScript = jdtScript;
        AppInject.passwordEncoder = passwordEncoder;
        AppInject.defaultLoader = defaultLoader;
    }
}
