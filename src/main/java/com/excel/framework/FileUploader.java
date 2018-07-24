package com.excel.framework;

import java.io.InputStream;

public interface FileUploader<T> {
    /**
     * @param inputStream input parameter
     * @return T any object that can be received from inputStream
     * @throws Exception
     */
    T extractData(InputStream inputStream) throws Exception;
}