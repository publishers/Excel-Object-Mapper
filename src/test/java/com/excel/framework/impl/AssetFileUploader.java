package com.excel.framework.impl;

import com.excel.data.extractor.DefaultExcelDataExtractor;
import com.excel.framework.AbstractFileUploader;
import com.excel.test.AssetRow;
import com.excel.test.TestSheet;
import org.apache.poi.xssf.usermodel.XSSFRow;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class AssetFileUploader extends AbstractFileUploader<AssetRow, TestSheet> {
    private static final int ASSET_INDEX_SHEET = 1;
    private DefaultExcelDataExtractor dataExtractor = new DefaultExcelDataExtractor();

    public AssetFileUploader(TestSheetFileUploader testSheetFileUploader) {
        super(testSheetFileUploader);
    }

    @Override
    public int getSheetPage() {
        return ASSET_INDEX_SHEET;
    }

    @Override
    protected AssetRow extractRow(XSSFRow row) {
        String portfolioName = dataExtractor.extract(String.class, row, 0);
        String assetSymbol = dataExtractor.extract(String.class, row, 1);
        BigDecimal quantity = dataExtractor.extract(BigDecimal.class, row, 2);
        BigDecimal averagePurchasePrice = dataExtractor.extract(BigDecimal.class, row, 3);

        return builder()
                .portfolioName(portfolioName)
                .assetSymbol(assetSymbol)
                .quantity(quantity)
                .averagePurchasePrice(averagePurchasePrice)
                .build();
    }

    @Override
    protected List<AssetRow> mergeDataSheets(List<AssetRow> currentDataSheet, List<TestSheet> sheetData) {
        return currentDataSheet.stream()
                .map(assetRow -> {
                    List<TestSheet> testSheet = sheetData.stream()
                            .filter(sheet -> sheet.getPortfolioName().equals(assetRow.getPortfolioName()))
                            .collect(Collectors.toList());
                    assetRow.setTestSheet(testSheet);
                    return assetRow;
                })
                .collect(Collectors.toList());
    }

    private AssetRowBuilder builder() {
        return new AssetRowBuilder();
    }

    @Override
    public boolean isValidHeaders(XSSFRow headerRow) {
        return true;
    }

    private class AssetRowBuilder {
        private AssetRow assetRow = new AssetRow();

        public AssetRowBuilder portfolioName(String portfolioName) {
            assetRow.setPortfolioName(portfolioName);
            return this;
        }

        public AssetRowBuilder assetSymbol(String assetSymbol) {
            assetRow.setAssetSymbol(assetSymbol);
            return this;
        }

        public AssetRowBuilder quantity(BigDecimal quantity) {
            assetRow.setQuantity(quantity);
            return this;
        }

        public AssetRowBuilder averagePurchasePrice(BigDecimal averagePurchasePrice) {
            assetRow.setAveragePurchasePrice(averagePurchasePrice);
            return this;
        }

        public AssetRow build() {
            return assetRow;
        }
    }
}
