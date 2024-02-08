package com.movieapp.controller;

import com.movieapp.api.dto.IntervalDTO;
import com.movieapp.api.dto.ProducerDTO;
import com.movieapp.domain.model.MovieModel;
import com.movieapp.domain.repository.MovieRepository;
import com.movieapp.domain.service.MovieService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MovieControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;

    @Autowired
    private MovieRepository movieRepository;

    @Test
    public void shouldGetAllMovies() throws Exception {
        MovieModel movie1 = MovieModel.builder().title("Title1").studios("Studio1").producers("Producers1")
                .winner("Yes").build();
        MovieModel movie2 = MovieModel.builder().title("Title2").studios("Studio2").producers("Producers2")
                .winner("Yes").build();

        movieRepository.saveAll(List.of(movie1, movie2));

        when(movieService.getAllMovies(org.mockito.ArgumentMatchers.any())).thenAnswer(invocation -> {
            Pageable pageable = invocation.getArgument(0);
            return new PageImpl<>(movieRepository.findAll(pageable).getContent());
        });

                mockMvc.perform(MockMvcRequestBuilders.get("/api/movie")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title", Matchers
                        .equalToIgnoringCase("Title1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title", Matchers
                        .equalToIgnoringCase("Title2")));

    }
    @Test
    public void shouldGetMinMaxWinningInterval() throws Exception {
        IntervalDTO intervalDTO = new IntervalDTO();
        intervalDTO.setMin(Arrays.asList(new ProducerDTO("Producer1", 2, "2000", "2002")));
        intervalDTO.setMax(Arrays.asList(new ProducerDTO("Producer2", 5, "1995", "2000")));

        when(movieService.getMinMaxIntervalDTO()).thenReturn(intervalDTO);

        ResultActions result = mockMvc.perform(get("/api/movie/interval"));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.min", hasSize(1)))
                .andExpect(jsonPath("$.max", hasSize(1)));

        verify(movieService, times(1)).getMinMaxIntervalDTO();
    }

    @Test
    public void shouldReturnBadRequestWithCorrectErrorMessage() throws Exception {
        Mockito.when(movieService.getMinMaxIntervalDTO()).thenThrow(new RuntimeException());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/movie/interval")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Erro ao obter a maior e menor faixa de prêmios dos produtores."));
    }

    @Test
    public void shouldReturnBadRequestWithCorrectErrorMessageForGetAllMovies() throws Exception {
        Mockito.when(movieService.getAllMovies(Mockito.any())).thenThrow(new RuntimeException(""));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/movie")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Erro ao obter a lista de filmes."));
    }

}
