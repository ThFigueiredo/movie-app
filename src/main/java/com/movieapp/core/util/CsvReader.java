package com.movieapp.core.util;

import com.movieapp.core.config.exception.CsvReadException;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class CsvReader<T> implements FileReader<T> {

    private final Class<T> typeParameterClass;
    private char separator = ';';

    public CsvReader(Class<T> typeParameterClass) {
        this.typeParameterClass = typeParameterClass;
    }

    @Override
    public List<T> read(String path) {
        try (Reader reader = Files.newBufferedReader(Paths.get(path))) {
            return read(reader);
        } catch (Exception e) {
            throw new CsvReadException("Erro ao ler o arquivo CSV em " + path, e);
        }
    }

    @Override
    public List<T> read(Reader reader) {
        try {
            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                    .withType(typeParameterClass)
                    .withSeparator(separator)
                    .build();

            return csvToBean.parse();
        } catch (Exception e) {
            throw new CsvReadException("Erro ao ler o arquivo CSV", e);
        }
    }
}
