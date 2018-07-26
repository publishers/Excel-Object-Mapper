package com.excel.framework;

import com.excel.framework.impl.AssetFileUploader;
import com.excel.framework.impl.PortfolioFileUploader;
import com.excel.framework.impl.TestSheetFileUploader;
import com.excel.test.AssetRow;
import com.excel.test.PortfolioRow;
import com.excel.test.TestSheet;
import org.junit.Test;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class AbstractFileUploaderTest {
    public static final String FILE_TEST_NAME = "portfolio-upload.xlsx";

    @Test
    public void test() throws Exception {
        TestSheetFileUploader sheetFileUploader = new TestSheetFileUploader();
        AssetFileUploader assetFileUploader = new AssetFileUploader(sheetFileUploader);
        PortfolioFileUploader portfolioFileUploader = new PortfolioFileUploader(assetFileUploader);

        URL url = getClass().getClassLoader().getResource(FILE_TEST_NAME);

        List<PortfolioRow> result = portfolioFileUploader.extractData(url.openStream());

        PortfolioRow portfolioRow = result.get(1);
        assertThat(portfolioRow.getName(), is("portfolio 12"));
        assertThat(portfolioRow.getCurrency(), is("EUR"));
        assertThat(portfolioRow.getStartDate(), is(LocalDateTime.of(2018,04, 28, 0, 0)));
        assertThat(portfolioRow.getStartingBalance(), is(BigDecimal.valueOf(600_000L)));
        assertThat(portfolioRow.getInvestmentPeriod(), is(BigDecimal.valueOf(3L)));
        assertThat(portfolioRow.getInvestmentGoal(), is(BigDecimal.valueOf(100_000L)));
        assertThat(portfolioRow.getMonthlyDeposit(), is(BigDecimal.valueOf(8_000L)));

        portfolioRow.getAssetRows().forEach(assetRow -> {
            assertThat(assetRow.getPortfolioName(), is(portfolioRow.getName()));

            TestSheet testSheet = assetRow.getTestSheet().get(0);
            assertThat(testSheet.getPortfolioName(), is(portfolioRow.getName()));
        });




    }
}