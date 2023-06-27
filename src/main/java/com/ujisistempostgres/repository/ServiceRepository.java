package com.ujisistempostgres.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSetMetaData;
import java.util.*;

import static org.yaml.snakeyaml.nodes.NodeId.mapping;

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

    public void insertData(List<Map<String, Object>> dataList, String tableName) {
        String columns = String.join(", ", dataList.get(0).keySet());

        List<Object[]> batchParams = new ArrayList<>();

        for (Map<String, Object> dataMap : dataList) {
            Object[] values = dataMap.values().toArray();
            batchParams.add(values);
        }

        String placeholders = String.join(", ", Collections.nCopies(dataList.get(0).size(), "?"));

        String sql = String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, columns, placeholders);

        jdbcTemplate.batchUpdate(sql, batchParams);
    }

    public String createTable(List<String> columns) {
        String tableName = "saleslineframe";
        List<String> column = new ArrayList<>();

        for (String col : columns) {
            String sqlCol = col + mapping ;
            column.add(sqlCol);
        }

        column.add("PRIMARY KEY (RCVNO)");

        String cols = String.join(", ", column);

        String sql = String.format("CREATE TABLE %s (%S)", tableName, cols);
        jdbcTemplate.update(sql);

        return tableName;
    }
}
