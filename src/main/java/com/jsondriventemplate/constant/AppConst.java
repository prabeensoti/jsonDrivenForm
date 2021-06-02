package com.jsondriventemplate.constant;

import java.util.Arrays;
import java.util.List;

public final class AppConst {

    public final static String ID = "_id";
    public final static String JSON = "json";
    public final static String LOGIN = "login";
    public final static String BLANK_JSON = "{}";
    public final static String TEMPLATE = "template";
    public final static String TEMPLATE_LIST = "templateList";
    public final static String QUERY = "query";
    public final static String NO_DISPLAY = "Nothing to display";
    public final static String BLANK = "";
    public final static String URL = "url";
    public final static String URI = "uri";

    public final static String TYPE = "type";
    public final static String SUCCESS = "success";
    public final static String SUCCESS_MESSAGE = "successMessage";
    public final static String ERROR_MESSAGE = "errorMessage";
    public final static String RECORD = "record";


    public final static String CREATE = "create";
    public final static String UPDATE = "update";
    public final static String DELETE = "delete";
    public final static String SEARCH = "search";
    public final static String RETRIEVE = "retrieve";
    public final static String RETRIEVE_BY_ID = "retrieveById";

    public final static String LIST = "list";
    public final static String LIST_VALUE = "list_value";
    public final static String EMPLOYEE = "employee";

    public static final List<String> JDT_SCRIPT = Arrays.asList(
            CREATE, UPDATE, DELETE, RETRIEVE, RETRIEVE_BY_ID, SEARCH
    );

}
