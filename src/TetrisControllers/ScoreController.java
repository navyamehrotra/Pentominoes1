package TetrisControllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import Constants.TetrisConstants;

public class ScoreController {
    private int currentScore;
    private int[] highScores =  new int[TetrisConstants.HIGH_SCORES_KEPT];
    
    // Nathaneal and Aukje please fill this class in (:
    public ScoreController() {
        readHighScoresFromFile();
    }

    public void readHighScoresFromFile() {
        int count = 0;
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/highScores.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                highScores[count] = Integer.parseInt(line);
                count++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void writeHighScoresToFile() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(System.getProperty("user.dir") + "/highScores.txt"));
            for (int score : highScores) {
                writer.write(String.valueOf(score));
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getHighestScore() {
        return highScores[0];
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public void resetScore() {
        currentScore = 0;
    }

    public void addPoint() {
        currentScore++;
        
        // determine the correct position for the current score in list
        int position = -1;
        for (int i = 0; i < highScores.length; i++) {
            if (currentScore > highScores[i]) {
                position = i;
                break;
            }
        }

        if (position == -1) {
            return;
        }

        // move the lower scores down
        for (int i = highScores.length - 1; i > position; i--)  {
            highScores[i] = highScores[i - 1];
        }
        
        // add current score to list
        highScores[position] = currentScore;

        writeHighScoresToFile();
    }
}

