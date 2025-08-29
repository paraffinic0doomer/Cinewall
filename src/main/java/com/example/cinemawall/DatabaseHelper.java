package com.example.cinemawall;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.DriverManager.getConnection;

public class DatabaseHelper {
    private Connection connection;
    private String userID;

    public void setUserID(String userID) {
        this.userID = userID;

    }

    public DatabaseHelper() {
        try {
            connection = getConnection("jdbc:mysql://localhost:3306/cinema", "root", "1234");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void addOrUpdateMovie(String userID, String title, String year) {
        String checkExistenceQuery = "SELECT 1 FROM searchedMovies WHERE MovieTitle = ? AND UserID = ?";
        String deleteQuery = "DELETE FROM searchedMovies WHERE MovieTitle = ? AND UserID = ?";
        String insertQuery = "INSERT INTO searchedMovies (UserID, MovieTitle, Year) VALUES (?, ?, ?)";
        String deleteMoviesQuery = "DELETE FROM movies WHERE MovieTitle = ? AND Year = ?";
        String insertMoviesQuery = "INSERT INTO movies (MovieTitle, Year) VALUES (?, ?)";

        try (Connection conn = getConnection("jdbc:mysql://localhost:3306/cinema", "root", "1234");
             PreparedStatement checkExistenceStmt = conn.prepareStatement(checkExistenceQuery);
             PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery);
             PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
             PreparedStatement deleteMoviesStmt = conn.prepareStatement(deleteMoviesQuery);
             PreparedStatement insertMoviesStmt = conn.prepareStatement(insertMoviesQuery)) {

            // Check if the record already exists in `searchedMovies`
            checkExistenceStmt.setString(1, title);
            checkExistenceStmt.setString(2, userID);
            ResultSet rs = checkExistenceStmt.executeQuery();

            if (rs.next()) {
                // Delete old record from `searchedMovies`
                deleteStmt.setString(1, title);
                deleteStmt.setString(2, userID);
                deleteStmt.executeUpdate();
            }

            // Insert the new record into `searchedMovies`
            insertStmt.setString(1, userID);
            insertStmt.setString(2, title);
            insertStmt.setString(3, year);
            insertStmt.executeUpdate();

            // Always delete and reinsert in `movies` table
            deleteMoviesStmt.setString(1, title);
            deleteMoviesStmt.setString(2, year);
            deleteMoviesStmt.executeUpdate();

            insertMoviesStmt.setString(1, title);
            insertMoviesStmt.setString(2, year);
            insertMoviesStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void addOrUpdateSeries(String userID, String title, String year) {
        String checkExistenceQuery = "SELECT 1 FROM searchedSeries WHERE SeriesTitle = ? AND UserID = ?";
        String deleteQuery = "DELETE FROM searchedSeries WHERE SeriesTitle = ? AND UserID = ?";
        String insertQuery = "INSERT INTO searchedSeries (UserID, SeriesTitle, Year) VALUES (?, ?, ?)";
        String deleteSeriesQuery = "DELETE FROM series WHERE SeriesTitle = ? AND Year = ?";
        String insertSeriesQuery = "INSERT INTO series (SeriesTitle, Year) VALUES (?, ?)";

        try (Connection conn = getConnection("jdbc:mysql://localhost:3306/cinema", "root", "1234");
             PreparedStatement checkExistenceStmt = conn.prepareStatement(checkExistenceQuery);
             PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery);
             PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
             PreparedStatement deleteSeriesStmt = conn.prepareStatement(deleteSeriesQuery);
             PreparedStatement insertSeriesStmt = conn.prepareStatement(insertSeriesQuery)) {

            // Check if the record already exists in `searchedSeries`
            checkExistenceStmt.setString(1, title);
            checkExistenceStmt.setString(2, userID);
            ResultSet rs = checkExistenceStmt.executeQuery();

            if (rs.next()) {
                // Delete old record from `searchedSeries`
                deleteStmt.setString(1, title);
                deleteStmt.setString(2, userID);
                deleteStmt.executeUpdate();
            }

            // Insert the new record into `searchedSeries`
            insertStmt.setString(1, userID);
            insertStmt.setString(2, title);
            insertStmt.setString(3, year);
            insertStmt.executeUpdate();

            // Always delete and reinsert in `series` table
            deleteSeriesStmt.setString(1, title);
            deleteSeriesStmt.setString(2, year);
            deleteSeriesStmt.executeUpdate();

            insertSeriesStmt.setString(1, title);
            insertSeriesStmt.setString(2, year);
            insertSeriesStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    // Method to get last added movies by a specific user
    public List<String[]> getLastAddedMoviesByUser(String userID) {
        // Query to get last 4 added movies for a specific user
        List<String[]> movies = new ArrayList<>();
        String query = "SELECT MovieTitle, Year FROM cinema.searchedmovies WHERE userID = ? ORDER BY id DESC LIMIT 4";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, userID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String[] movie = new String[2];
                movie[0] = rs.getString("MovieTitle");
                movie[1] = rs.getString("Year");
                movies.add(movie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }

    // Method to get last added series by a specific user
    public List<String[]> getLastAddedSeriesByUser(String userID) {
        // Query to get last 4 added series for a specific user
        List<String[]> series = new ArrayList<>();
        String query = "SELECT SeriesTitle, Year FROM searchedSeries WHERE userID = ? ORDER BY id DESC LIMIT 4";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, userID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String[] show = new String[2];
                show[0] = rs.getString("SeriesTitle");
                show[1] = rs.getString("Year");
                series.add(show);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return series;
    }
    public void deletePrevious(String userID, String title, String type) {
        String query = "";
        if ("movie".equals(type)) {
            query = "DELETE FROM searchedMovies WHERE userID = ? AND MovieTitle = ?";
        } else if ("series".equals(type)) {
            query = "DELETE FROM searchedSeries WHERE userID = ? AND SeriesTitle = ?";
        }

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, userID);
            ps.setString(2, title);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public boolean checkIfExists(String userID, String title, String type) {
        String query = "";
        if ("movie".equals(type)) {
            query = "SELECT COUNT(*) FROM searchedMovies WHERE userID = ? AND MovieTitle = ?";
        } else if ("series".equals(type)) {
            query = "SELECT COUNT(*) FROM searchedSeries WHERE userID = ? AND SeriesTitle = ?";
        }

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, userID);
            ps.setString(2, title);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;  // Return true if the title exists, false otherwise
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Default return false if something goes wrong
    }
    public List<String[]> getLastAddedMovies(int limit) {
        String query = "SELECT MovieTitle, Year FROM cinema.movies ORDER BY id DESC LIMIT ?";
        List<String[]> results = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, limit); // Use limit as the first parameter for the query
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                results.add(new String[]{rs.getString("MovieTitle"), rs.getString("Year")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }


    public List<String[]> getLastAddedSeries(int limit) {
        String query = "SELECT SeriesTitle, Year FROM cinema.series ORDER BY id DESC LIMIT ?";
        List<String[]> results = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, limit); // Use limit as the first parameter for the query
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                results.add(new String[]{rs.getString("SeriesTitle"), rs.getString("Year")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }
    public List<String[]> getLastAddedFavouriteMoviesByUser(String userID) {
        // Query to get last 4 added movies for a specific user
        List<String[]> movies = new ArrayList<>();
        String query = "SELECT MovieTitle, Year FROM favouritemovies WHERE userID = ? ORDER BY id DESC LIMIT 4";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, userID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String[] movie = new String[2];
                movie[0] = rs.getString("MovieTitle");
                movie[1] = rs.getString("Year");
                movies.add(movie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }
    public List<String[]> getLastAddedFavouriteSeriesByUser(String userID) {
        // Query to get last 4 added series for a specific user
        List<String[]> series = new ArrayList<>();
        String query = "SELECT SeriesTitle, Year FROM favouriteseries WHERE userID = ? ORDER BY id DESC LIMIT 4";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, userID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String[] show = new String[2];
                show[0] = rs.getString("SeriesTitle");
                show[1] = rs.getString("Year");
                series.add(show);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return series;
    }
    public void addOrUpdateFavouriteMovie(String userID, String title, String year) {
        String deleteQuery = "DELETE FROM favouritemovies WHERE MovieTitle = ? AND UserID = ?";
        String insertQuery = "INSERT INTO favouritemovies (UserID, MovieTitle, Year) VALUES (?, ?, ?)";

        try (Connection conn = getConnection("jdbc:mysql://localhost:3306/cinema", "root", "1234");
             PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery);
             PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {

            // Delete previous entry
            deleteStmt.setString(1, title);
            deleteStmt.setString(2, userID);
            deleteStmt.executeUpdate();

            // Insert new entry
            insertStmt.setString(1, userID);
            insertStmt.setString(2, title);
            insertStmt.setString(3, year);
            insertStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void addOrUpdateFavouriteSeries(String userID, String title, String year) {
        String deleteQuery = "DELETE FROM favouriteseries WHERE SeriesTitle = ? AND userID = ?";
        String insertQuery = "INSERT INTO favouriteseries (userID, SeriesTitle, Year) VALUES (?, ?, ?)";

        try (Connection conn = getConnection("jdbc:mysql://localhost:3306/cinema", "root", "1234");
             PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery);
             PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {

            // Delete previous entry
            deleteStmt.setString(1, title);
            deleteStmt.setString(2, userID);
            deleteStmt.executeUpdate();

            // Insert new entry
            insertStmt.setString(1, userID);
            insertStmt.setString(2, title);
            insertStmt.setString(3, year);
            insertStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<String[]> getWatchlist() {
        List<String[]> watchlist = new ArrayList<>();
        String query = "SELECT Title, Year FROM watchlist WHERE userID = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, userID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String[] show = new String[2];
                show[0] = rs.getString("Title");
                show[1] = rs.getString("Year");
                watchlist.add(show);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return watchlist;
    }

    // Check if the movie or series already exists in the watchlist
    public boolean isItemInWatchlist(String title, String userId) {
        String query = "SELECT COUNT(*) FROM watchlist WHERE Title = ? AND userID = ?";
        try (Connection conn = getConnection("jdbc:mysql://localhost:3306/cinema", "root", "1234");
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, userId);
            stmt.setString(2, title);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Add a new item to the watchlist
    public void addToWatchlist(String userId, String title,  String year) {
        String query = "INSERT INTO watchlist ( userID,Title, Year) VALUES (?, ?, ?)";
        try (Connection conn = getConnection("jdbc:mysql://localhost:3306/cinema", "root", "1234");
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, userId);
            stmt.setString(2, title);
            stmt.setString(3, year);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public double isRated(String userID, String title) {
        // SQL query to check if the user has rated this movie/series
        String query = "SELECT Rating FROM rating WHERE userID = ? AND Title = ?";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "1234");
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set parameters for the query
            preparedStatement.setString(1, userID);
            preparedStatement.setString(2, title);

            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Check if a result is returned
            if (resultSet.next()) {
                // Return the rating if found
                return resultSet.getDouble("Rating");
            } else {
                // Return -1 or some other value indicating no rating
                return -1;
            }

        } catch (SQLException e) {
            // Handle any SQL exceptions
            e.printStackTrace();
            return -1; // Return -1 if there was an error
        }
    }


    public void addOrUpdateRating(String userID, String title, String year, double newValue) {
        String query = "SELECT * FROM rating WHERE userID = ? AND Title = ?";
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "1234");
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            connection.setAutoCommit(false);  // Start a transaction

            preparedStatement.setString(1, userID);
            preparedStatement.setString(2, title);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Delete the existing rating from the 'rating' table
                String deleteRatingQuery = "DELETE FROM rating WHERE userID = ? AND Title = ?";
                try (PreparedStatement deleteStatement = connection.prepareStatement(deleteRatingQuery)) {
                    deleteStatement.setString(1, userID);
                    deleteStatement.setString(2, title);
                    deleteStatement.executeUpdate();
                }
                // Optionally delete from the 'watchlist' table if required
                String deleteWatchlistQuery = "DELETE FROM watchlist WHERE userID = ? AND Title = ?";
                try (PreparedStatement deleteWatchlistStatement = connection.prepareStatement(deleteWatchlistQuery)) {
                    deleteWatchlistStatement.setString(1, userID);
                    deleteWatchlistStatement.setString(2, title);
                    deleteWatchlistStatement.executeUpdate();
                }
            }

            // Add the new rating
            String insertQuery = "INSERT INTO rating (userID, Title, Year, Rating) VALUES (?, ?, ?, ?)";
            try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                insertStatement.setString(1, userID);
                insertStatement.setString(2, title);
                insertStatement.setString(3, year);
                insertStatement.setDouble(4, newValue);
                insertStatement.executeUpdate();
            }

            connection.commit();  // Commit the transaction
            System.out.println("New rating added for " + title);
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();  // Rollback in case of an error
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }


    public double totalRating(String title, String year) {
        double totalRating = 0;
        double ratingCount = 0;

        // Query the database to get the ratings for the specified title and year
        String query = "SELECT rating FROM rating WHERE Title = ? AND Year = ?";

        try (Connection conn = getConnection("jdbc:mysql://localhost:3306/cinema","root","1234");
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, title);
            stmt.setString(2, year);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    totalRating += rs.getDouble("rating");  // Sum all the ratings
                    ratingCount++;
                }
            }

            // If there are any ratings, return the average
            if (ratingCount > 0) {
                return totalRating / ratingCount;  // Integer division to get the average rating
            } else {
                return 0;  // Return 0 if no ratings are found
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;  // Return 0 in case of error
        }
    }


    public List<String[]> getLastAddedRatedByUser(String userID) {
        List<String[]> series = new ArrayList<>();
        String query = "SELECT Title, Year FROM rating WHERE userID = ? ORDER BY id DESC LIMIT 4";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, userID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String[] show = new String[2];
                show[0] = rs.getString("Title");
                show[1] = rs.getString("Year");
                series.add(show);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return series;
    }
}
