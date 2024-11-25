package com.example.demo.imdbapi;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/imdb")
public class ImdbController {

    @Autowired
    private ImdbService imdbService;

    @GetMapping("/film")
    public JsonNode getFilmData(@RequestParam(name = "imdbId", required = false) String imdbId,
                                @RequestParam(name = "query", required = false) String query) {
        // Use either imdbId or query (fallback to query if imdbId is not present)
        String id = (imdbId != null) ? imdbId : query;
        return imdbService.fetchFilmData(id);
    }

    @GetMapping("/search")
    public List<Map<String, String>> searchMovies(@RequestParam String query) {
        JsonNode results = imdbService.searchMovies(query);
        return imdbService.formatSearchResults(results);
    }
} 