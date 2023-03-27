package com.aecon.utils;


import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

public class ExcelParser {

    public static HashMap<String, String> getMapFromExcel(String filePath) {
        FileInputStream file;
        DataFormatter formatter = new DataFormatter();
        HashMap<String, String> map = new HashMap<String, String>();
        try {
            file = new FileInputStream(filePath);
            XSSFWorkbook wb = new XSSFWorkbook(file);
            XSSFSheet sh = wb.getSheet("Sheet1");
            for (int r = 0; r <= sh.getLastRowNum(); r++) {
                String key = formatter.formatCellValue(sh.getRow(r)
                        .getCell(0));
                String value = formatter.formatCellValue(sh.getRow(r)
                        .getCell(1));
                if (!key.equals(""))
                    map.put(key, value);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }

    public static void main(String[] args) {
        getMapFromExcel("Z:/UFT/QA/Inbound/Staging/Automation Framework/Excel/parametersPDF.xlsx");
    }
}

