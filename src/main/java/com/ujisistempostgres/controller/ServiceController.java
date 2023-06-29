package com.ujisistempostgres.controller;

import com.ujisistempostgres.compareter.CompareList;
import com.ujisistempostgres.converter.listToLowercase;
import com.ujisistempostgres.mapper.MapReader;
import com.ujisistempostgres.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<List<Map<String, Object>>> getAllData() {
        try {
            List<Map<String, Object>> dataList = serviceRepository.getAllData();
            return ResponseEntity.ok(dataList);
        } catch (Exception e) {
            String eMessage = "An error while retrieving data";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/postgres")
    public ResponseEntity<String> insertData(
            @RequestHeader HttpHeaders headers,
            @RequestBody List<Map<String, Object>> dataList) {
        try {
//
//            for (Map<String, Object> data : dataList) {
//                System.out.println(data);
//            }


            List<String> key = new ArrayList<>();
            for (Map<String, Object> data : dataList) {
                key.addAll(data.keySet());
                break;
            }
            //Change column name from csv into lowercase
            listToLowercase convToLow = new listToLowercase();
            List<String> lowKey = convToLow.listLowercase(key);

            //Get all table from DB
            List<String> tableName = serviceRepository.getAllTableNames();

            //List for get compare result
            List<Boolean> isDuplicate = new ArrayList<>();
            for (String table : tableName) {
                List<String> column = convToLow.listLowercase(serviceRepository.getColumnList(table));
                boolean cmpr = CompareList.compareLists(lowKey, column);
                if (cmpr) {
                    isDuplicate.add(true);
                } else {
                    isDuplicate.add(false);
                }
            }

            //Check is column in database dublicate
            boolean createTable = !isDuplicate.contains(true);

            //Get table name from request header (Based csv filename)
            String newTableName = headers.getFirst("table-name");
            System.out.println(newTableName);

            //Create new table if table column name is not duplicate, and insert data into exsisting table if table column name duplicate
            if (createTable) {
                MapReader mapReader = new MapReader();
                String fileName = mapReader.cmprMapper(lowKey);
                if (fileName != null) {
                    List<String> columnList = mapReader.mapping(fileName);
                    serviceRepository.createTableWithMap(columnList, newTableName);
                    serviceRepository.insertData(dataList, newTableName);
                } else {
                    serviceRepository.createTable(key, newTableName);
                    serviceRepository.insertData(dataList, newTableName);
                }
            } else {
                serviceRepository.insertData(dataList, newTableName);
            }
            return ResponseEntity.ok("Data inserted succesfully!");
        } catch (Exception e) {
            String eMessage = "Failed to insert data!";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(eMessage);
        }
    }

    @PostMapping("/coba")
    public void cobah(){
//        MapReader cb = new MapReader();
//        cb.cmprMapper();
    }
}
