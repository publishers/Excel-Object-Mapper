package com.excel.framework;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

public interface ExcelFileUploader<T> extends FileUploader<List<T>> {
    List<T> extractData(XSSFWorkbook workbook);
}
