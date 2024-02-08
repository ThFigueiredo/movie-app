package com.movieapp.domain.service;

import com.movieapp.api.dto.IntervalDTO;
import com.movieapp.api.dto.ProducerDTO;
import com.movieapp.core.config.exception.MovieAppException;
import com.movieapp.domain.model.MovieModel;
import com.movieapp.domain.repository.MovieRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public void save(List<MovieModel> movieList) throws DataAccessException {
        try {
            movieRepository.saveAll(movieList);
        } catch (DataAccessException e) {
            throw new DataAccessException("Erro ao salvar dados no banco de dados", e) {};
        }
    }
    @Override
    public IntervalDTO getMinMaxIntervalDTO() {
        try {
            List<String> twiceWinningProducers = getTwiceWinningProducers(
                    movieRepository.findAllProducersByWinnerMovies().orElseGet(ArrayList::new));

            return filterMinMaxIntervalDTO(
                    getAllProducersDTO(twiceWinningProducers));
        } catch (Exception exception) {
            throw new MovieAppException("Erro ao calcular a maior e menor faixa de prêmios dos produtores.", exception);
        }
    }
    @Override
    public Page<MovieModel> getAllMovies(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }

    private IntervalDTO filterMinMaxIntervalDTO(List<ProducerDTO> producersDTO) {
        if (producersDTO.isEmpty()) {
            return new IntervalDTO();
        }

        int minInterval = producersDTO.stream().mapToInt(ProducerDTO::getInterval).min().orElse(0);
        int maxInterval = producersDTO.stream().mapToInt(ProducerDTO::getInterval).max().orElse(0);

        IntervalDTO intervalDTO = new IntervalDTO();
        intervalDTO.setMin(producersDTO.stream().filter(producerDTO -> producerDTO.getInterval() == minInterval).collect(Collectors.toList()));
        intervalDTO.setMax(producersDTO.stream().filter(producerDTO -> producerDTO.getInterval() == maxInterval).collect(Collectors.toList()));

        return intervalDTO;
    }

    private List<ProducerDTO> getAllProducersDTO(List<String> twiceWinningProducers) {
        List<ProducerDTO> producersDTO = new ArrayList<>();

        twiceWinningProducers.forEach(producerName -> producersDTO.addAll(getProducerDTO(
                movieRepository.findAllByProducersContainingAndWinnerOrderByYearAsc(producerName, "yes"),
                producerName)));

        return producersDTO;
    }

    private List<String> getTwiceWinningProducers(List<String> winnerProducers) {
        List<String> twiceWinningProducers = new ArrayList<>();

        winnerProducers.forEach(producers -> {
            for (String producerName : producers.split(", | and ")) {
                if (!twiceWinningProducers.contains(producerName) && movieRepository.countProducerWins(producerName) > 1) {
                    twiceWinningProducers.add(producerName);
                }
            }
        });

        return twiceWinningProducers;
    }

    private List<ProducerDTO> getProducerDTO(List<MovieModel> producerMovies, String producerName) {
        List<ProducerDTO> producersDTO = new ArrayList<>();
        for (int i = 0; i < producerMovies.size() - 1; i++) {
            MovieModel previousWin = producerMovies.get(i);
            MovieModel followingWin = producerMovies.get(i + 1);

            Integer interval = Integer.parseInt(followingWin.getYear()) - Integer.parseInt(previousWin.getYear());

            producersDTO.add(
                    new ProducerDTO(
                            producerName,
                            interval,
                            previousWin.getYear(),
                            followingWin.getYear()));
        }

        return producersDTO;
    }
}
