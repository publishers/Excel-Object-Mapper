package com.excel.test;

import com.excel.mapper.annotation.Sheet;
import com.excel.mapper.annotation.field.Header;

@Sheet(sheetName = "TestSheet")
public class TestSheet {
    @Header(name = "Name", position = 0)
    private String portfolioName;
    @Header(name = "Test", position = 1)
    private String testV;

    public String getPortfolioName() {
        return portfolioName;
    }

    public void setPortfolioName(String portfolioName) {
        this.portfolioName = portfolioName;
    }

    public String getTestV() {
        return testV;
    }

    public void setTestV(String testV) {
        this.testV = testV;
    }
}
