package com.excel.test;

import com.excel.mapper.annotation.Sheet;
import com.excel.mapper.annotation.field.Header;

@Sheet(sheetName = "SecondSheetObject")
public class SecondSheetObject {
    @Header(name = "FIELD1", position = 0)
    private String fieldC;

    @Header(name = "FIELD2", position = 1)
    private String fieldD;

    public String getFieldC() {
        return fieldC;
    }

    public String getFieldD() {
        return fieldD;
    }
}
