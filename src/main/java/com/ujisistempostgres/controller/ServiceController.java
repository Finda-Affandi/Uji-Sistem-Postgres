package com.ujisistempostgres.controller;

import com.ujisistempostgres.entity.Mahasiswa;
import com.ujisistempostgres.repository.MahasiswaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api")
public class ServiceController {
    private final MahasiswaRepository mahasiswaRepository;

    @Autowired
    public ServiceController(MahasiswaRepository mahasiswaRepository) {
        this.mahasiswaRepository = mahasiswaRepository;
    }

    @GetMapping("/mahasiswa")
    public List<Mahasiswa> getAllMahasiswa() {
        return mahasiswaRepository.getAllMahasiswa();
    }

    @PostMapping("/mahasiswa")
    public void createMahasiswa(@RequestBody Mahasiswa mahasiswa) {
        mahasiswaRepository.insertMahasiswa(mahasiswa);
    }

    @DeleteMapping("/mahasiswa/{nim}")
    public void deleteTask(@PathVariable("nim") int nim) {
        mahasiswaRepository.deleteMahasiswa(nim);
    }

    @PutMapping("/mahasiswa/{nim}")
    public void updateTask(@PathVariable("nim") int nim, @RequestBody Mahasiswa mahasiswa) {
        mahasiswa.setNim(nim);
        mahasiswaRepository.updateTask(mahasiswa);
    }
}
