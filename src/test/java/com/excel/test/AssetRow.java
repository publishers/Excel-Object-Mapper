package com.excel.test;

import com.excel.mapper.annotation.Sheet;
import com.excel.mapper.annotation.field.Header;

import java.math.BigDecimal;

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
}
