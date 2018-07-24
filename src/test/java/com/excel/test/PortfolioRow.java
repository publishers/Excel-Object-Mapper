package com.excel.test;

import com.excel.mapper.annotation.Sheet;
import com.excel.mapper.annotation.field.Header;
import com.excel.mapper.annotation.field.NextSheet;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Sheet(sheetName = "Portfolio")
public class PortfolioRow {
    @Header(name = "Name", position = 0)
    private String name;
    @Header(name = "Currency", position = 1)
    private String currency;
    @Header(name = "Start Date", position = 2)
    private LocalDateTime startDate;
    @Header(name = "Starting Balance", position = 3)
    private BigDecimal startingBalance;
    @Header(name = "Investment Period", position = 4)
    private BigDecimal investmentPeriod;
    @Header(name = "Investment Goal", position = 5)
    private BigDecimal investmentGoal;
    @Header(name = "Monthly Deposit", position = 6)
    private BigDecimal monthlyDeposit;
    @NextSheet(clazz = String.class, join = "portfolioName", select = "name")
    private List<AssetRow> assetRows;

    public String getName() {
        return name;
    }

    public String getCurrency() {
        return currency;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public BigDecimal getStartingBalance() {
        return startingBalance;
    }

    public BigDecimal getInvestmentPeriod() {
        return investmentPeriod;
    }

    public BigDecimal getInvestmentGoal() {
        return investmentGoal;
    }

    public BigDecimal getMonthlyDeposit() {
        return monthlyDeposit;
    }

    public List<AssetRow> getAssetRows() {
        return assetRows;
    }
}