package com.example.javamusicplayer;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class HelloController implements Initializable {


    private Parent root;

    private Scene scene;

    private Stage stage;

    private String path;

    private MediaPlayer mediaPlayer;

    @FXML
    private MediaView mediaView;

    @FXML
    private TextArea songName;

    @FXML
    private Slider volumeSlider;

    @FXML
    private Slider progressBar;

    @FXML
    private TextArea songInfo;

    private String nameOfSong;

    private Media media;

    private char array[];

    private String status;

    List<Path> pathList;

    @FXML
    private CheckBox checkBox;

    private Integer btnNext=0;

    private Integer btnPrevious;

    private AudioClip audioClip;

    @FXML
    private Canvas canvas;

    private Draw draw;

    GraphicsContext gc;

    @FXML
    ListView<String> listView;









    public void chooseFile(ActionEvent actionEvent) {
        System.out.println("selected");
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        path=selectedFile.toURI().toString();
        System.out.println(path);
        if(path!=null){
            media = new Media(path);
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);
            songName.setText(selectedFile.getName());

            songInfo.setText(String.valueOf(mediaPlayer.getAudioSpectrumNumBands() + " kbps"));

            currentTimeProperty();


//            progressBar.setOnMousePressed(new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent mouseEvent) {
//                    mediaPlayer.seek(Duration.seconds(progressBar.getValue()));
//                }
//            });
//
//            progressBar.setOnMouseDragged(new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent mouseEvent) {
//                    mediaPlayer.seek(Duration.seconds(progressBar.getValue()));
//                }
//            });
//
            mediaPlayer.setOnReady(new Runnable() {
                @Override
                public void run() {
                    javafx.util.Duration total = media.getDuration();
                    progressBar.setMax(total.toSeconds());

                }
            });




            mediaPlayer.play();


        }
    }




    public void playFolder(ActionEvent event){
        if(mediaPlayer!=null)
            mediaPlayer.stop();
        System.out.println("Play folder kliknuto");
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File directory = directoryChooser.showDialog(null);
        if(directory!=null){
            Path path = Paths.get(directory.getPath());

            try (Stream<Path> paths= Files.walk(path)){
                pathList=paths.collect(Collectors.toList());
                pathList.remove(0);
                pathList.forEach(System.out::println);


            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            media=new Media(pathList.get(0).toUri().toString());

            mediaPlayer = new MediaPlayer(media);


            currentTimeProperty();
            songName.setText(pathList.get(0).getFileName().toString());


            mediaPlayer.play();

            mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observableValue, Duration duration, Duration t1) {
                    mediaPlayer.setOnEndOfMedia(new Runnable() {
                        @Override
                        public void run() {
                            //daodti kod kad se muzika prekine da krene sledeca pesma
                        }
                    });
                }
            });

        }

    }

    private void currentTimeProperty() {
        mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observableValue, Duration duration, Duration t1) {
                progressBar.setValue(t1.toSeconds());
            }
        });

        volumeSlider.setValue(mediaPlayer.getVolume() * 100);
        volumeSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                mediaPlayer.setVolume(volumeSlider.getValue()/100);
                songInfo.setText(mediaPlayer.getAudioSpectrumNumBands() + " kbps" + " Volume: " + (int)volumeSlider.getValue() + "%");

            }
        });
    }

    public void onPlayNext(ActionEvent event){//srediti ovo ako se ode preko velicine niza da krene ponovo
        if(btnNext<=0)
            btnNext=0;
        if(btnNext>=pathList.size())
            btnNext=0;
        mediaPlayer.stop();

        btnNext++;
        media=new Media(pathList.get(btnNext).toUri().toString());
        mediaPlayer=new MediaPlayer(media);
        progressBar.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                mediaPlayer.seek(Duration.seconds(progressBar.getValue()));
            }
        });

        progressBar.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                mediaPlayer.seek(Duration.seconds(progressBar.getValue()));
            }
        });
        currentTimeProperty();
        songName.setText(pathList.get(btnNext).getFileName().toString());
        mediaPlayer.play();


    }

    public void playVisual(){
        if(mediaPlayer != null) {
            draw.Audio(gc, mediaPlayer);


//            mediaPlayer.setOnEndOfMedia(new Runnable() {
//                @Override
//                public void run() {
//
//                }
//            });
        }

    }

    public void onPlayPrevious(ActionEvent event){
        mediaPlayer.stop();
        btnNext--;
        if(btnNext<=0)
            btnNext=0;
        media=new Media(pathList.get(btnNext).toUri().toString());
        mediaPlayer=new MediaPlayer(media);
        progressBar.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                mediaPlayer.seek(Duration.seconds(progressBar.getValue()));
            }
        });

        progressBar.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                mediaPlayer.seek(Duration.seconds(progressBar.getValue()));
            }
        });
        currentTimeProperty();
        songName.setText(pathList.get(btnNext).getFileName().toString());
        mediaPlayer.play();

    }

    public void isLoopOn(ActionEvent event){


        checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                //System.out.println(aBoolean);
                System.out.println(t1);
                if(t1){
                    mediaPlayer.setOnEndOfMedia(new Runnable() {
                        @Override
                        public void run() {
                            mediaPlayer.seek(Duration.ZERO);
                            mediaPlayer.play();
                        }
                    });

                }
            }
        });




    }



    public void onPlayUrl(ActionEvent event) throws IOException {
        try {
            if (audioClip.isPlaying())
                audioClip.stop();
        }catch (NullPointerException e){

        }
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setTitle("Enter URL");
        textInputDialog.getDialogPane().setContentText("Enter URL: ");
        Optional<String> url = textInputDialog.showAndWait();
        TextField input = textInputDialog.getEditor();
        System.out.println(url.get());
        audioClip = new AudioClip(url.get());
        audioClip.play();

       
    }

    public void onPause(ActionEvent event){
        mediaPlayer.pause();

    }

    public void onPlay(ActionEvent event){
        mediaPlayer.play();
    }

    public void onStop(ActionEvent event){
        mediaPlayer.stop();
    }


    public void minusTen(ActionEvent event){
        mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(10)));
    }

    public void plusTen(ActionEvent event){
        mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(-10)));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        draw = new Draw();
        gc=canvas.getGraphicsContext2D();
        Globals.canvasSizeX = (int)canvas.getWidth();
        Globals.canvasSizeY = (int)canvas.getHeight();

        Globals.PlayedSlider = progressBar;

        GraphicsContext graphicsContext= canvas.getGraphicsContext2D();
        graphicsContext.setFill(Color.RED);




        progressBar.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                mediaPlayer.seek(Duration.seconds(progressBar.getValue()));
            }
        });

        progressBar.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                mediaPlayer.seek(Duration.seconds(progressBar.getValue()));
            }
        });



    }
}