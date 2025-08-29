package com.example.cinemawall;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class HomeController {
    @FXML
    public VBox searchResultsVBox;
    @FXML
    public FlowPane searchResultsFlowPane;
    @FXML
    public Button logoutButton;
    public AnchorPane AppLabel;
    public AnchorPane mainContentAnchorPane;
    public AnchorPane profileAnchorPane;
    public AnchorPane watchlistAnchorPane;
    public AnchorPane MovieOrSeriesDetails;
    public ImageView posterImageView;
    public Label titleLabel;
    public Label descriptionLabel;
    public Label directorLabel;
    public Label castLabel;
    public Label releasedYearLabel;
    public Label genresLabel;
    public ComboBox ratingComboBox;
    public Button addToFavouritesButton;
    public Button addToWatchlistButton;
    public AnchorPane Sidebar;
    public ComboBox popularCombo;
    public ComboBox topCombo;
    public Button profileButton;
    //public Button watchlistButton;
    public Label durationLabel;
    public AnchorPane browseMoreMoviesAnchorPane;
    public AnchorPane browseMoreShowsAnchorPane;
    public Label userNameLabel;
    public ImageView favMovieImage1;
    public Hyperlink favMovieLink1;
    public ImageView favMovieImage2;
    public Hyperlink favMovieLink2;
    public ImageView favMovieImage3;
    public Hyperlink favMovieLink3;
    public ImageView favMovieImage4;
    public Hyperlink favMovieLink4;
    public ImageView favSeriesImage1;
    public Hyperlink favSeriesLink1;
    public ImageView favSeriesImage2;
    public Hyperlink favSeriesLink2;
    public ImageView favSeriesImage3;
    public Hyperlink favSeriesLink3;
    public ImageView favSeriesImage4;
    public Hyperlink favSeriesLink4;
    public ImageView ratedMovieImage1;
    public Hyperlink ratedMovieLink1;
    public ImageView ratedMovieImage2;
    public Hyperlink ratedMovieLink2;
    public ImageView ratedMovieImage3;
    public Hyperlink ratedMovieLink3;
    public ImageView ratedMovieImage4;
    public Hyperlink ratedMovieLink4;
    public ScrollPane browseMoreSeriesScrollPane;
    public ScrollPane browseMoreMoviesScrollPane;
    public GridPane browseMoviesGridPane;
    public GridPane browseShowsGridPane;
    public GridPane watchlistGridPane;
    public ScrollPane watchlistScrollPane;
    public Label ratingLabel;
    public Label YourRatingLabel;
    @FXML
    private ImageView moviePosterImageView1, moviePosterImageView2, moviePosterImageView3, moviePosterImageView4;
    @FXML
    private Hyperlink movieTitleHyperlink1, movieTitleHyperlink2, movieTitleHyperlink3, movieTitleHyperlink4;
    @FXML
    private ImageView seriesPosterImageView1, seriesPosterImageView2, seriesPosterImageView3, seriesPosterImageView4;
    @FXML
    private Hyperlink seriesTitleHyperlink1, seriesTitleHyperlink2, seriesTitleHyperlink3, seriesTitleHyperlink4;
    @FXML
    private TextField searchTextField;
    @FXML
    private Button searchButton;
    @FXML
    public ImageView showsPosterImageView1,showsPosterImageView2,showsPosterImageView3,showsPosterImageView4,showsPosterImageView5,showsPosterImageView6,showsPosterImageView7,showsPosterImageView8,showsPosterImageView9,showsPosterImageView10,showsPosterImageView11,showsPosterImageView12,showsPosterImageView13,showsPosterImageView14,showsPosterImageView15;
    public ImageView filmPosterImageView1,filmPosterImageView2,filmPosterImageView3,filmPosterImageView4,filmPosterImageView5,filmPosterImageView6,filmPosterImageView7,filmPosterImageView8,filmPosterImageView9,filmPosterImageView10,filmPosterImageView11,filmPosterImageView12,filmPosterImageView13,filmPosterImageView14,filmPosterImageView15;
    @FXML
    public  Hyperlink showsTitleHyperlink1,showsTitleHyperlink2,showsTitleHyperlink3,showsTitleHyperlink4,showsTitleHyperlink5,showsTitleHyperlink6,showsTitleHyperlink7,showsTitleHyperlink8,showsTitleHyperlink9,showsTitleHyperlink10,showsTitleHyperlink11,showsTitleHyperlink12,showsTitleHyperlink13,showsTitleHyperlink14,showsTitleHyperlink15;
    public  Hyperlink filmTitleHyperlink1,filmTitleHyperlink2,filmTitleHyperlink3,filmTitleHyperlink4,filmTitleHyperlink5,filmTitleHyperlink6,filmTitleHyperlink7,filmTitleHyperlink8,filmTitleHyperlink9,filmTitleHyperlink10,filmTitleHyperlink11,filmTitleHyperlink12,filmTitleHyperlink13,filmTitleHyperlink14,filmTitleHyperlink15;
    public int browse;
    public int rate;

    private final String apiKey = "3df91752"; // Use your OMDb API key here
    private final DatabaseHelper dbHelper = new DatabaseHelper();
    private String userID; // The current logged-in user's ID

    // Set the user ID after login
    public void setUserID(String userID) {
        this.userID = userID;
        dbHelper.setUserID(userID);
        loadHomepage();
    }
    ObservableList<String> ratings = FXCollections.observableArrayList("1", "2", "3", "4", "5");

    @FXML
    public void initialize() {
        // Set up search button action
        searchButton.setOnAction(action -> handleSearch());


        // Set the ComboBox items to the ObservableList
        ratingComboBox.setItems(ratings);

        // Set a default value if needed
//        ratingComboBox.setValue("1");
//
//        // Optional: Add a listener to capture selection changes
//        ratingComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
//
//        });
    }

    public void loadHomepage() {
        this.browse=0;
        if (userID == null) return;
        mainContentAnchorPane.setVisible(true);
        MovieOrSeriesDetails.setVisible(false);
        watchlistAnchorPane.setVisible(false);
        AppLabel.setVisible(true);
        profileAnchorPane.setVisible(false);
        Sidebar.setVisible(true);
        browseMoreMoviesAnchorPane.setVisible(false);
        browseMoreShowsAnchorPane.setVisible(false);
        watchlistAnchorPane.setVisible(false);
        // Load the last 4 added movies and series from the database
        List<String[]> movies = dbHelper.getLastAddedMoviesByUser(userID);
        List<String[]> series = dbHelper.getLastAddedSeriesByUser(userID);

        if (movies.isEmpty()) {
            insertDefaultMovies();
        }
        for (int i = 0; i < Math.min(movies.size(), 4); i++) {
            String title = movies.get(i)[0];
            String year = movies.get(i)[1];
            fetchAndDisplayMovie(title, year, i);
        }

        if (series.isEmpty()) {
            insertDefaultSeries();
        }
        for (int i = 0; i < Math.min(series.size(), 4); i++) {
            String title = series.get(i)[0];
            String year = series.get(i)[1];
            fetchAndDisplaySeries(title, year, i);
        }
    }

    private void insertDefaultMovies() {
        String[][] defaultMovies = {
                {"The Shawshank Redemption", "1994"},
                {"The Dark Knight", "2008"},
                {"Inception", "2010"},
                {"Interstellar", "2014"}
        };

        for (String[] movie : defaultMovies) {
            dbHelper.addOrUpdateMovie(userID, movie[0], movie[1]);
        }
    }

    private void insertDefaultSeries() {
        String[][] defaultSeries = {
                {"Breaking Bad", "2008"},
                {"Stranger Things", "2016"},
                {"Game of Thrones", "2011"},
                {"The Crown", "2016"}
        };

        for (String[] series : defaultSeries) {
            dbHelper.addOrUpdateSeries(userID, series[0], series[1]);
        }
    }

    private void handleSearch() {
        String query = searchTextField.getText().trim();

        if (query.isEmpty()) return;

        fetchFromOMDb(query, true);
    }

    private void fetchFromOMDb(String query, boolean isSearch) {
        String urlString = "http://www.omdbapi.com/?t=" + query + "&apikey=" + apiKey;

        new Thread(() -> {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONObject result = new JSONObject(response.toString());
                if (result.getString("Response").equals("True")) {
                    String title = result.getString("Title");
                    String year = result.getString("Year");
                    String type = result.getString("Type");

                    Platform.runLater(() -> {
                        if (isSearch) {
                            displaySearchResults(result, type);
                        } else {
                            loadHomepage(); // Reload homepage after fetching new data
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }

    private void displaySearchResults(JSONObject result, String type) {
        Platform.runLater(() -> {
            searchResultsFlowPane.getChildren().clear(); // Clear previous results

            String title = result.optString("Title");
            String posterUrl = result.optString("Poster");

            // Create an ImageView for the poster
            ImageView posterImageView = new ImageView();
            posterImageView.setFitWidth(100); // Set desired width
            posterImageView.setPreserveRatio(true);

            if (!posterUrl.isEmpty() && !"N/A".equals(posterUrl)) {
                posterImageView.setImage(new Image(posterUrl));
            }

            // Create a Hyperlink for the title
            Hyperlink titleLink = new Hyperlink(title);
            titleLink.setOnAction(event -> {
                if ("series".equals(type)) {
                    showDetails(result); // Show series details if it's a series
                } else {
                    showDetails(result); // Show movie details if it's a movie
                }
            });

            // Create a VBox to hold the poster and title
            VBox resultBox = new VBox(5); // 5px spacing between items
            resultBox.getChildren().addAll(posterImageView, titleLink);
            resultBox.setStyle("-fx-alignment: center;");

            // Add the result box to the FlowPane
            searchResultsFlowPane.getChildren().add(resultBox);
        });
    }

    private void showDetails(JSONObject movieData) {
        ratingComboBox.setValue("0");
        mainContentAnchorPane.setVisible(false);
        MovieOrSeriesDetails.setVisible(true);
        watchlistAnchorPane.setVisible(false);
        AppLabel.setVisible(true);
        profileAnchorPane.setVisible(false);
        Sidebar.setVisible(true);
        browseMoreMoviesAnchorPane.setVisible(false);
        browseMoreShowsAnchorPane.setVisible(false);

        String title = movieData.optString("Title");
        String year = movieData.optString("Released");
        String rating = String.valueOf(dbHelper.totalRating(title, year));
        String plot = movieData.optString("Plot");
        String director = movieData.optString("Director");
        String cast = movieData.optString("Actors");
        String posterUrl = movieData.optString("Poster");
        String genre = movieData.optString("Genre");
        String type = movieData.optString("Type");
        String duration = movieData.optString("Runtime");

        if ("movie".equalsIgnoreCase(type)) {
            dbHelper.addOrUpdateMovie(userID, title, year);
        } else if ("series".equalsIgnoreCase(type)) {
            dbHelper.addOrUpdateSeries(userID, title, year);
        }

        // Set poster image (check for empty or invalid URL)
        if (posterUrl != null && !posterUrl.isEmpty()) {
            posterImageView.setImage(new Image(posterUrl));
        } else {
            posterImageView.setImage(new Image("defaultPosterUrl")); // Set a default image if the poster URL is invalid
        }

        // Set UI labels
        titleLabel.setText(title);
        descriptionLabel.setText(plot);
        directorLabel.setText(director);
        castLabel.setText(cast);
        releasedYearLabel.setText(year);
        genresLabel.setText(genre);
        durationLabel.setText(duration);
        double currentRating = dbHelper.totalRating(title, year);
        ratingLabel.setText(String.valueOf(currentRating));

        // Fetch current rating from the database


        // Check if the user has already rated this movie/series
        double userRating = dbHelper.isRated(userID, title);
        if (userRating == -1) {
            YourRatingLabel.setText("");  // No rating by user
        } else {
            YourRatingLabel.setText(String.valueOf(userRating));  // Display the user's rating
        }

        // Rating ComboBox Action Event
        ratingComboBox.setOnAction(event -> {
            try {
                String selectedRating = (String) ratingComboBox.getValue();

                // Validate the selected rating
                if (selectedRating != "0" ) {
                    double rating1 = Double.parseDouble(selectedRating);

                    // Update the database with the new rating for the specific movie
                    dbHelper.addOrUpdateRating(userID, title, year, rating1);

                    // Update the label to show the newly selected rating
                    YourRatingLabel.setText(selectedRating);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid rating value: " + ratingComboBox.getValue());
            }
        });

        // Add to Favourites
        if ("movie".equals(type)) {
            addToFavouritesButton.setOnAction(event -> {
                dbHelper.addOrUpdateFavouriteMovie(userID, title, year);
                showAlert("ADDED TO FAVOURITES", "Successfully Added to Favourites", Alert.AlertType.INFORMATION);
            });
        } else if ("series".equals(type)) {
            addToFavouritesButton.setOnAction(event -> {
                dbHelper.addOrUpdateFavouriteSeries(userID, title, year);
                showAlert("ADDED TO FAVOURITES", "Successfully Added to Favourites", Alert.AlertType.INFORMATION);
            });
        }

        // Add to Watchlist
        addToWatchlistButton.setOnAction(event -> {
            if (dbHelper.isItemInWatchlist(userID, title)) {
                showAlert("Warning", "Already in the watchlist", Alert.AlertType.WARNING);
            } else {
                dbHelper.addToWatchlist(userID, title, year);
                showAlert("Added", "ADDED TO WATCHLIST", Alert.AlertType.INFORMATION);
            }
        });
    }




    private void fetchAndDisplayMovie(String title, String year, int index) {
        fetchAndDisplayMedia(title, year, index, "movie");
    }

    private void fetchAndDisplaySeries(String title, String year, int index) {
        fetchAndDisplayMedia(title, year, index, "series");
    }
    private void fetchAndDisplay(String title, String year, int index) {
        fetchAndDisplayMedia1(title, year, index);
    }

    private void fetchAndDisplayMedia(String title, String year, int index, String type) {
        String urlString = "http://www.omdbapi.com/?t=" + title + "&y=" + year + "&apikey=" + apiKey;

        new Thread(() -> {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONObject result = new JSONObject(response.toString());
                if (result.getString("Response").equals("True")) {
                    Platform.runLater(() -> updateMediaUI(result, index, type));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    private void fetchAndDisplayMedia1(String title, String year, int index) {
        String urlString = "http://www.omdbapi.com/?t=" + title + "&y=" + year + "&apikey=" + apiKey;

        new Thread(() -> {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONObject result = new JSONObject(response.toString());
                if (result.getString("Response").equals("True")) {
                    Platform.runLater(() -> updateMediaUI1(result, index));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void updateMediaUI(JSONObject result, int index, String type) {
        String title = result.optString("Title");
        String posterUrl = result.optString("Poster");

        if ("movie".equals(type)&&browse==0) {
            Hyperlink titleLink = getMovieTitleLink(index);
            ImageView posterImageView = getMoviePosterImageView(index);

            updateMediaUIHelper(titleLink, posterImageView, title, posterUrl, result);
        } else if ("series".equals(type)&&browse==0) {
            Hyperlink titleLink = getSeriesTitleLink(index);
            ImageView posterImageView = getSeriesPosterImageView(index);

            updateMediaUIHelper(titleLink, posterImageView, title, posterUrl, result);
        }

        else if ("series".equals(type)&&browse==1) {
            Hyperlink titleLink = SeriesTitleLink(index);
            ImageView posterImageView = SeriesPosterImageView(index);

            updateMediaUIHelper(titleLink, posterImageView, title, posterUrl, result);
        }
        else if ("movie".equals(type)&&browse==1) {
            Hyperlink titleLink = MovieTitleLink(index);
            ImageView posterImageView = MoviePosterImageView(index);

            updateMediaUIHelper(titleLink, posterImageView, title, posterUrl, result);
        }
        else if ("series".equals(type)&&browse==2) {
            Hyperlink titleLink =getFavouriteSeriesTitleLink(index);
            ImageView posterImageView = getFavouriteSeriesPosterImageView(index);

            updateMediaUIHelper(titleLink, posterImageView, title, posterUrl, result);
        }
        else if ("movie".equals(type)&&browse==2) {
            Hyperlink titleLink =getFavouriteMovieTitleLink(index);
            ImageView posterImageView = getFavouriteMoviePosterImageView(index);

            updateMediaUIHelper(titleLink, posterImageView, title, posterUrl, result);
        }
    }
    private void updateMediaUI1(JSONObject result, int index) {
        String title = result.optString("Title");
        String posterUrl = result.optString("Poster");
        Hyperlink titleLink =RatedMovieTitleLink(index);
        ImageView posterImageView = RatedMoviePosterImageView(index);
        updateMediaUIHelper(titleLink, posterImageView, title, posterUrl, result);
    }

    private void updateMediaUIHelper(Hyperlink link, ImageView image, String title, String posterUrl, JSONObject result) {
        link.setText(title);
        link.setOnAction(event -> showDetails(result)); // Use correct method based on type

        if (!posterUrl.isEmpty() && !"N/A".equals(posterUrl)) {
            image.setImage(new Image(posterUrl));
        }
    }

    private Hyperlink getMovieTitleLink(int index) {
        switch (index) {
            case 0: return movieTitleHyperlink1;
            case 1: return movieTitleHyperlink2;
            case 2: return movieTitleHyperlink3;
            case 3: return movieTitleHyperlink4;
            default: return null;
        }
    }

    private ImageView getMoviePosterImageView(int index) {
        switch (index) {
            case 0: return moviePosterImageView1;
            case 1: return moviePosterImageView2;
            case 2: return moviePosterImageView3;
            case 3: return moviePosterImageView4;
            default: return null;
        }
    }

    private Hyperlink getSeriesTitleLink(int index) {
        switch (index) {
            case 0: return seriesTitleHyperlink1;
            case 1: return seriesTitleHyperlink2;
            case 2: return seriesTitleHyperlink3;
            case 3: return seriesTitleHyperlink4;
            default: return null;
        }
    }

    private ImageView getSeriesPosterImageView(int index) {
        switch (index) {
            case 0: return seriesPosterImageView1;
            case 1: return seriesPosterImageView2;
            case 2: return seriesPosterImageView3;
            case 3: return seriesPosterImageView4;
            default: return null;
        }
    }
    private Hyperlink RatedMovieTitleLink(int index) {
        switch (index) {
            case 0: return ratedMovieLink1;
            case 1: return ratedMovieLink2;
            case 2: return ratedMovieLink3;
            case 3: return ratedMovieLink1;
            default: return null;
        }
    }

    private ImageView RatedMoviePosterImageView(int index) {
        switch (index) {
            case 0: return ratedMovieImage1;
            case 1: return ratedMovieImage2;
            case 2: return ratedMovieImage3;
            case 3: return ratedMovieImage4;
            default: return null;
        }
    }


    @FXML
    private void logout(ActionEvent event) throws IOException {
        // Close the current window (logout)
        Parent root= FXMLLoader.load(getClass().getResource("/com/example/cinemawall/hello-view.fxml"));
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }


    public void showProfile(ActionEvent actionEvent) {
        this.browse=2;
        this.rate=1;
        mainContentAnchorPane.setVisible(false);
        mainContentAnchorPane.setVisible(false);
        MovieOrSeriesDetails.setVisible(false);
        watchlistAnchorPane.setVisible(false);
        AppLabel.setVisible(false);
        profileAnchorPane.setVisible(true);
        Sidebar.setVisible(true);
        browseMoreMoviesAnchorPane.setVisible(false);
        browseMoreShowsAnchorPane.setVisible(false);
        watchlistAnchorPane.setVisible(false);
        userNameLabel.setText(userID);
        List<String[]> movies = dbHelper.getLastAddedFavouriteMoviesByUser(userID);
        for (int i = 0; i < Math.min(movies.size(), 4); i++) {
            String title = movies.get(i)[0];
            String year = movies.get(i)[1];
            System.out.println(title+" "+year);
            fetchAndDisplayMovie(title, year, i);
        }
        List<String[]> series= dbHelper.getLastAddedFavouriteSeriesByUser(userID);
        for (int i = 0; i < Math.min(series.size(), 4); i++) {
            String title = series.get(i)[0];
            String year = series.get(i)[1];
            fetchAndDisplaySeries(title, year, i);
        }
        List<String[]> rated= dbHelper.getLastAddedRatedByUser(userID);
        for (int i = 0; i < Math.min(series.size(), 4); i++) {
            String title = rated.get(i)[0];
            String year = rated.get(i)[1];
            fetchAndDisplay(title, year, i);
        }



        ;
    }
    private JSONObject fetchFromOMDbW(String query, boolean isSearch) {
        String urlString = "http://www.omdbapi.com/?t=" + query + "&apikey=" + apiKey;
        final JSONObject[] resultContainer = new JSONObject[1];
        final CountDownLatch latch = new CountDownLatch(1);  // This will wait for 1 signal

        new Thread(() -> {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                resultContainer[0] = new JSONObject(response.toString());
                if (resultContainer[0].getString("Response").equals("True")) {
                    // Signal that the result is ready
                    latch.countDown();
                } else {
                    latch.countDown(); // In case of an error, signal immediately.
                }
            } catch (Exception e) {
                e.printStackTrace();
                latch.countDown(); // Signal in case of an error.
            }
        }).start();

        try {
            latch.await();  // Wait until the background thread finishes
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return resultContainer[0];  // Return the result after the background thread finishes
    }


    public void watchlist(ActionEvent actionEvent) {
        mainContentAnchorPane.setVisible(false);
        MovieOrSeriesDetails.setVisible(false);
        watchlistAnchorPane.setVisible(false);
        AppLabel.setVisible(false);
        profileAnchorPane.setVisible(false);
        Sidebar.setVisible(true);
        browseMoreMoviesAnchorPane.setVisible(false);
        browseMoreShowsAnchorPane.setVisible(false);
        watchlistAnchorPane.setVisible(true);
        populateWatchlist();
    }
    public void populateWatchlist() {
        // Assuming you have a method to fetch watchlist items from the database
        List<String[]> watchlist = dbHelper.getWatchlist();

        // Create a container for the GridPane to ensure it works properly with ScrollPane
        VBox container = new VBox();
        container.setStyle("-fx-padding: 10; -fx-background-color: #1e1e1e;");
        container.setSpacing(10);

        // Access the GridPane from FXML or create a new one
        GridPane watchlistGridPane = new GridPane();
        watchlistGridPane.setHgap(10); // Horizontal gap between items
        watchlistGridPane.setVgap(10); // Vertical gap between items
        watchlistGridPane.setPadding(new Insets(10)); // Padding around the grid
        watchlistGridPane.setAlignment(Pos.TOP_CENTER); // Center the grid

        container.getChildren().add(watchlistGridPane); // Add GridPane to container

        // Iterate over the watchlist and add items to the grid
        for (int i = 0; i < watchlist.size(); i++) {
            String title = watchlist.get(i)[0];
            String year = watchlist.get(i)[1];

            // Fetch details from OMDb in a separate thread
            final int index = i;
            new Thread(() -> {
                JSONObject result = fetchFromOMDbW(title, true); // Fetch from OMDb
                if (result != null) {
                    // Ensure the UI updates on the JavaFX thread
                    Platform.runLater(() -> {
                        addWatchlistItem(watchlistGridPane, result, index); // Add item to the grid
                    });
                }
            }).start();
        }

        // Set the container as the content for the ScrollPane
        watchlistScrollPane.setContent(container);
        watchlistScrollPane.setFitToWidth(true); // Ensure container fits horizontally
        watchlistScrollPane.setFitToHeight(false); // Allow vertical scrolling
        watchlistScrollPane.setPannable(true); // Allow panning
    }

    private void addWatchlistItem(GridPane gridPane, JSONObject result, int index) {
        // Create a VBox to hold each item (poster, title, year)
        VBox itemVBox = new VBox(5); // 5 is the spacing between the elements inside the VBox
        itemVBox.setStyle("-fx-background-color: #292929; -fx-padding: 10; -fx-alignment: center;");

        // Get the necessary information from the JSON response
        String title = result.optString("Title", "Unknown Title");
        String year = result.optString("Year", "Unknown Year");
        String posterUrl = result.optString("Poster", "");

        // Create an ImageView for the poster
        ImageView posterImageView = new ImageView();
        if (posterUrl != null && !posterUrl.isEmpty() && !"N/A".equals(posterUrl)) {
            posterImageView.setImage(new Image(posterUrl));
            posterImageView.setFitWidth(120); // Adjust the size of the poster image
            posterImageView.setFitHeight(200);
            posterImageView.setPreserveRatio(true); // Keep the aspect ratio
        }

        // Create a hyperlink for the title
        Hyperlink titleLink = new Hyperlink(title);
        titleLink.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");

        // Action when the title hyperlink is clicked
        titleLink.setOnAction(event -> {
            System.out.println("Clicked on: " + title);
            showDetails(result); // Navigate to details
        });

        // Create a label for the year
        Label yearLabel = new Label(year);
        yearLabel.setStyle("-fx-text-fill: white; -fx-font-size: 12px;");

        // Add the poster image, title (hyperlink), and year to the VBox
        itemVBox.getChildren().addAll(posterImageView, titleLink, yearLabel);

        // Add the VBox to the GridPane
        int row = index / 5; // Create a new row every 5 items
        int col = index % 5; // Add items in columns
        gridPane.add(itemVBox, col, row);
    }




    public void browseMoreMovies(ActionEvent actionEvent) {
        this.browse = 1;
        mainContentAnchorPane.setVisible(false);
        MovieOrSeriesDetails.setVisible(false);
        watchlistAnchorPane.setVisible(false);
        AppLabel.setVisible(false);
        profileAnchorPane.setVisible(false);
        Sidebar.setVisible(true);
        browseMoreMoviesAnchorPane.setVisible(true);
        browseMoreShowsAnchorPane.setVisible(false);
        List<String[]> movies = dbHelper.getLastAddedMovies(15);

//       GridPane browseMoviesGridPane=new GridPane();
        for (int i = 0; i <Math.min(movies.size(),15); i++) {
            String title = movies.get(i)[0];
            String year = movies.get(i)[1];
            System.out.println(title+" "+year);
            fetchAndDisplayMovie(title, year, i);
        }
//        browseMoreMoviesScrollPane.setContent(browseMoviesGridPane);
//
//        // Add the ScrollPane to the AnchorPane
//        browseMoreMoviesAnchorPane.getChildren().clear();  // Clear previous content if any
//        browseMoreMoviesAnchorPane.getChildren().add(browseMoreMoviesScrollPane);

    }
    public void browseMoreSeries(ActionEvent actionEvent) {
        this.browse=1;
        mainContentAnchorPane.setVisible(false);
        MovieOrSeriesDetails.setVisible(false);
        watchlistAnchorPane.setVisible(false);
        AppLabel.setVisible(false);
        profileAnchorPane.setVisible(false);
        Sidebar.setVisible(true);
        browseMoreMoviesAnchorPane.setVisible(false);
        browseMoreShowsAnchorPane.setVisible(true);
//        GridPane browseShowsGridPane = (GridPane) browseMoreShowsAnchorPane.lookup("#browseShowsGridPane");
//        browseShowsGridPane.getChildren().clear();
        List<String[]> series= dbHelper.getLastAddedSeries(15);
        for (int i = 0; i < Math.min(series.size(),15); i++) {
            String title = series.get(i)[0];
            String year = series.get(i)[1];
            fetchAndDisplaySeries(title, year, i);
        }


    }


    private Hyperlink MovieTitleLink(int index) {
        switch (index) {
            case 0: return filmTitleHyperlink1;
            case 1: return filmTitleHyperlink2;
            case 2: return filmTitleHyperlink3;
            case 3: return filmTitleHyperlink4;
            case 4: return filmTitleHyperlink5;
            case 5: return filmTitleHyperlink6;
            case 6: return filmTitleHyperlink7;
            case 7: return filmTitleHyperlink8;
            case 8: return filmTitleHyperlink9;
            case 9: return filmTitleHyperlink10;
            case 10: return filmTitleHyperlink11;
            case 11: return filmTitleHyperlink12;
            case 12: return filmTitleHyperlink13;
            case 13: return filmTitleHyperlink14;
            case 14: return filmTitleHyperlink15;
            default: return null;
        }
    }

    private ImageView MoviePosterImageView(int index) {
        switch (index) {
            case 0: return filmPosterImageView1;
            case 1: return filmPosterImageView2;
            case 2: return filmPosterImageView3;
            case 3: return filmPosterImageView4;
            case 4: return filmPosterImageView5;
            case 5: return filmPosterImageView6;
            case 6: return filmPosterImageView7;
            case 7: return filmPosterImageView8;
            case 8: return filmPosterImageView9;
            case 9: return filmPosterImageView10;
            case 10: return filmPosterImageView11;
            case 11: return filmPosterImageView12;
            case 12: return filmPosterImageView13;
            case 13: return filmPosterImageView14;
            case 14: return filmPosterImageView15;
            default: return null;
        }
    }
    private Hyperlink SeriesTitleLink(int index) {
        switch (index) {
            case 0: return showsTitleHyperlink1;
            case 1: return showsTitleHyperlink2;
            case 2: return showsTitleHyperlink3;
            case 3: return showsTitleHyperlink4;
            case 4: return showsTitleHyperlink5;
            case 5: return showsTitleHyperlink6;
            case 6: return showsTitleHyperlink7;
            case 7: return showsTitleHyperlink8;
            case 8: return showsTitleHyperlink9;
            case 9: return showsTitleHyperlink10;
            case 10: return showsTitleHyperlink11;
            case 11: return showsTitleHyperlink12;
            case 12: return showsTitleHyperlink13;
            case 13: return showsTitleHyperlink14;
            case 14: return showsTitleHyperlink15;


            default: return null;
        }
    }
    private ImageView SeriesPosterImageView(int index) {
        switch (index) {
            case 0: return showsPosterImageView1;
            case 1: return showsPosterImageView2;
            case 2: return showsPosterImageView3;
            case 3: return showsPosterImageView4;
            case 4: return showsPosterImageView5;
            case 5: return showsPosterImageView6;
            case 6: return showsPosterImageView7;
            case 7: return showsPosterImageView8;
            case 8: return showsPosterImageView9;
            case 9: return showsPosterImageView10;
            case 10: return showsPosterImageView11;
            case 11: return showsPosterImageView12;
            case 12: return showsPosterImageView13;
            case 13: return showsPosterImageView14;
            case 14: return showsPosterImageView15;
            default: return null;
        }
    }
    private Hyperlink getFavouriteMovieTitleLink(int index) {
        switch (index) {
            case 0: return favMovieLink1;
            case 1: return favMovieLink2;
            case 2: return favMovieLink3;
            case 3: return favMovieLink4;
            default: return null;
        }
    }

    private ImageView getFavouriteMoviePosterImageView(int index) {
        switch (index) {
            case 0: return favMovieImage1;
            case 1: return favMovieImage2;
            case 2: return favMovieImage3;
            case 3: return favMovieImage4;
            default: return null;
        }
    }


    private Hyperlink getFavouriteSeriesTitleLink(int index) {
        switch (index) {
            case 0: return favSeriesLink1;
            case 1: return favSeriesLink2;
            case 2: return favSeriesLink3;
            case 3: return favSeriesLink4;
            default: return null;
        }
    }
    private ImageView getFavouriteSeriesPosterImageView(int index) {
        switch (index) {
            case 0: return favSeriesImage1;
            case 1: return favSeriesImage2;
            case 2: return favSeriesImage3;
            case 3: return favSeriesImage4;
            default: return null;
        }
    }
    public void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}

