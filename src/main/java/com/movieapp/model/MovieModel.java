package com.movieapp.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "moviesTable")
public class MovieModel {

    @Id
    @GeneratedValue
    @Column(name = "M_ID")
    private Long id;

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