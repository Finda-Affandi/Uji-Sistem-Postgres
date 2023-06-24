package com.ujisistempostgres.controller;

import com.ujisistempostgres.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            // Handle the exception and return an appropriate response status
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/mahasiswa")
    public void insertData(@RequestBody List<Map<String, Object>> dataList) {
        for (Map<String, Object> data : dataList) {
            serviceRepository.insertData(data);
        }
    }


//    @DeleteMapping("/mahasiswa/{nim}")
//    public void deleteTask(@PathVariable("nim") int nim) {
//        mahasiswaRepository.deleteMahasiswa(nim);
//    }
//
//    @PutMapping("/mahasiswa/{nim}")
//    public void updateTask(@PathVariable("nim") int nim, @RequestBody Mahasiswa mahasiswa) {
//        mahasiswa.setNim(nim);
//        mahasiswaRepository.updateTask(mahasiswa);
//    }
}
