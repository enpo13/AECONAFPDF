package com.aecon.utils;

public final class Consts {
    private Consts(){}

    public static final String USER_DIR = "C:/Users/"+System.getProperty("user.name")+"/AECONAF/";
    public static final String DOWNLOADS_FOLDER = "C:/Users/"+System.getProperty("user.name")+"/Downloads/";
    public static final String SCREENSHOT_OUT_DIR = USER_DIR + "/src/main/resources/screenshots/";
    public static final String PARAMETER_FILE = "Z:/UFT/QA/Inbound/Staging/Automation Framework/Excel/Parameters.xlsx";
    public static final String PARAMETER_FILE_XML = "Z:/UFT/QA/Inbound/Staging/Automation Framework/Excel/Parameters_FDC.xml";
    public static final String PARAMETER_FILE_LOCAL = "C:\\Users\\mpoltorak\\Documents\\a\\Parameters.xlsx";
    public static final String JSON_ALM_INPUT = "C:/Users/" + System.getProperty("user.name") + "/AppData/Local/Temp/LeanFTRA/TestData.json";
    public static final String JSON_ALM_OUTPUT_FOLDER = "C:/Users/" + System.getProperty("user.name") + "/AppData/Local/Temp/LeanFTRA/";
    public static final String PASSED = "[PASSED]";
    public static final String FAILED = "[FAILED]";
    public static final String SKIPPED = "[SKIPPED]";

    public static final boolean SUCCESS = true;
    public static final boolean FAILURE = false;

    public static final String EMPTY_STRING = "";
    public static final String SPACE = " ";
    public static final String TAB = "\t";

    public static final String ALPHA_STRING_UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String ALPHA_STRING_LOWERCASE = ALPHA_STRING_UPPERCASE.toLowerCase();
    public static final String NUMERIC_STRING = "0123456789";
    public static final String ALPHANUMERIC_STRING = ALPHA_STRING_UPPERCASE + ALPHA_STRING_LOWERCASE + NUMERIC_STRING;

}

