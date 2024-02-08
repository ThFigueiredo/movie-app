package com.movieapp.domain.service;

import com.movieapp.domain.model.MovieModel;
import com.movieapp.core.util.CsvReader;
import com.movieapp.core.util.FileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieCsvLoaderServiceImpl implements MovieCsvLoaderService {

    private final MovieService movieService;

    @Autowired
    public MovieCsvLoaderServiceImpl(MovieService movieService) {
        this.movieService = movieService;
    }

    @Override
    public void loadAndPersistCsvFile(String path) {
        FileReader<MovieModel> fileReader = new CsvReader<>(MovieModel.class);
        Optional<List<MovieModel>> movieList = Optional.ofNullable(fileReader.read(path));
        movieList.ifPresent(movieService::save);
    }

}
