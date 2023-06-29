package com.ujisistempostgres.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSetMetaData;
import java.util.*;

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

//    public void insertData(Map<String, Object> dataMap) {
//        String tableName = "saleslineframe";
//        String columns = String.join(", ", dataMap.keySet());
//        String placeholders = String.join(", ", Collections.nCopies(dataMap.size(), "?"));
//
//        String sql = String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, columns, placeholders);
//        jdbcTemplate.update(sql, dataMap.values().toArray());
//    }

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

    public List<String> getAllTableNames() {
        String query = "SELECT tablename FROM pg_catalog.pg_tables WHERE schemaname = 'public';";
        return jdbcTemplate.queryForList(query, String.class);
    }

    public List<String> getColumnList(String table) {
        String sql = "SELECT column_name FROM information_schema.columns WHERE table_name = ?";
        List<String> columnList = new ArrayList<>();

        jdbcTemplate.query(sql, new Object[]{table}, (rs, rowNum) -> {
            String columnName = rs.getString("column_name");
            columnList.add(columnName);
            return null;
        });

        return columnList;
    }

    public void createTable(List<String> columns, String tableName) {
        List<String> column = new ArrayList<>();

        for (String col : columns) {
            String sqlCol = col + " VARCHAR(100)";
            column.add(sqlCol);
        }

        String cols = String.join(", ", column);

        String sql = String.format("CREATE TABLE IF NOT EXISTS %s (%S)", tableName, cols);
        jdbcTemplate.update(sql);
    }

    public void createTableWithMap(List<String> columns, String tableName) {
        String column = String.join(",", columns);
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s (%S)", tableName, column);
        jdbcTemplate.update(sql);
    }
}
