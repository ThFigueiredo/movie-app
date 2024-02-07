package com.movieapp.domain.repository;


import com.movieapp.domain.model.MovieModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<MovieModel, Integer> {

    List<MovieModel> findAllByProducersContainingAndWinnerOrderByYearAsc(String producer, String winner);

    @Query("SELECT m.producers FROM MovieModel m WHERE m.winner = 'yes' GROUP BY m.producers")
    Optional<List<String>> findAllProducersByWinnerMovies();

    @Query("SELECT COUNT(m.id) FROM MovieModel m WHERE m.winner = 'yes' AND m.producers LIKE CONCAT('%', :producer, '%')")
    Integer countProducerWins(@Param("producer") String producer);


}
