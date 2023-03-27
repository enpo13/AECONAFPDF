package com.aecon.logic.overrided;

import com.aecon.utils.Consts;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.testng.annotations.DataProvider;

import java.io.*;
import java.lang.reflect.Method;
import java.util.*;

public class DataProviders {
    @DataProvider(name = "ALM_O")
    public static Object[][] data(Method method) throws Exception {
        //uncomment this for not ALM run

        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(Consts.JSON_ALM_INPUT);
        Map<String, ArrayList> result = objectMapper.readValue(file, HashMap.class);
        ArrayList dataList = (ArrayList) result.get("data").get(0);
        Object[][] data = new Object[1][dataList.size()];
        for (int j = 0; j < dataList.size(); j++) {
            data[0][j] = dataList.get(j).toString();
        }
        return data;
    }

    //    public static void setOutputParameter(String output) throws IOException {
//        String updatedContent;
//        String content = Files.readFile(new File(Consts.JSON_ALM_INPUT));
//
//        if (StringUtils.countMatches(content,"data")==1) {
//            updatedContent = content.replace("}", ",\"data\":[[\"" + output + "\"]]}");
//        }else updatedContent = content.replace("]]}", ",\""+output+"\"]]}");
//        Files.writeFile(updatedContent,new File(Consts.JSON_ALM_INPUT));
//    }
    public static void setOutputParameter(Map<String, Object> data) throws IOException {
        String[] parameters = (String[]) data.keySet().toArray(new String[1]);
        String[] values = (String[]) data.values().toArray(new String[1]);
        List<String[]> valueRows = new ArrayList();
        valueRows.add(values);
        String[][] rows = (String[][]) valueRows.toArray(new String[1][]);
        TreeMap<String, Object> endData = new TreeMap<>(Collections.reverseOrder());
        endData.put("outputParameters", parameters);
        endData.put("data", rows);

        File targetFile = new File(Consts.JSON_ALM_OUTPUT_FOLDER, "OutputData.json");
        Writer writer = new FileWriter(targetFile);
        Gson gson = new Gson();
        gson.toJson(endData, writer);
        writer.close();
    }

    public static String[] getInputParametersNames() throws FileNotFoundException {
        ArrayList<String> names;
        File target = new File(Consts.JSON_ALM_INPUT);
        Reader reader = new FileReader(target);
        Gson gson = new Gson();
        TreeMap<String, ArrayList<String>> map = gson.fromJson(reader, TreeMap.class);
        names = map.get("parameters");
        return names.toArray(new String[0]);
    }


    public static void main(String[] args) throws FileNotFoundException {
        getInputParametersNames();
    }

}
//{"parameters":["equipment"],"data":[["10083631"]],"outputParameters":["Out_isPassed"],"data":[["111"]]}