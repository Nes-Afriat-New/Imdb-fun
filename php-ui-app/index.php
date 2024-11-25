<?php
// Initialize variables
$result = null;
$error = null;

// Handle form submission
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $type = $_POST['type']; // 'film' or 'search'
    $query = $_POST['query']; // Input query (IMDb ID or search term)

    // Prepare the URL for the Spring Boot application
    $url = ($type === 'film') 
    ? "http://demo-app:8080/api/imdb/film?imdbId=" . urlencode($query)
    : "http://demo-app:8080/api/imdb/search?query=" . urlencode($query);

    // Send the request to the Spring Boot application using cURL
    $ch = curl_init($url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_TIMEOUT, 120); 

    $response = curl_exec($ch);
    $error = curl_error($ch);
    curl_close($ch);

    if ($error) {
        $result = "Error connecting to the Spring Boot service: $error";
    } else {
        $result = json_decode($response, true);
    }
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>IMDb Search UI</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <h1>IMDb Search</h1>
    <form method="POST">
        <label for="type">Type:</label>
        <select name="type" id="type">
            <option value="film">Film</option>
            <option value="search">Search</option>
        </select>
        <br><br>
        <label for="query">Query:</label>
        <input type="text" id="query" name="query" required>
        <br><br>
        <button type="submit">Submit</button>
    </form>

    <?php if (isset($result) && is_array($result)): ?>
        <div class="result">
            <h2>Result:</h2>
            <!-- For Film Type -->
            <?php if ($_POST['type'] === 'film'): ?>
                <h3><?php echo htmlspecialchars($result['title'] ?? 'N/A'); ?> (<?php echo htmlspecialchars($result['year'] ?? 'N/A'); ?>)</h3>
                <?php if (!empty($result['poster'])): ?>
                    <img src="<?php echo htmlspecialchars($result['poster']); ?>" alt="Poster" style="width:200px;">
                <?php endif; ?>
                <p><strong>Plot:</strong> <?php echo htmlspecialchars($result['plot'] ?? 'N/A'); ?></p>
                <p><strong>Rating:</strong> <?php echo htmlspecialchars($result['rating'] ?? 'N/A'); ?></p>
                <h4>Cast:</h4>
                <ul>
                    <?php foreach ($result['cast'] as $actor): ?>
                        <li>
                            <strong><?php echo htmlspecialchars($actor['actor'] ?? 'Unknown'); ?></strong> as <?php echo htmlspecialchars($actor['character'] ?? 'Unknown'); ?>
                            <?php if (!empty($actor['avatar'])): ?>
                                <br>
                                <img src="<?php echo htmlspecialchars($actor['avatar']); ?>" alt="Actor" style="width:50px; border-radius:50%;">
                            <?php endif; ?>
                        </li>
                    <?php endforeach; ?>
                </ul>
            <!-- For Search Type -->
            <?php elseif ($_POST['type'] === 'search'): ?>
                <h4>Search Results:</h4>
                <ul>
                    <?php foreach ($result as $movie): ?>
                        <li>
                            <strong><?php echo htmlspecialchars($movie['title'] ?? 'Unknown'); ?></strong>
                            <?php if (!empty($movie['image'])): ?>
                                <br>
                                <img src="<?php echo htmlspecialchars($movie['image']); ?>" alt="Image" style="width:100px;">
                            <?php endif; ?>
                        </li>
                    <?php endforeach; ?>
                </ul>
            <?php endif; ?>
        </div>
    <?php elseif ($error): ?>
        <div class="error">
            <h2>Error:</h2>
            <p><?php echo htmlspecialchars($error); ?></p>
        </div>
    <?php endif; ?>
</body>
</html>
