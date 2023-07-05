package com.ujisistempostgres.controller;

import com.ujisistempostgres.compareter.CompareList;
import com.ujisistempostgres.converter.listToLowercase;
import com.ujisistempostgres.mapper.MapReader;
import com.ujisistempostgres.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api")
public class ServiceController {
    private final ServiceRepository serviceRepository;

    @Autowired
    public ServiceController(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @GetMapping("/postgres")
    public ResponseEntity<List<Map<String, Object>>> getAllData(@RequestHeader HttpHeaders headers) {
        try {
            String tableName = headers.getFirst("table-name");
            List<Map<String, Object>> dataList = serviceRepository.getAllData(tableName);
            return ResponseEntity.ok(dataList);
        } catch (Exception e) {
            String eMessage = "An error while retrieving data";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

//    @GetMapping("/getAllDataPostgres")
//    public ResponseEntity<List<Map<String, Object>>> getAllDataforService(@RequestHeader HttpHeaders headers) {
//        try {
//            List<String> tableNames = serviceRepository.getAllTableNames();
//            List<List<Map<String, Object>>> dataLists = serviceRepository.getBothData(tableNames);
//
//            List<Map<String, Object>> combinedData = new ArrayList<>();
//            for (List<Map<String, Object>> dataList : dataLists) {
//                combinedData.addAll(dataList);
//            }
//
//            return ResponseEntity.ok(combinedData);
//        } catch (Exception e) {
//            String eMessage = "An error occurred while retrieving data";
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//    @GetMapping("/chooseTablePosgres")
//    public ResponseEntity<List<String>> chooseTablePosgres(@RequestHeader HttpHeaders headers) {
//        try {
//            List<String> tableNames = serviceRepository.getAllTableNamesForChoose();
//            return ResponseEntity.ok()
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .body(tableNames);
//        } catch (Exception e) {
//            String eMessage = "An error occurred while retrieving data";
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//    @GetMapping("/getByTablePostgres")
//    public ResponseEntity<List<Map<String, Object>>> getByTablePostgres(
//            @RequestHeader HttpHeaders headers,
//            @RequestHeader("param") String param) {
//        try {
//            String[] tableNames = serviceRepository.getAllTableNamesForSelectByTable();
//            List<Map<String, Object>> result = new ArrayList<>();
//
//            int paramIndex = Integer.parseInt(param) - 1;
//
//            if (paramIndex >= 0 && paramIndex < tableNames.length) {
//                String tableName = tableNames[paramIndex];
//
//                Map<String, Object> paramMap = new HashMap<>();
//                List<List<Map<String, Object>>> dataLists = serviceRepository.getBothData(Collections.singletonList(tableName));
//                paramMap.put("Data : ", dataLists);
//                result.add(paramMap);
//            } else {
//                Map<String, Object> paramMap = new HashMap<>();
//                paramMap.put("param", param);
//                paramMap.put("message", "Invalid param index: " + param);
//                result.add(paramMap);
//            }
//
//            return ResponseEntity.ok(result);
//        } catch (Exception e) {
//            String eMessage = "An error occurred while retrieving data";
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }

    @PostMapping("/postgres")
    public ResponseEntity<String> insertData(
            @RequestHeader HttpHeaders headers,
            @RequestBody List<Map<String, Object>> dataList
    ) {
        try {
            System.out.println("insert");
            String table = headers.getFirst("table-name");
            System.out.println(table);
            serviceRepository.insertData(dataList, table);
            return ResponseEntity.ok("Data inserted succesfully!");
        } catch (Exception e) {
            String eMessage = "Failed to insert data!";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(eMessage);
        }
    }


//    @PostMapping("/postgres")
//    public ResponseEntity<String> insertData(
//            @RequestHeader HttpHeaders headers,
//            @RequestBody List<Map<String, Object>> dataList) {
//        try {
//
//            List<String> key = new ArrayList<>();
//            for (Map<String, Object> data : dataList) {
//                key.addAll(data.keySet());
//                break;
//            }
//            listToLowercase convToLow = new listToLowercase();
//            List<String> lowKey = convToLow.listLowercase(key);
//
//            List<String> tableName = serviceRepository.getAllTableNames();
//
//            List<Boolean> isDuplicate = new ArrayList<>();
//            for (String table : tableName) {
//                List<String> column = convToLow.listLowercase(serviceRepository.getColumnList(table));
//                boolean cmpr = CompareList.compareLists(lowKey, column);
//                if (cmpr) {
//                    isDuplicate.add(true);
//                } else {
//                    isDuplicate.add(false);
//                }
//            }
//
//            boolean createTable = !isDuplicate.contains(true);
//
//            String newTableName = headers.getFirst("table-name");
//            System.out.println(newTableName);
//
//            if (createTable) {
//                MapReader mapReader = new MapReader();
//                String fileName = mapReader.cmprMapper(lowKey);
//                if (fileName != null) {
//                    List<String> columnList = mapReader.mapping(fileName);
//                    serviceRepository.createTableWithMap(columnList, newTableName);
//                    serviceRepository.insertData(dataList, newTableName);
//                } else {
//                    serviceRepository.createTable(key, newTableName);
//                    serviceRepository.insertData(dataList, newTableName);
//                }
//            } else {
//                serviceRepository.insertData(dataList, newTableName);
//            }
//            return ResponseEntity.ok("Data inserted succesfully!");
//        } catch (Exception e) {
//            String eMessage = "Failed to insert data!";
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(eMessage);
//        }
//    }

    @PostMapping("/postgres/create-table")
    public void createTable(
            @RequestHeader HttpHeaders headers,
            @RequestBody Map<String, Object> dataColumn)
    {
        String tableName = headers.getFirst("table-name");
        serviceRepository.createTable(dataColumn, tableName);
    }
}
