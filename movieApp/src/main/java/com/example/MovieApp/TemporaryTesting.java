package com.example.MovieApp;

import java.util.List;

public class TemporaryTesting {
    public static void main(String[] args) {
       List<Movie> movies = APIUtility.getMoviesFromFile("movies.json");
        System.out.println(movies);
    }
}
