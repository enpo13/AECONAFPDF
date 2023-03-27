package com.aecon.utils;

import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {

    public static long getRandomNumber() {
        long time = System.currentTimeMillis();
        return time;
    }

    public static String getToday() {
        return new SimpleDateFormat("MM/dd/yyyy").format(new Date());
    }


    public static String getTimesheetFile(String timesheetNumber) {
        String filePath = null;
        List<String> result = null;

        try (Stream<Path> walk = Files.walk(Paths.get(Consts.DOWNLOADS_FOLDER))) {
            result = walk
                    .filter(p -> !Files.isDirectory(p))
                    .map(p -> p.toString().toLowerCase())
                    .filter(f -> f.endsWith(".xls") && f.contains(timesheetNumber))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (result.size() == 1)
            filePath = result.get(0);
        return filePath;
    }

    public static String[] removeElementFromArray(String[] source, int index) {
        if (source == null) {
            return source;
        }
        return ArrayUtils.remove(source, index);
    }

    public static int getFileModeIndex(String[] source) {
        String string = "IP_FileMode";
        int index = -1;
        if (ArrayUtils.contains(source, string)) {
            index = ArrayUtils.indexOf(source, string);
        }
        return index;
    }

    public static List<String> getListOfFiles(String folder) {
        List<String> result = null;
        try (Stream<Path> walk = Files.walk(Paths.get(folder))) {
            // We want to find only regular files
            result = walk.filter(Files::isRegularFile)
                    .map(x -> x.toString()).collect(Collectors.toList());

            result.forEach(System.out::println);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


}
