package com.movieapp.repository;


import com.movieapp.domain.model.MovieModel;
import com.movieapp.domain.repository.MovieRepository;
import com.movieapp.core.util.CsvReader;
import com.movieapp.core.util.FileReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.io.StringReader;
import java.util.List;
import java.util.Optional;

import static com.movieapp.util.MovieData.getFileData;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class MovieRepositoryIntegrationTest {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    void setUp() {
        FileReader<MovieModel> fileReader = new CsvReader<>(MovieModel.class);
        Optional<List<MovieModel>> movieList = Optional.ofNullable(
                fileReader.read(new StringReader(getFileData())));

        assertTrue(movieList.isPresent());
        movieList.get().forEach(movie -> testEntityManager.persistAndFlush(movie));
    }

    @Test
    void shouldRetrieveWinningProducersList() {
        Optional<List<String>> winningProducersList = movieRepository.findAllProducersByWinnerMovies();

        assertTrue(winningProducersList.isPresent());
        assertTrue(winningProducersList.get().contains("Frank Yablans"));
        assertTrue(winningProducersList.get().contains("Mitsuharu Ishii"));

    }

    @Test
    void shouldCountProducerWins() {
        int countAllanCarrWins = movieRepository.countProducerWins("Frank Yablans");
        int countBoDerekWins = movieRepository.countProducerWins("Bo Derek");
        int countSteveShaganWins = movieRepository.countProducerWins("Steve Shagan");

        assertNotEquals(0, countAllanCarrWins);
        assertEquals(1, countAllanCarrWins);
        assertEquals(2, countBoDerekWins);
        assertEquals(0, countSteveShaganWins);
    }

    @Test
    void shouldFindMoviesByWinningProducers() {
        List<MovieModel> frankYablansMovies = movieRepository.findAllByProducersContainingAndWinnerOrderByYearAsc("Frank Yablans", "yes");
        List<MovieModel> boDerekMovies = movieRepository.findAllByProducersContainingAndWinnerOrderByYearAsc("Bo Derek", "yes");

        assertTrue(frankYablansMovies.stream().anyMatch(movie -> movie.getTitle().equals("Mommie Dearest")));
        assertFalse(frankYablansMovies.stream().anyMatch(movie -> movie.getTitle().equals("Where the Boys Are '84")));

        assertTrue(boDerekMovies.stream().anyMatch(movie -> movie.getTitle().equals("Bolero")));
        assertTrue(boDerekMovies.stream().anyMatch(movie -> movie.getTitle().equals("Ghosts Can't Do It")));
    }
}