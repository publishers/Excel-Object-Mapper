package com.excel.test;

import com.excel.mapper.annotation.Sheet;
import com.excel.mapper.annotation.field.Header;
import com.excel.mapper.annotation.field.NextSheet;

import java.util.List;

@Sheet(sheetName = "FirstSheetObject")
public class FirstSheetObject {
    @Header(name = "FIELD1", position = 0)
    private String fieldA;

    @Header(name = "FIELD2", position = 1, required = false)
    private String fieldB;

    @NextSheet(select = "fieldA", join = "fieldC", clazz = String.class)
    private List<SecondSheetObject> secondSheetObject;

    public String getFieldA() {
        return fieldA;
    }

    public String getFieldB() {
        return fieldB;
    }

    public List<SecondSheetObject> getSecondSheetObject() {
        return secondSheetObject;
    }
}
