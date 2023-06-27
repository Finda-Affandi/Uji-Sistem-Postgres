package com.ujisistempostgres.datamapping;

import java.util.List;
import java.util.Map;

public class DataMappingJson {
    public static List<Map<String, Object>> transformData(List<Map<String, Object>> dataList) {
        for (Map<String, Object> data : dataList) {
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                if (entry.getValue() instanceof String) {
                    String fieldName = entry.getKey();
                    String value = (String) entry.getValue();
                    try {
                        // Ubah tipe data berdasarkan nama field
                        if (fieldName.equals("LINEID") ||
                                fieldName.equals("ORDER_LINE_NUMBER") ||
                                fieldName.equals("ORDER_FRAME_EYE_SIZE") ||
                                fieldName.equals("ORDER_FRAME_DBL") ||
                                fieldName.equals("ORDER_REMARKS") ||
                                fieldName.equals("ORDQTY") ||
                                fieldName.equals("STOCK_FRAME_EYE_SIZE") ||
                                fieldName.equals("STOCK_FRAME_DBL") ||
                                fieldName.equals("PRICE_FRAME_EYE_SIZE") ||
                                fieldName.equals("PRICE_FRAME_DBL") ||
                                fieldName.equals("PRICE_UNIT_PRICE")) {
                            int intValue = Integer.parseInt(value);
                            data.put(fieldName, intValue);
                        } else if (fieldName.equals("PRICE_DISCOUNT_RATE") ||
                                fieldName.equals("PRICE_INPUT_FLAG")) {
                            float floatValue = Float.parseFloat(value);
                            data.put(fieldName, floatValue);
                        } else {
                            data.put(fieldName, value);
                        }
                    } catch (NumberFormatException e) {
                    }
                }
            }
        }
        return dataList;
    }




}
