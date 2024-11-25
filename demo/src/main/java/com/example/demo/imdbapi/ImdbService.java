package com.example.demo.imdbapi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.*;

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
     * @return Search results as a JsonNode.
     */
    public JsonNode searchMovies(String query) {
        String url = "http://php-imdb-api:80/api.php?type=search&query=" + query;
        logger.info("Searching for movies with query: {}", query);

        try {
            // Send the GET request and capture the raw response as a String
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);

            // Log the raw response body and content type for debugging purposes
            logger.info("Raw response body: {}", response.getBody());
            logger.info("Response Content-Type: {}", response.getHeaders().getContentType());

            // Log the response status code
            logger.info("Response Status Code: {}", response.getStatusCode());

            // Log the response headers (metadata)
            logger.info("Response Headers: {}", response.getHeaders());

            // Check if the response body is empty or invalid
            String responseBody = response.getBody();
            if (responseBody == null || responseBody.isEmpty()) {
                logger.error("Received empty or null response body.");
                return null;
            }

            // Attempt to parse the raw response body into JSON
            JsonNode jsonResponse = objectMapper.readTree(responseBody);

            // If jsonResponse is null, log the error
            if (jsonResponse == null) {
                logger.error("Failed to parse JSON response.");
                return null;
            }

            // Log the parsed JSON response for debugging
            logger.info("Parsed JSON response: {}", jsonResponse.toString());

            // Check if "titles" exists and process it
            JsonNode titlesNode = jsonResponse.get("titles");
            if (titlesNode != null) {
                logger.info("'titles' field found.");
                if (titlesNode.isArray()) {
                    logger.info("'titles' is an array.");
                    for (JsonNode title : titlesNode) {
                        logger.info("Title: {}", title.get("title").asText());
                        logger.info("Image: {}", title.get("image").asText());
                        logger.info("ID: {}", title.get("id").asText());
                    }
                } else {
                    logger.warn("'titles' field is not an array.");
                }
            } else {
                logger.warn("No 'titles' field found in the response.");
            }

            // Check for the "names" field
            JsonNode namesNode = jsonResponse.get("names");
            if (namesNode != null && namesNode.isArray()) {
                logger.info("'names' is an array.");
                for (JsonNode name : namesNode) {
                    logger.info("Name: {}", name.get("title").asText());
                    logger.info("Image: {}", name.get("image").asText());
                }
            } else {
                logger.warn("No 'names' field found or it's not an array.");
            }

            return jsonResponse;
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
    public List<Map<String, String>> formatSearchResults(JsonNode searchResults) {
        logger.info("Formatting search results: {}", searchResults);
        List<Map<String, String>> formattedResults = new ArrayList<>();
    
        if (searchResults.isArray()) {
            logger.debug("Processing search results array.");
            for (JsonNode result : searchResults) {
                Map<String, String> movieDetails = new HashMap<>();
                movieDetails.put("title", result.get("title").asText());
                movieDetails.put("year", result.has("year") ? result.get("year").asText() : "N/A");
                movieDetails.put("image", result.has("image") ? result.get("image").asText() : null);
                formattedResults.add(movieDetails);
            }
            logger.info("Formatted {} search results.", formattedResults.size());
        } else {
            logger.warn("Search results are not in an array format.");
        }
    
        return formattedResults;
    }
}
