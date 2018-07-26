package com.excel.framework.impl;

import com.excel.data.extractor.DefaultExcelDataExtractor;
import com.excel.framework.AbstractFileUploader;
import com.excel.test.AssetRow;
import com.excel.test.PortfolioRow;
import org.apache.poi.xssf.usermodel.XSSFRow;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PortfolioFileUploader extends AbstractFileUploader<PortfolioRow, AssetRow> {
    private static final int ASSET_INDEX_SHEET = 0;
    private DefaultExcelDataExtractor dataExtractor = new DefaultExcelDataExtractor();

    public PortfolioFileUploader(AssetFileUploader fileUploader) {
        super(fileUploader);
    }

    @Override
    protected List<PortfolioRow> mergeDataSheets(List<PortfolioRow> portfolioRows, List<AssetRow> assetRows) {
        return portfolioRows.stream()
                .peek(portfolioRow -> assetRows.forEach(assetRow -> {
                    if (portfolioRow.getName().equals(assetRow.getPortfolioName())) {
                        portfolioRow.getAssetRows().add(assetRow);
                    }
                }))
                .collect(Collectors.toList());
    }

    @Override
    public int getSheetPage() {
        return ASSET_INDEX_SHEET;
    }

    @Override
    protected PortfolioRow extractRow(XSSFRow row) {
        String name = dataExtractor.extract(String.class, row, 0);
        String currency = dataExtractor.extract(String.class, row, 1);
        LocalDateTime startDate = dataExtractor.extract(LocalDateTime.class, row, 2);
        BigDecimal startingBalance = dataExtractor.extract(BigDecimal.class, row, 3);
        BigDecimal investmentPeriod = dataExtractor.extract(BigDecimal.class, row, 4);
        BigDecimal investmentGoal = dataExtractor.extract(BigDecimal.class, row, 5);
        BigDecimal monthlyDeposit = dataExtractor.extract(BigDecimal.class, row, 6);

        return builder()
                .name(name)
                .currency(currency)
                .startDate(startDate)
                .startingBalance(startingBalance)
                .investmentPeriod(investmentPeriod)
                .investmentGoal(investmentGoal)
                .monthlyDeposit(monthlyDeposit)
                .assetRows(new ArrayList<>())
                .build();
    }

    private BuilderPortfolioRow builder() {
        return new BuilderPortfolioRow();
    }

    @Override
    public boolean isValidHeaders(XSSFRow headerRow) {
        return true;
    }

    private class BuilderPortfolioRow {
        private PortfolioRow portfolioRow = new PortfolioRow();

        public BuilderPortfolioRow name(String name) {
            portfolioRow.setName(name);
            return this;
        }

        public BuilderPortfolioRow currency(String currency) {
            portfolioRow.setCurrency(currency);
            return this;
        }

        public BuilderPortfolioRow startDate(LocalDateTime startDate) {
            portfolioRow.setStartDate(startDate);
            return this;
        }

        public BuilderPortfolioRow startingBalance(BigDecimal startingBalance) {
            portfolioRow.setStartingBalance(startingBalance);
            return this;
        }

        public BuilderPortfolioRow investmentPeriod(BigDecimal investmentPeriod) {
            portfolioRow.setInvestmentPeriod(investmentPeriod);
            return this;
        }

        public BuilderPortfolioRow investmentGoal(BigDecimal investmentGoal) {
            portfolioRow.setInvestmentGoal(investmentGoal);
            return this;
        }

        public BuilderPortfolioRow assetRows(List<AssetRow> assetRows) {
            portfolioRow.setAssetRows(assetRows);
            return this;
        }

        public BuilderPortfolioRow monthlyDeposit(BigDecimal monthlyDeposit) {
            portfolioRow.setMonthlyDeposit(monthlyDeposit);
            return this;
        }

        public PortfolioRow build() {
            return portfolioRow;
        }
    }
}
