package com.movieapp.service;

import com.movieapp.domain.model.MovieModel;
import com.movieapp.core.util.CsvReader;
import com.movieapp.core.util.FileReader;
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
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class MovieCsvLoaderServiceIntegrationTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void shouldLoadAndPersistCsvFile() {
        FileReader<MovieModel> fileReader = new CsvReader<>(MovieModel.class);
        Optional<List<MovieModel>> movieList = Optional.ofNullable(
                fileReader.read(new StringReader(getFileData())));

        assertThat(movieList).isPresent();
        assertThat(movieList.get()).hasSizeGreaterThan(0);

        for (MovieModel movie : movieList.get()) {
            String movieTitle = movie.getTitle();
            Integer id = testEntityManager.persistAndGetId(movie, Integer.class);
            MovieModel persistedMovie = testEntityManager.find(MovieModel.class, id);

            assertThat(persistedMovie).isNotNull();
            assertThat(persistedMovie.getTitle()).isEqualTo(movieTitle);
        }

        assertThat(movieList.get().get(0).getYear()).isEqualTo("1980");
        assertThat(movieList.get().get(0).getTitle()).isEqualTo("Can't Stop the Music");
        assertThat(movieList.get().get(0).getProducers()).isEqualTo("Allan Carr");
        assertThat(movieList.get().get(1).getTitle()).isEqualTo("Cruising");
        assertThat(movieList.get().get(1).getStudios()).isEqualTo("Lorimar Productions, United Artists");
        assertThat(movieList.get().get(movieList.get().size() - 1).getTitle()).isEqualTo("Rambo: Last Blood");
    }
}
