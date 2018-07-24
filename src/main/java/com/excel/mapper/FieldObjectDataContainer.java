package com.excel.mapper;

import java.lang.reflect.Field;
import java.util.List;

public class FieldObjectDataContainer<T> {
    private Field field;
    private List<T> fieldParams;

    public FieldObjectDataContainer(Field field, List<T> fieldParams) {
        this.field = field;
        this.fieldParams = fieldParams;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public List<T> getFieldParams() {
        return fieldParams;
    }

    public void setFieldParams(List<T> fieldParams) {
        this.fieldParams = fieldParams;
    }

    public static <T> FieldObjectDataContainer.Builder<T> builder() {
        return new FieldObjectDataContainer.Builder<>();
    }

    public static class Builder<T> {
        private Field field;
        private List<T> fieldParams;

        public Builder<T> field(Field field) {
            this.field = field;
            return this;
        }

        public Builder<T> fieldParams(List<T> fieldParams) {
            this.fieldParams = fieldParams;
            return this;
        }

        public FieldObjectDataContainer<T> build() {
            return new FieldObjectDataContainer<>(this.field, this.fieldParams);
        }

    }
}
