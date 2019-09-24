package com.api.service.file;

/*
Contract to write the in memory structure to some persistent location.
can be extended to write to various types.
 */
public interface FileWriter {

    Boolean write(Object obj);
}
