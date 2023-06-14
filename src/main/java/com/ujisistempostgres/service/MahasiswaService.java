package com.ujisistempostgres.service;

import com.ujisistempostgres.entity.Mahasiswa;
import com.ujisistempostgres.repository.MahasiswaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MahasiswaService {
    @Autowired
    MahasiswaRepository mahasiswaRepository;

    @GetMapping
    public List<Mahasiswa> getAllUsers() {
        return mahasiswaRepository.findAll();
    }
}
