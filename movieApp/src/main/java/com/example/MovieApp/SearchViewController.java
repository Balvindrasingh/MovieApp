package com.example.MovieApp;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.TreeSet;

public class SearchViewController {

    @FXML
    private Button fetchAllButton;

    @FXML
    private ListView<Movie> listView;

    @FXML
    private Label msgLabel;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private HBox resultsBox;

    @FXML
    private Label resultsMsgLabel;

    @FXML
    private TextField searchTextField;

    @FXML
    private VBox selectedVBox;

    @FXML
    private VBox titlesVBox;

    private int page, totalNumOfMovies;

    @FXML
    private void initialize() {
        progressBar.setVisible(false);
        selectedVBox.setVisible(false);
        titlesVBox.setVisible(false);
        msgLabel.setVisible(false);

        listView.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldValue, movieSelected) -> {
                    if (movieSelected != null) {
                        selectedVBox.setVisible(true);
                    } else {
                        selectedVBox.setVisible(false);
                    }
                });
    }

    @FXML
    private void searchForMovies(ActionEvent event) throws IOException, InterruptedException {
        page = 1;
        String movieName = searchTextField.getText().trim();
        ApiResponse apiResponse = APIUtility.callAPI(movieName, page);
        totalNumOfMovies = Integer.parseInt(apiResponse.getTotalResults());

        if (apiResponse.getMovies() != null) {
            titlesVBox.setVisible(true);
            listView.getItems().clear();
            listView.getItems().addAll(apiResponse.getMovies());

            TreeSet<Movie> sortedMovieSet = new TreeSet<>();
            sortedMovieSet.addAll(listView.getItems());
            System.out.println(Integer.toString(sortedMovieSet.size()) + sortedMovieSet);

            updateLabels();
        } else {
            titlesVBox.setVisible(false);
            msgLabel.setVisible(true);
            msgLabel.setText("Enter a movie title and click \"Search\"");
        }
    }

    private void updateLabels() {
        resultsMsgLabel.setText("Showing " + listView.getItems().size() + " of " + totalNumOfMovies);
        if (listView.getItems().size() < totalNumOfMovies)
            fetchAllButton.setVisible(true);
        else
            fetchAllButton.setVisible(false);
    }

    @FXML
    void getMovieDetails(ActionEvent event) throws IOException {
        Movie movieSelected = listView.getSelectionModel().getSelectedItem();
        SceneChanger.changeScenes(event, "info-view.fxml", movieSelected.getImdbID());
    }

    @FXML
    void fetchAllMovies() {
        Thread fetchThread = new Thread(() -> {
            progressBar.setVisible(true);
            page++;
            try {
                ApiResponse apiResponse = APIUtility.callAPI(searchTextField.getText().trim(), page);
                listView.getItems().addAll(apiResponse.getMovies());

                Platform.runLater(() -> {
                    updateLabels();
                    progressBar.setProgress((double) listView.getItems().size() / totalNumOfMovies);
                });

            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (listView.getItems().size() < totalNumOfMovies)
                fetchAllMovies();
            else
                progressBar.setVisible(false);
        });
        fetchThread.start();
    }
}
