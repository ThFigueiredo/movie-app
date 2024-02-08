package com.movieapp.domain.service;

import com.movieapp.api.dto.IntervalDTO;
import com.movieapp.domain.model.MovieModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MovieService {

    void save(List<MovieModel> movieList);
    IntervalDTO getMinMaxIntervalDTO();
    Page<MovieModel> getAllMovies(Pageable pageable);

}
