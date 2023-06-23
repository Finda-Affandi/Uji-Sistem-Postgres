package com.ujisistempostgres.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSetMetaData;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MahasiswaRepository {
    private final JdbcTemplate jdbcTemplate;

    public MahasiswaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> getAllMahasiswa() {
        String sql = "SELECT * FROM mahasiswa";
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            Map<String, Object> mahasiswaMap = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                Object columnValue = resultSet.getObject(i);
                mahasiswaMap.put(columnName, columnValue);
            }

            return mahasiswaMap;
        });
    }

    public void insertMahasiswa(Map<String, Object> mahasiswaMap) {
        String tableName = "mahasiswa";
        String columns = String.join(", ", mahasiswaMap.keySet());
        String placeholders = String.join(", ", Collections.nCopies(mahasiswaMap.size(), "?"));

        String sql = String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, columns, placeholders);
        jdbcTemplate.update(sql, mahasiswaMap.values().toArray());
    }

//    public void deleteMahasiswa(int nim) {
//        String sql = "DELETE FROM mahasiswa WHERE nim = " + nim;
//        jdbcTemplate.update(sql);
//    }
//
//    public void updateTask(Mahasiswa mahasiswa) {
//        String sql = "UPDATE mahasiswa SET nama = "+ mahasiswa.getNama() +", alamat = "+ mahasiswa.getAlamat() +" WHERE nim = "+ mahasiswa.getNim();
//        jdbcTemplate.update(sql);
//    }
}
