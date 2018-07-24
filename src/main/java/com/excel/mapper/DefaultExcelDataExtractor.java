package com.excel.mapper;

import com.excel.mapper.exception.UnsupportedDataExtractorException;
import com.excel.mapper.exception.ValidationException;
import com.excel.utils.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class DefaultExcelDataExtractor implements ExcelTypeHelper {
    private final Map<Class, BiFunction<XSSFRow, Integer, ?>> SUPPORTED_DATA_MAP = new HashMap<>();

    {
        SUPPORTED_DATA_MAP.put(String.class, string());
        SUPPORTED_DATA_MAP.put(Integer.class, integer());
        SUPPORTED_DATA_MAP.put(Double.class, doubleD());
        SUPPORTED_DATA_MAP.put(BigDecimal.class, bigDecimal());
        SUPPORTED_DATA_MAP.put(BigInteger.class, bigInteger());
        SUPPORTED_DATA_MAP.put(LocalDateTime.class, localDateTime());
    }

    public <T> T extract(Class<T> tClass, XSSFRow row, Integer columnNumber) {
        BiFunction function = SUPPORTED_DATA_MAP.get(tClass);
        if (function == null) {
            throw new UnsupportedDataExtractorException("DefaultExcelDataExtractor doesn't support this field format :" + tClass.getTypeName());
        }
        BiFunction<XSSFRow, Integer, T> executor = (BiFunction<XSSFRow, Integer, T>) function;
        return executor.apply(row, columnNumber);
    }

    private BiFunction<XSSFRow, Integer, Double> doubleD() {
        return (row, cellNumber) -> {
            String rawValue = getRawValue(row, cellNumber);
            return new Double(rawValue);
        };
    }

    private BiFunction<XSSFRow, Integer, Integer> integer() {
        return (row, cellNumber) -> {
            String rawValue = getRawValue(row, cellNumber);
            return new Integer(rawValue);
        };
    }

    private BiFunction<XSSFRow, Integer, String> string() {
        return (row, cellNumber) -> {
            XSSFCell cell = getCell(row, cellNumber);
            return cell.getStringCellValue();
        };
    }

    private BiFunction<XSSFRow, Integer, BigDecimal> bigDecimal() {
        return (row, cellNumber) -> {
            String rawValue = getRawValue(row, cellNumber);
            return new BigDecimal(rawValue);
        };
    }

    private BiFunction<XSSFRow, Integer, BigInteger> bigInteger() {
        return (row, cellNumber) -> {
            String rawValue = getRawValue(row, cellNumber);
            return new BigInteger(rawValue);
        };
    }

    private BiFunction<XSSFRow, Integer, LocalDateTime> localDateTime() {
        return (row, cellNumber) -> {
            XSSFCell cell = getCell(row, cellNumber);
            Date dateValue;
            try {
                dateValue = cell.getDateCellValue();
            } catch (IllegalStateException ex) {
                throw new ValidationException(ex, "Invalid excel file. Cannot be transform to Date. Cell: " + cellNumber
                        + " raw: " + row.getRowNum());
            }
            Instant instant = dateValue.toInstant();
            ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
            return zonedDateTime.toLocalDateTime();
        };
    }

    private XSSFCell getCell(XSSFRow row, int cellNumber) {
        XSSFCell cell = row.getCell(cellNumber);
        if (cell == null) {
            throw new ValidationException("Invalid excel file. Cell is null. Cell: " + cellNumber
                    + " raw: " + row.getRowNum());
        }
        return validateCellValue(cell);
    }

    private XSSFCell validateCellValue(XSSFCell cell) {
        if (StringUtils.isEmpty(cell.getRawValue())) {
            throw new ValidationException("Invalid excel file. Cell value cannot be null. Cell: " + cell.getColumnIndex()
                    + " raw: " + cell.getRowIndex());
        }
        return cell;
    }

    private String getRawValue(XSSFRow row, int cellNumber) {
        XSSFCell cell = getCell(row, cellNumber);
        return cell.getRawValue();
    }
}
