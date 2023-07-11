package com.ujisistempostgres.repository;

import org.springframework.dao.DataAccessException;
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

    public List<Map<String, Object>> getBothData(String tableName) {
        try {
            String sql = "SELECT * FROM " + tableName;
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
        } catch (DataAccessException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public void insertData(List<Map<String,Object>> dataList, String tableName) {
        List<String> column = new ArrayList<>();

        for (Map<String, Object> data : dataList) {
            column.addAll(data.keySet());
            break;
        }

        String colName = String.join(",", column);
        String template = "INSERT INTO" + " " + tableName + " " + "(" + colName +")";

        List<String> allValue = new ArrayList<>();
        List<String> value = new ArrayList<>();
        for (Map<String, Object> data : dataList) {
            value.clear();
            for (String col : data.keySet()) {
                value.add("'" + data.get(col).toString() + "'");
            }

            String joinValue = String.join(",", value);
            String wrapValue = "(" + joinValue +")";

            String sql = template + " VALUES " + wrapValue;
            System.out.println(sql);
            jdbcTemplate.update(sql);
        }
    }

    public void updateData (String nim, Map<String, Object> newData, String table) {
        List<String> column = new ArrayList<>();
        List<String> columnQuery = new ArrayList<>();
        column.addAll(newData.keySet());

        String colName = String.join(",", column);
        String template = "UPDATE" + " " + table + " " + "SET ";

        for (String col : column) {
            columnQuery.add(col + " = " + "'" + newData.get(col) + "'");
        }

        String joinValue = String.join(",", columnQuery);

        String sql = template + joinValue + " WHERE nim = " + "'" + nim + "'";

        jdbcTemplate.update(sql);
    }

    public void deleteData (String nim, String table) {
        String sql = "DELETE FROM " + table + " WHERE nim = " + "'" + nim + "'";
        jdbcTemplate.update(sql);
    }
    
}
