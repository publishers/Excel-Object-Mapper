package com.excel.test;

import com.excel.mapper.annotation.Sheet;
import com.excel.mapper.annotation.field.Header;
import com.excel.mapper.annotation.field.NextSheet;

import java.math.BigDecimal;
import java.util.List;

@Sheet(sheetName = "Asset")
public class AssetRow {
    @Header(name = "Name", position = 0)
    private String portfolioName;
    @Header(name = "Asset", position = 1)
    private String assetSymbol;
    @Header(name = "Quantity", position = 2)
    private BigDecimal quantity;
    @Header(name = "Average Purchase Price", position = 3)
    private BigDecimal averagePurchasePrice;
    @NextSheet(clazz = String.class, join = "portfolioName", select = "portfolioName")
    private List<TestSheet> testSheet;

    public String getPortfolioName() {
        return portfolioName;
    }

    public String getAssetSymbol() {
        return assetSymbol;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public BigDecimal getAveragePurchasePrice() {
        return averagePurchasePrice;
    }

    public void setPortfolioName(String portfolioName) {
        this.portfolioName = portfolioName;
    }

    public void setAssetSymbol(String assetSymbol) {
        this.assetSymbol = assetSymbol;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public void setAveragePurchasePrice(BigDecimal averagePurchasePrice) {
        this.averagePurchasePrice = averagePurchasePrice;
    }

    public List<TestSheet> getTestSheet() {
        return testSheet;
    }

    public void setTestSheet(List<TestSheet> testSheet) {
        this.testSheet = testSheet;
    }
}
