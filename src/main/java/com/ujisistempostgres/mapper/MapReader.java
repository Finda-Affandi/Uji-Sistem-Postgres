package com.ujisistempostgres.mapper;

import com.ujisistempostgres.compareter.CompareList;
import com.ujisistempostgres.converter.listToLowercase;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MapReader {

    public List<String> mapping(String fileName) {
        String directoryPath = "src/main/resources/Mapping";

        List<String> columnList = new ArrayList<>();

        List<Map<String,Object>> mapResult = getMapping(directoryPath, fileName);

        for (Map<String, Object> result : mapResult) {
            columnList.add(result.get("columnName").toString() + " " + result.get("typeData"));
        }
        System.out.println(columnList);
        return columnList;
    }

    public String cmprMapper(List<String> colList){
        String fileMap = null;
        String directoryPath = "src/main/resources/Mapping";

        List<String> filenames = getAllFilenames(directoryPath);

        List<String> colMap = new ArrayList<>();
        for (String file : filenames) {
            List<Map<String, Object>> map = getMapping(directoryPath, file);
            for(Map<String, Object> x : map){
                colMap.add(x.get("columnName").toString());
            }
            int lastIndex = map.size() -1;
            colMap.remove(lastIndex);

            listToLowercase convToLow = new listToLowercase();
            List<String> lowCol = convToLow.listLowercase(colList);

            boolean cmpr = CompareList.compareLists(lowCol, convToLow.listLowercase(colMap));

            if (cmpr) {
                System.out.println("match");
                fileMap = file;
                break;
            }

            colMap.clear();

        }
        return fileMap;

    }

    public static List<String> getAllFilenames(String directoryPath) {
        List<String> filenames = new ArrayList<>();

        // Create a File object for the directory
        File directory = new File(directoryPath);

        // Check if the directory exists
        if (directory.exists() && directory.isDirectory()) {
            // Get all the files in the directory
            File[] files = directory.listFiles();

            // Iterate through the files and add their names to the list
            for (File file : files) {
                if (file.isFile()) {
                    filenames.add(file.getName());
                }
            }
        }

        return filenames;
    }

    public static List<Map<String, Object>> getMapping(String path, String filename) {
        String csvFile = path + "/" + filename;
        String line;

        List<Map<String, Object>> mappingList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            line = br.readLine(); // Read and discard the header line
            String[] columnNames = line.split(",");
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                Map<String, Object> dataMap = new HashMap<>();
                for (int i = 0; i < columnNames.length; i++) {
                    dataMap.put(columnNames[i], data[i]);
                }
                mappingList.add(dataMap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mappingList;
    }

}
