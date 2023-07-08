package com.ujisistempostgres.repository;

import com.ujisistempostgres.converter.DateConverter;
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

    public void insertData(List<Map<String,Object>> dataList, Map<String, Object> dataType, String tableName) {
        List<String> column = new ArrayList<>();
        DateConverter dateConverter = new DateConverter();

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
            for (Object type : dataType.keySet()) {
                if (Objects.equals(dataType.get(type).toString(), "int")) {
                    value.add(data.get(type).toString());
                } else if (Objects.equals(dataType.get(type).toString(), "varchar")) {
                    value.add("'" + data.get(type).toString() + "'");
                } else if (Objects.equals(dataType.get(type).toString(), "date")) {
                    value.add("'" + dateConverter.cassandraDate(data.get(type).toString()) + "'");
                } else if (Objects.equals(dataType.get(type).toString(), "float")) {
                    value.add(data.get(type).toString());
                }
            }

            String joinValue = String.join(",", value);
            String wrapValue = "(" + joinValue +")";

            String sql = template + " VALUES " + wrapValue;
            jdbcTemplate.update(sql);
        }
    }

    public List<String> getAllTableNames() {
        String query = "SELECT tablename FROM pg_catalog.pg_tables WHERE schemaname = 'public';";
        return jdbcTemplate.queryForList(query, String.class);
    }


    public void createTable(Map<String, Object> columnList, String tableName) {
        List<String> column = new ArrayList<>();
        List<String> columnAndType = new ArrayList<>();
        column.addAll(columnList.keySet());
        for (String col : column) {
            if (col != "PRIMARY KEY") {
                columnAndType.add(col + " " + columnList.get(col));
            }
        }

        columnAndType.add("PRIMARY KEY" + " " + columnList.get("PRIMARY KEY"));

        String cols = String.join(",", columnAndType);

        String sql = String.format("CREATE TABLE IF NOT EXISTS %s (%S)", tableName, cols);
        jdbcTemplate.update(sql);
    }

    public void truncateTable( String tableName ) {
        String query = "TRUNCATE TABLE " + tableName + ";";
        jdbcTemplate.update(query);
    }
    
}
