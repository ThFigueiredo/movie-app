package com.movieapp.api.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class IntervalDTO {
    public IntervalDTO() {
        min = new ArrayList<>();
        max = new ArrayList<>();
    }

    private List<ProducerDTO> min;
    private List<ProducerDTO> max;

}
