package com.jsondriventemplate.app;

import com.jsondriventemplate.AppInject;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication(scanBasePackages = "com.jsondriventemplate")
public class JSONDrivenTemplateApplication implements CommandLineRunner {

    @PostConstruct
    public void initApp(){
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    public static void main(String[] args) {
        SpringApplication.run(JSONDrivenTemplateApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        AppInject.defaultLoader.createUpdateDefaultJSON();
        AppInject.defaultLoader.loadDefaultUser();
        AppInject.defaultLoader.defaultJSONLoader();
    }



}
