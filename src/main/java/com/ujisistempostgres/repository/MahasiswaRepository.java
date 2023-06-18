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
        String sql = "INSERT INTO mahasiswa (nim, nama, alamat) VALUES ("+ mahasiswa.getNim() +", '"+ mahasiswa.getNama() +"', '"+ mahasiswa.getAlamat() +"')";
        jdbcTemplate.update(sql);
    }

    public void deleteMahasiswa(int nim) {
        String sql = "DELETE FROM mahasiswa WHERE nim = " + nim;
        jdbcTemplate.update(sql);
    }

    public void updateTask(Mahasiswa mahasiswa) {
        String sql = "UPDATE mahasiswa SET nama = "+ mahasiswa.getNama() +", alamat = "+ mahasiswa.getAlamat() +" WHERE nim = "+ mahasiswa.getNim();
        jdbcTemplate.update(sql);
    }
}
