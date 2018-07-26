package com.excel.mapper;

import com.excel.data.extractor.DefaultExcelDataExtractor;
import com.excel.data.extractor.ExcelTypeHelper;
import com.excel.framework.FileUploader;
import com.excel.mapper.annotation.Sheet;
import com.excel.mapper.annotation.field.Header;
import com.excel.mapper.annotation.field.NextSheet;
import com.excel.mapper.exception.ValidationException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExcelObjectMapper<T> implements FileUploader<List<T>> {
    private Map<Class, FieldObjectDataContainer> sheetsMap;

    private Class<T> currentClass;

    private ExcelTypeHelper typeHelper;

    private XSSFWorkbook workbook;

    private ExcelObjectMapper() {
        this.sheetsMap = new HashMap<>();
    }

    public ExcelObjectMapper(Class<T> obj) {
        this();
        this.currentClass = obj;
        this.typeHelper = new DefaultExcelDataExtractor();
    }

    public ExcelObjectMapper(Class<T> obj, ExcelTypeHelper typeHelper) {
        this();
        this.currentClass = obj;
        this.typeHelper = typeHelper;
    }

    @Override
    public List<T> extractData(InputStream inputStream) throws Exception {
        workbook = new XSSFWorkbook(inputStream);
        return extractSheet(currentClass);
    }

    private <E> List<E> extractSheet(Class<E> eClass) throws Exception {
        for (Sheet ann : eClass.getDeclaredAnnotationsByType(Sheet.class)) {
            return startExtractingData(ann, eClass);
        }
        return Collections.emptyList();
    }

    private <E> List<E> startExtractingData(Sheet sheet, Class<E> clazz) throws InstantiationException, IllegalAccessException {
        String sheetName = sheet.sheetName();
        XSSFSheet xssfSheet = workbook.getSheet(sheetName);
        Field[] fields = clazz.getDeclaredFields();
        Iterator<Row> iterator = xssfSheet.rowIterator();
        List<E> result = new ArrayList<>();
        if (iterator.hasNext()) {
            if (isValidHeaders(fields, (XSSFRow) iterator.next())) {
                while (iterator.hasNext()) {
                    E obj = clazz.newInstance();
                    XSSFRow row = (XSSFRow) iterator.next();
                    fillObjectFieldsNextSheet(obj, fields, row);
                    result.add(obj);
                }
                endedInitialization(result);
            }
        }
        return result;
    }

    private boolean isValidHeaders(Field[] fields, XSSFRow row) {
        for (Field field : fields) {
            boolean result =
                    handlerField(field, annotations -> {
                        if (annotations == null || annotations.length == 0) return true;
                        return checkHeader(annotations, row);
                    });
            if (!result) {
                throw new ValidationException("Uploaded file is wrong check headers!");
            }
        }
        return true;
    }

    private boolean checkHeader(Annotation[] findHeaderAnnotation, XSSFRow row) {
        Header annotation = findAnnotation(findHeaderAnnotation, Header.class);
        if (annotation == null) return true;
        if (annotation.required()) {
            String headerValue = typeHelper.extract(String.class, row, annotation.position());
            return annotation.name().matches(headerValue);
        }
        return true;
    }

    private <E> void fillObjectFieldsNextSheet(E obj, Field[] fields, XSSFRow row) {
        for (Field field : fields) {
            field.setAccessible(true);
            Annotation[] annotations = field.getDeclaredAnnotations();
            Header header = findAnnotation(annotations, Header.class);
            headerHandler(header, field, row, obj);

            NextSheet nextSheet = findAnnotation(annotations, NextSheet.class);
            nextSheetHandler(nextSheet, field);
        }
    }

    private <E> void headerHandler(Header header, Field field, XSSFRow row, E obj) {
        if (header != null) {
            Object value = typeHelper.extract(field.getType(), row, header.position());
            try {
                field.set(obj, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void nextSheetHandler(NextSheet nextSheet, Field field) {
        if (nextSheet != null) {
            try {
                Class fieldType = extractFieldType(field);
                if (!sheetsMap.containsKey(fieldType)) {
                    List<?> newSheetResult = extractSheet(fieldType);
                    if (!newSheetResult.isEmpty()) {
                        sheetsMap.put(fieldType, FieldObjectDataContainer.builder()
                                .field(field)
                                .fieldParams((List<Object>) newSheetResult)
                                .build());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private <E> void endedInitialization(List<E> objects) {
        objects.forEach(obj -> {
            Class clazz = obj.getClass();
            Field[] fields = clazz.getDeclaredFields();
            Arrays.stream(fields).forEach(field -> {
                field.setAccessible(true);
                fillObjectFieldsNextSheet(field, obj);
            });
        });
    }

    private <E> void fillObjectFieldsNextSheet(Field field, E obj) {
        Class clazz = obj.getClass();
        try {
            Annotation[] annotations = field.getDeclaredAnnotations();
            NextSheet nextSheet = findAnnotation(annotations, NextSheet.class);
            if (nextSheet != null) {
                Field selectField = clazz.getDeclaredField(nextSheet.select());
                if (selectField != null) {
                    selectField.setAccessible(true);
                    Class fieldType = extractFieldType(field);

                    if (sheetsMap.containsKey(fieldType)) {
                        FieldObjectDataContainer fieldObjectDataContainer = sheetsMap.get(fieldType);

                        Field fieldClazz = clazz.getDeclaredField(fieldObjectDataContainer.getField().getName());
                        fieldClazz.setAccessible(true);

                        List<?> values = fieldObjectDataContainer.getFieldParams();
                        Object selectedFieldValue = selectField.get(obj);
                        List<?> result = filterValuesByFieldCondition(values, nextSheet, selectedFieldValue);
                        fieldClazz.set(obj, result);
                    }
                }
            }
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private List<?> filterValuesByFieldCondition(List<?> values, NextSheet nextSheet, Object selectedFieldValue) {
        Class mergedType = nextSheet.clazz();
        return values.stream().filter(o -> {
            Class mergedSheetClass = o.getClass();
            try {
                Field mergedFieldClazz = mergedSheetClass.getDeclaredField(nextSheet.join());
                mergedFieldClazz.setAccessible(true);
                Object mergedFieldValue = mergedFieldClazz.get(o);
                return mergedType.isAssignableFrom(selectedFieldValue.getClass())
                        && mergedType.isAssignableFrom(mergedFieldValue.getClass())
                        && selectedFieldValue.equals(mergedFieldValue);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return false;
        }).collect(Collectors.toList());
    }

    private Class extractFieldType(Field field) {
        if (List.class.getTypeName().equals(field.getType().getTypeName())) {
            ParameterizedType type = (ParameterizedType) field.getGenericType();
            return (Class) type.getActualTypeArguments()[0];
        }
        return field.getType();
    }

    private <E> E handlerField(Field field, Function<Annotation[], E> function) {
        Annotation[] annotations = field.getDeclaredAnnotations();
        return function.apply(annotations);
    }

    private <A> A findAnnotation(Annotation[] annotations, Class<A> headerClass) {
        Annotation result = Stream.of(annotations)
                .filter(annotation -> headerClass != null
                        && headerClass.isAssignableFrom(annotation.annotationType())
                        && annotation.annotationType().getTypeName().equals(headerClass.getTypeName()))
                .findFirst()
                .orElse(null);
        return (A) result;
    }

}
