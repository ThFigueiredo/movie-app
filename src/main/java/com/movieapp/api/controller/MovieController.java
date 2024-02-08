package com.movieapp.api.controller;

import com.movieapp.api.dto.IntervalDTO;
import com.movieapp.domain.model.MovieModel;
import com.movieapp.domain.service.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping(value = "/api/movie")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/interval")
    public ResponseEntity<?> getMinMaxWinningInterval() {
        try {
            IntervalDTO intervalDTO = movieService.getMinMaxIntervalDTO();
            return ResponseEntity.ok(intervalDTO);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body("Erro ao obter a maior e menor faixa de prêmios dos produtores.");
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getAllMovie(Pageable pageable) {
        try {
            Page<MovieModel> moviePage = movieService.getAllMovies(pageable);
            List<MovieModel> movies = moviePage.getContent();
            return ResponseEntity.ok(movies);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body("Erro ao obter a lista de filmes.");
        }
    }
}
