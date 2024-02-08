package com.movieapp.core;

import com.movieapp.domain.service.MovieCsvLoaderService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class CsvDataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final MovieCsvLoaderService movieCsvLoaderService;

    public CsvDataInitializer(MovieCsvLoaderService movieCsvLoaderService) {
        this.movieCsvLoaderService = movieCsvLoaderService;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        movieCsvLoaderService.loadAndPersistCsvFile("src/main/resources/movielist.csv");
    }
}
