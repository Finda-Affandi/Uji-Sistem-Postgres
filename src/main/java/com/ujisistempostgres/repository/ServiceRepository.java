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

    public List<Map<String, Object>> getAllData(String tableName) {
        try {
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
        } catch (DataAccessException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<List<Map<String, Object>>> getBothData(List<String> tableNames) {
        try {
            List<List<Map<String, Object>>> result = new ArrayList<>();

            for (String tableName : tableNames) {
                String sql = "SELECT * FROM " + tableName;

                List<Map<String, Object>> dataList = jdbcTemplate.query(sql, (resultSet, rowNum) -> {
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

                result.add(dataList);
            }

            return result;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }





//    public void insertData(Map<String, Object> dataMap) {
//        String tableName = "saleslineframe";
//        String columns = String.join(", ", dataMap.keySet());
//        String placeholders = String.join(", ", Collections.nCopies(dataMap.size(), "?"));
//
//        String sql = String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, columns, placeholders);
//        jdbcTemplate.update(sql, dataMap.values().toArray());
//    }

//    public void insertData(List<Map<String, Object>> dataList, String tableName) {
//        try {
//            if (dataList.isEmpty()) {
//                System.out.println("Data list is empty.");
//                return;
//            }
//
//            Map<String, Object> firstRow = dataList.get(0);
//            String columns = String.join(", ", firstRow.keySet());
//            String placeholders = String.join(", ", Collections.nCopies(firstRow.size(), "?"));
//
//            List<Object[]> batchParams = new ArrayList<>();
//            for (Map<String, Object> dataMap : dataList) {
//                Object[] values = dataMap.values().toArray();
//                batchParams.add(values);
//            }
//
//            String sql = String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, columns, placeholders);
//            jdbcTemplate.batchUpdate(sql, batchParams);
//        } catch (DataAccessException e) {
//            System.out.println("Error executing SQL statement: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }

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
            for (Object obj : data.values()) {
                value.add("'" + obj.toString() + "'");
            }
            String joinValue = String.join(",", value);
            String wrapValue = "(" + joinValue +")";

            allValue.add(wrapValue);
        }
        System.out.println("cekkk");

        String joinAllValue = String.join(",", allValue);

        String sql = template + " VALUES " + joinAllValue;

        jdbcTemplate.update(sql);
    }

    public List<String> getAllTableNames() {
        String query = "SELECT tablename FROM pg_catalog.pg_tables WHERE schemaname = 'public';";
        return jdbcTemplate.queryForList(query, String.class);
    }

    public List<String> getAllTableNamesForChoose() {
        String query = "SELECT tablename FROM pg_catalog.pg_tables WHERE schemaname = 'public';";
        List<Map<String, Object>> results = jdbcTemplate.queryForList(query);

        List<String> tableNames = new ArrayList<>();
        int count = 1;
        for (Map<String, Object> row : results) {
            String tableName = (String) row.get("tablename");
            tableNames.add(count + ". " + tableName);
            count++;
        }

        return tableNames;
    }

    public String[] getAllTableNamesForSelectByTable() {
        String query = "SELECT tablename FROM pg_catalog.pg_tables WHERE schemaname = 'public';";
        List<String> tableNameList = jdbcTemplate.queryForList(query, String.class);

        String[] tableNameArray = new String[tableNameList.size()];
        tableNameArray = tableNameList.toArray(tableNameArray);

        return tableNameArray;
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
        System.out.println(sql);
        jdbcTemplate.update(sql);
    }

    public void createTableWithMap(List<String> columns, String tableName) {
        String column = String.join(",", columns);
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s (%S)", tableName, column);
        System.out.println("Success create table");
        jdbcTemplate.update(sql);
    }

    public void mencoba(List<Map<String, Object>> dataList) {
        String insert = "INSERT INTO";
        String table = "tb_cb";




        List<String> column = new ArrayList<>();
        for (Map<String, Object> data : dataList) {
            column.addAll(data.keySet());
            break;
        }
        String colName = String.join(",", column);
        String template = insert + " " + table + " " + "(" + colName +")";

        List<String> allValue = new ArrayList<>();
        List<String> value = new ArrayList<>();
        for (Map<String, Object> data : dataList) {
            value.clear();
            for (Object obj : data.values()) {
                value.add("'" + obj.toString() + "'");
            }
            String joinValue = String.join(",", value);
            String wrapValue = "(" + joinValue +")";

            allValue.add(wrapValue);
        }

        String joinAllValue = String.join(",", allValue);

        String sql = template + " VALUES " + joinAllValue;

        System.out.println(sql);



//        System.out.println(sql);
    }
}
