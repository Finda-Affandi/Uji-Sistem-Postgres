package com.ujisistempostgres.repository;

import com.ujisistempostgres.entity.Mahasiswa;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MahasiswaRepository {
    private final JdbcTemplate jdbcTemplate;

    public MahasiswaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Mahasiswa> getAllMahasiswa() {
        String sql = "SELECT * FROM mahasiswa";
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> {
            Mahasiswa mahasiswa = new Mahasiswa();
            mahasiswa.setNim(resultSet.getInt("nim"));
            mahasiswa.setNama(resultSet.getString("nama"));
            mahasiswa.setAlamat(resultSet.getString("alamat"));
            return mahasiswa;
        });
    }

    public void insertMahasiswa(Mahasiswa mahasiswa) {
        String sql = "INSERT INTO mahasiswa (nim, nama, alamat) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, mahasiswa.getNim(), mahasiswa.getNama(), mahasiswa.getAlamat());
    }

    public void deleteMahasiswa(int nim) {
        String sql = "DELETE FROM mahasiswa WHERE nim = ?";
        jdbcTemplate.update(sql, nim);
    }

    public void updateTask(Mahasiswa mahasiswa) {
        String sql = "UPDATE mahasiswa SET nama = ?, alamat = ? WHERE nim = ?";
        jdbcTemplate.update(sql, mahasiswa.getNama(), mahasiswa.getAlamat(), mahasiswa.getNim());
    }
}
