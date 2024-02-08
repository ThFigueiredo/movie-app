package com.movieapp.domain.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@SuperBuilder
@Table(name = "moviesTable")
public class MovieModel {

    @Id
    @GeneratedValue
    @Column(name = "M_ID")
    private Integer id;

    @Column(name = "M_YEAR")
    private String year;

    @Column(name = "M_TITLE")
    private String title;

    @Column(name = "M_STUDIOS")
    private String studios;

    @Column(name = "M_PRODUCERS")
    private String producers;

    @Column(name = "M_WINNER")
    private String winner;

}
