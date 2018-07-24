package com.excel.data.extractor;

import org.apache.poi.xssf.usermodel.XSSFRow;

public interface ExcelTypeHelper {
    /**
     * Extracts exactly from row
     *
     * @param tClass - returned type
     * @param row  - current row in com.excel
     * @param columnNumber
     * @return type from parametrized class
     */
    <T> T extract(Class<T> tClass, XSSFRow row, Integer columnNumber);
}
