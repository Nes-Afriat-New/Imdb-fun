package com.example.demo.imdbapi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.ResponseObjects.Movie;

import jdk.jfr.Label;

@RestController
@RequestMapping("/api/imdb")
public class ImdbController {

    @Autowired
    private ImdbService imdbService;

    private static final Logger logger = LoggerFactory.getLogger(ImdbController.class);
    @GetMapping("/film")
    public JsonNode getFilmData(@RequestParam(name = "imdbId", required = false) String imdbId,
                                @RequestParam(name = "query", required = false) String query) {
        // Use either imdbId or query (fallback to query if imdbId is not present)
        String id = (imdbId != null) ? imdbId : query;
        return imdbService.fetchFilmData(id);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Map<String, String>>> searchMovies(@RequestParam String query) {
        if (query == null || query.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }

        List<Map<String, String>> results = imdbService.searchMovies(query);
        return ResponseEntity.ok(results);
    }
    
} 