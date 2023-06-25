package com.ujisistempostgres.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSetMetaData;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ServiceRepository {
    private final JdbcTemplate jdbcTemplate;

    public ServiceRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> getAllData() {
        String tableName = "saleslineframe";
        String sql = String.format("SELECT * FROM %s", tableName);
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            Map<String, Object> dataMap = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                Object columnValue = resultSet.getObject(i);
                dataMap.put(columnName, columnValue);
            }

            return dataMap;
        });
    }

    public void insertData(Map<String, Object> dataMap) {
        String tableName = "saleslineframe";
        String columns = String.join(", ", dataMap.keySet());
        String placeholders = String.join(", ", Collections.nCopies(dataMap.size(), "?"));

        String sql = String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, columns, placeholders);
        jdbcTemplate.update(sql, dataMap.values().toArray());
    }
}
