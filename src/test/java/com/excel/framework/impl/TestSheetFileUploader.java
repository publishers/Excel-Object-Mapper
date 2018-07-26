package com.excel.framework.impl;

import com.excel.data.extractor.DefaultExcelDataExtractor;
import com.excel.framework.AbstractFileUploader;
import com.excel.test.TestSheet;
import org.apache.poi.xssf.usermodel.XSSFRow;

public class TestSheetFileUploader extends AbstractFileUploader<TestSheet, Object> {

    private DefaultExcelDataExtractor dataExtractor = new DefaultExcelDataExtractor();

    @Override
    public int getSheetPage() {
        return 2;
    }

    @Override
    protected TestSheet extractRow(XSSFRow row) {
        String name = dataExtractor.extract(String.class, row, 0);
        String currency = dataExtractor.extract(String.class, row, 1);

        return builder()
                .name(name)
                .currency(currency)
                .build();
    }

    @Override
    protected boolean isValidHeaders(XSSFRow headerRow) {
        return true;
    }

    private TestSheetBuilder builder() {
        return new TestSheetBuilder();
    }

    private class TestSheetBuilder {
        private TestSheet testSheet = new TestSheet();

        public TestSheetBuilder name(String name) {
            testSheet.setPortfolioName(name);
            return this;
        }

        public TestSheetBuilder currency(String currency) {
            testSheet.setTestV(currency);
            return this;
        }

        public TestSheet build() {
            return testSheet;
        }
    }
}
