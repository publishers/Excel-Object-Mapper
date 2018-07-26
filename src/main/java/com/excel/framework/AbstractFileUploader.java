package com.excel.framework;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractFileUploader<T, D> implements ExcelFileUploader<T> {

    private ExcelFileUploader<D> fileUploader;

    public AbstractFileUploader() {
        this.fileUploader = null;
    }

    public AbstractFileUploader(ExcelFileUploader<D> fileUploader) {
        this.fileUploader = fileUploader;
    }

    @Override
    public List<T> extractData(InputStream inputStream) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        return extractData(workbook);
    }

    protected List<T> mergeDataSheets(List<T> currentDataSheet, List<D> sheetData) {
        throw new UnsupportedOperationException("Merge sheets by default is not supported");
    }

    @Override
    public List<T> extractData(XSSFWorkbook workbook) {
        XSSFSheet currentSheet = workbook.getSheetAt(getSheetPage());
        List<T> dataList = new ArrayList<>();
        Iterator<Row> portfolioSheetRowIterator = currentSheet.rowIterator();
        if(portfolioSheetRowIterator.hasNext()) {
            if (isValidHeaders((XSSFRow) portfolioSheetRowIterator.next())) {
                while (portfolioSheetRowIterator.hasNext()) {
                    XSSFRow row = (XSSFRow) portfolioSheetRowIterator.next();
                    T excelObject = extractRow(row);
                    dataList.add(excelObject);
                }
            }
        }
        return extractNextSheet(dataList, workbook);
    }

    private List<T> extractNextSheet(List<T> dataList, XSSFWorkbook workbook) {
        if(fileUploader != null) {
            List<D> nextDataSheet = fileUploader.extractData(workbook);
            dataList = mergeDataSheets(dataList, nextDataSheet);
        }
        return dataList;
    }

    protected abstract int getSheetPage();

    protected abstract T extractRow(XSSFRow row);

    protected abstract boolean isValidHeaders(XSSFRow headerRow);
}

