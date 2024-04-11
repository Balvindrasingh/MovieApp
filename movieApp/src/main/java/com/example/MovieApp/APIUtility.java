package com.example.MovieApp;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;

public class APIUtility {

    /**
     * This method will call the OMDB API with a movie title passed in as an argument
     */
    public static ApiResponse callAPI(String movieName, int page) throws IOException, InterruptedException {
        //if we received "Star Wars", we need to translate that to be "Star%20Wars"
        movieName = movieName.replaceAll(" ", "%20");

        String uri = "http://www.omdbapi.com/?apikey=5993c070&s=" + movieName + "&page=" + page;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .build();

        HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        Gson gson = new Gson();
        return gson.fromJson(httpResponse.body(), ApiResponse.class);
    }

    public static MovieDetails getMovieDetails(String imdbID) throws IOException, InterruptedException {
        //if we received "Star Wars", we need to translate that to be "Star%20Wars"
        imdbID = imdbID.trim().replaceAll(" ", "%20");

        String uri = "http://www.omdbapi.com/?apikey=5993c070&i=" + imdbID;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .build();

        HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        Gson gson = new Gson();
        return gson.fromJson(httpResponse.body(), MovieDetails.class);
    }

    public static List<Movie> getMoviesFromFile(String fileName) {
        Gson gson = new Gson();

        try (
                FileReader fileReader = new FileReader(fileName);
                JsonReader jsonReader = new JsonReader(fileReader)
        ) {
            Movie[] movies = gson.fromJson(jsonReader, Movie[].class);
            return Arrays.asList(movies);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
