package com.ujisistempostgres.controller;

import com.ujisistempostgres.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//dava
@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api")
public class ServiceController {
    private final ServiceRepository serviceRepository;

    @Autowired
    public ServiceController(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }



    @GetMapping("/mahasiswa")
    public ResponseEntity<List<Map<String, Object>>> getAllDataforService() {
        try {
            String tableName = "mahasiswa";
            List<Map<String, Object>> dataLists = serviceRepository.getBothData(tableName);

            return ResponseEntity.ok(dataLists);
        } catch (Exception e) {
            String eMessage = "Terjadi kesalahan saat mengambil data";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/mahasiswa")
    public ResponseEntity<String> insertData(
            @RequestBody List<Map<String, Object>> dataList
    ) {
        try {
            String table = ("mahasiswa");
            serviceRepository.insertData(dataList, table);

            return ResponseEntity.ok("Berhasil menambah Data!");
        } catch (Exception e) {
            String eMessage = "Failed to insert data!";
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", eMessage);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse.toString());
        }
    }

    @PutMapping("/mahasiswa/{id}")
    public ResponseEntity<String> updateData(
            @PathVariable("id") String id,
            @RequestBody Map<String, Object> newData
    ) {
        try {
            String table = "mahasiswa";
            serviceRepository.updateData(id, newData, table);

            return ResponseEntity.ok("Data berhasil diperbarui!");
        } catch (Exception e) {
            String eMessage = "Gagal memperbarui data!";
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", eMessage);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse.toString());
        }
    }

    @DeleteMapping("/mahasiswa/{id}")
    public ResponseEntity<String> deleteData(@PathVariable("id") String id) {
        try {
            String table = "mahasiswa";
            serviceRepository.deleteData(id, table);

            return ResponseEntity.ok("Data berhasil dihapus!");
        } catch (Exception e) {
            String eMessage = "Gagal menghapus data!";
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", eMessage);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse.toString());
        }
    }


}

