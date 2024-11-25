<?php

require "vendor/autoload.php";

use hmerritt\Imdb;

header('Content-Type: application/json');

// Retrieve query parameters
$type = $_GET['type'] ?? null; // Type: 'film' or 'search'
$query = $_GET['query'] ?? null; // IMDb ID or search string

// Validate input
if (!$type || !$query) {
    http_response_code(400);
    echo json_encode(['error' => 'Invalid parameters. Please provide "type" and "query".']);
    exit;
}

try {
    $imdb = new Imdb();

    switch($type)
{
    case "film":
        $response = $imdb->film($query, ['cache' => false, 'techSpecs' => true]);
        break;

    case "search":
        $response = $imdb->search($query, ['cache' => false]);
        break;
     default:
        http_response_code(400);
        echo json_encode(['error' => 'Invalid type. Use "film" or "search".']);
        exit;
    }

    // Return the result as JSON
    echo json_encode($response, JSON_PRETTY_PRINT);

} catch (Exception $e) {
    // Handle exceptions
    http_response_code(500);
    echo json_encode(['error' => 'An error occurred', 'details' => $e->getMessage()]);
}
