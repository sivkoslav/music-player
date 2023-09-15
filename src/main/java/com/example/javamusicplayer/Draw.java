package com.example.javamusicplayer;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.util.Duration;

import java.text.DecimalFormat;

public class Draw {

    GraphicsContext gc;
    private Double PlayedTime=.0;
    private int bands=0;
    private float previousBand=0, smooth;
    private double minutes=0, seconds=0;

    public double Audio(GraphicsContext gc, MediaPlayer player) { //this is where we get all the data and send it to other functions
        this.gc = gc;
        player.setAudioSpectrumNumBands(512);
        bands = player.getAudioSpectrumNumBands();

        player.setAudioSpectrumListener(new AudioSpectrumListener() {
            @Override
            public void spectrumDataUpdate(double timestamp, double duration, float[] magnitudes, float[] phases) {
                gc.clearRect(0, 0, Globals.canvasSizeX, Globals.canvasSizeY);
                PlayedTime = .0;
                PlayedTime += timestamp;

                time(player);

                int width = (int) Screen.getPrimary().getBounds().getWidth()/bands;
                float[] correctedMagnitude = new float[magnitudes.length];

                for (int i = 0; i < bands; i++) {
                    correctedMagnitude[i] = magnitudes[i] - player.getAudioSpectrumThreshold();

                    smooth = (correctedMagnitude[i] + previousBand)/2;

                    list(Globals.Selected, i, (i * width+1), Globals.canvasSizeY, width, (int)(smooth * 10));
                    previousBand = correctedMagnitude[i];
                }
            }
        });
        return 0;
    }

    private void time(MediaPlayer player) { //This is where the controls for the playback slider are and where it shows the duration
        //add listeners
        Globals.PlayedSlider.valueProperty().addListener(new InvalidationListener() { //listener for video jump using slider
            public void invalidated(Observable arg0) {
                if(Globals.PlayedSlider.isPressed())
                    player.seek(player.getMedia().getDuration().multiply(Globals.PlayedSlider.getValue()/100));
            }
        });

        player.currentTimeProperty().addListener(new InvalidationListener() { //updating video slider according to time
            public void invalidated(Observable observable) {
                Platform.runLater(new Runnable() {
                    public void run() {
                        Globals.PlayedSlider.setValue(player.getCurrentTime().toMillis()/player.getTotalDuration().toMillis()*100);
                    }
                });
            }
        });
        Duration currentTime = player.getCurrentTime();
        Duration duration = player.getMedia().getDuration();

        //Globals.PlayedText.setText(formatTime(currentTime));
        //Globals.DurationText.setText(formatTime(duration));
    }
    private void list(int select, int i, int x, int y, int w, int h) {
        switch(select) {
            case 0: inHeight(i, x, y, w, h);
                break;
            case 1: inHeightTwice(i, x, y, w, h);
                break;
            case 2: inPosition(i, x, y, w, h);
                break;
            case 3: inRadius(i, x, y, w, h);
                break;
            case 4: inDisks(i, x, y, w, h);
                break;
            case 5: inArc(i, x, y, w, h);
                break;
        }
    }














    public void inHeight(int i, int x, int y, int w, double h) {
        double amplitude;

        amplitude = y - h;

        DecimalFormat df = new DecimalFormat("#.00");

        double bandColor = Double.parseDouble(df.format(i * 0.00196));

        gc.setFill(Color.color(1, bandColor, 0));
        gc.fillRect(x, amplitude, w, h);
    }
    public void inHeightTwice(int i, int x, int y, int w, double h) {
        double amplitude;

        amplitude = (((y - 300) - (h/2)) * 0.1) * 10;

        DecimalFormat df = new DecimalFormat("#.00");

        double bandColor = Double.parseDouble(df.format(i * 0.00196));

        gc.setFill(Color.color(1, bandColor, 0));
        gc.fillRect(x, amplitude, w, h);
    }
    public void inPosition(int i, int x, int y, int w, int h) {
        double amplitude;
        int rectHeight = 80;
        amplitude = (y - rectHeight) - h;

        DecimalFormat df = new DecimalFormat("#.00");

        double bandColor = Double.parseDouble(df.format(i * 0.00196));

        gc.setFill(Color.color(1, bandColor, 0));

        gc.fillRect(x, amplitude + i*0.1, w, rectHeight);
    }
    public void inRadius(int i, int xPos, int yPos, int w, int h) {
        float r = bands-80;
        float x = (float) (r * Math.cos(i));
        float y = (float) ((r-50) * Math.sin(i)/2);

        double centerX = x + Globals.primaryStage.getWidth()/2;
        double centerY = y + Globals.primaryStage.getHeight()/3;

        DecimalFormat df = new DecimalFormat("#.00");
        double bandColor = Double.parseDouble(df.format(i * 0.00196));

        gc.setFill(Color.color(1, bandColor, 0));

        gc.fillRect(centerX, centerY, w, h);
    }
    public void inDisks(int i, int xPos, int yPos, int w, int h) {
        float r = bands-80;
        float x = (float) (r * Math.cos(i));
        float y = (float) ((r-50) * Math.sin(i)/2);

        double centerY = y + Globals.primaryStage.getHeight()/2;
        double centerX = x + Globals.primaryStage.getWidth()/2;

        DecimalFormat df = new DecimalFormat("#.00");
        double bandColor = Double.parseDouble(df.format(i * 0.00196));

        gc.setFill(Color.color(1, bandColor, 0));

        gc.fillRect(centerY, centerY, w, h);
    }
    public void inArc(int i, int x, int y, int w, int h) {
        double amplitude;

        amplitude = y - h;

        DecimalFormat df = new DecimalFormat("#.00");
        double bandColor = Double.parseDouble(df.format(i * 0.00196));

        gc.setFill(Color.color(1, bandColor, 0));
        //gc.fillArc(x, 300, w, 300, 45, h, ArcType.ROUND);
        gc.arc(320, 10, 50, 50, 90, 180);
    }

































    private static String formatTime(javafx.util.Duration time) {
        int intElapsed = (int)Math.floor(time.toSeconds());
        int elapsedHours = intElapsed / (60 * 60);
        if (elapsedHours > 0) {
            intElapsed -= elapsedHours * 60 * 60;
        }
        int elapsedMinutes = intElapsed / 60;
        int elapsedSeconds = intElapsed - elapsedHours * 60 * 60
                - elapsedMinutes * 60;


        return String.format("%02d:%02d",
                elapsedMinutes, elapsedSeconds);
    }


}
