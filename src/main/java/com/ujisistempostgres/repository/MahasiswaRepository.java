package com.ujisistempostgres.repository;

import com.ujisistempostgres.entity.Mahasiswa;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MahasiswaRepository extends CrudRepository<Mahasiswa, Long> {
    List<Mahasiswa> findAll();
}
