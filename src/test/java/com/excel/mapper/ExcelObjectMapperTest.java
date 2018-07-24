package com.excel.mapper;

import com.excel.test.FirstSheetObject;
import com.excel.test.PortfolioRow;
import com.excel.test.SecondSheetObject;
import org.junit.Test;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ExcelObjectMapperTest {
    public static final String FILE_TEST_NAME = "testMapperObjects.xlsx";

    @Test
    public void testObjectMapper() throws Exception {
        URL url = getClass().getClassLoader().getResource(FILE_TEST_NAME);
        List<FirstSheetObject> result =
                new ExcelObjectMapper<>(FirstSheetObject.class).extractData(url.openStream());
        FirstSheetObject firstSheetObject = result.get(0);
        assertThat(firstSheetObject.getFieldA(), is("test value 1"));
        assertThat(firstSheetObject.getFieldB(), is("test2.1"));
        SecondSheetObject secondSheetObject = firstSheetObject.getSecondSheetObject().get(0);
        assertThat(secondSheetObject.getFieldC(), is("test value 1"));
        assertThat(secondSheetObject.getFieldD(), is("test value 2"));
    }

    @Test
    public void testObjectMapper2() throws Exception {
        URL url = getClass().getClassLoader().getResource("portfolio-upload.xlsx");
        List<PortfolioRow> portfolioRows =
                new ExcelObjectMapper<>(PortfolioRow.class).extractData(url.openStream());
        PortfolioRow portfolioRow = portfolioRows.get(1);
        assertThat(portfolioRow.getName(), is("portfolio 12"));
        assertThat(portfolioRow.getCurrency(), is("EUR"));
        assertThat(portfolioRow.getStartDate(), is(LocalDateTime.of(2018,04, 28, 0, 0)));
        assertThat(portfolioRow.getStartingBalance(), is(BigDecimal.valueOf(600_000L)));
        assertThat(portfolioRow.getInvestmentPeriod(), is(BigDecimal.valueOf(3L)));
        assertThat(portfolioRow.getInvestmentGoal(), is(BigDecimal.valueOf(100_000L)));
        assertThat(portfolioRow.getMonthlyDeposit(), is(BigDecimal.valueOf(8_000L)));
    }
}
