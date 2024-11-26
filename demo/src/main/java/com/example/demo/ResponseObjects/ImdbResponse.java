package com.example.demo.ResponseObjects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ImdbResponse {
    private List<Movie> titles;

    // Getters and setters
    public List<Movie> getTitles() {
        return titles;
    }

    public void setTitles(List<Movie> titles) {
        this.titles = titles;
    }
}



