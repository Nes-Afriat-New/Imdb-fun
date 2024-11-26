package com.example.demo.imdbapi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.ResponseObjects.ImdbResponse;
import com.example.demo.ResponseObjects.Movie;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

@Service
public class ImdbService {

    private static final Logger logger = LoggerFactory.getLogger(ImdbService.class);
    private static final String BASE_URL = "http://php-imdb-api:80/api.php";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public ImdbService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Fetch film data by IMDb ID.
     * @param imdbId The IMDb ID of the film.
     * @return Film data as a JsonNode.
     */
public JsonNode fetchFilmData(String imdbId) {
    String url = BASE_URL + "?type=film&query=" + imdbId;
    logger.info("Fetching film data for IMDb ID: {}", imdbId);

    try {
        String response = restTemplate.getForObject(url, String.class);
        logger.debug("Raw response: {}", response);

        // Clean and validate JSON response
        response = cleanResponse(response);
        logger.debug("Cleaned response: {}", response);

        return objectMapper.readTree(response);
    } catch (Exception e) {
        logger.error("Error fetching film data for IMDb ID: {}", imdbId, e);
        throw new RuntimeException("Error parsing response from PHP API", e);
    }
}

    private String cleanResponse(String response) {
        // Log raw response for debugging
        logger.debug("Raw API response: {}", response);

        // Match and extract the first JSON object or array
        String jsonRegex = "(\\{.*\\}|\\[.*\\])";
        Pattern pattern = Pattern.compile(jsonRegex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(response);

        if (matcher.find()) {
            return matcher.group(1); // Return the matched JSON
        } else {
            throw new RuntimeException("Failed to extract valid JSON from response: " + response);
        }
    }

    
    /**
     * Search movies by query.
     * @param query The search query.
     * @return Search results as a List<Map<String, String>>.
     */
    public List<Map<String, String>> searchMovies(String query) {
        String url = BASE_URL + "?type=search&query=" + query;
        logger.info("Searching for movies with query: {}", query);
    
        try {
            // Fetch raw response
            String response = restTemplate.getForObject(url, String.class);
            logger.debug("Raw response: {}", response);
    
            // Parse response into ImdbResponse object
            ImdbResponse imdbResponse = objectMapper.readValue(response, ImdbResponse.class);
            logger.info("Parsed response into ImdbResponse object.");
    
            // Extract titles from the response
            List<Movie> movies = imdbResponse.getTitles();
            if (movies != null && !movies.isEmpty()) {
                logger.info("Extracted {} movies from response.", movies.size());
    
                // Format and return results
                return formatSearchResults(movies);
            } else {
                logger.warn("No movies found in the response.");
                return Collections.emptyList();
            }
        } catch (Exception e) {
            logger.error("Error while searching movies with query: {}", query, e);
            throw new RuntimeException("Error parsing response from PHP API", e);
        }
    }
    
    /**
     * Format search results into a list of maps.
     * @param searchResults The raw search results as a JsonNode.
     * @return A list of maps containing simplified movie details.
     */
    public List<Map<String, String>> formatSearchResults(List<Movie> movies) {
        logger.info("Formatting {} movies into simplified results.", movies.size());
    
        List<Map<String, String>> formattedResults = new ArrayList<>();
        for (Movie movie : movies) {
            Map<String, String> movieDetails = new HashMap<>();
            movieDetails.put("title", movie.getTitle() != null ? movie.getTitle() : "Unknown");
            movieDetails.put("image", (movie.getImage() != null && !movie.getImage().isEmpty()) 
                                       ? movie.getImage() 
                                       : "No image available");
            movieDetails.put("id", movie.getId() != null ? movie.getId() : "N/A");
            logger.debug("Formatted movie: {}", movieDetails);
            formattedResults.add(movieDetails);
        }
    
        return formattedResults;
    }
    
}