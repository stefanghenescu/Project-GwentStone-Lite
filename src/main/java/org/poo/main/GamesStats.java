package org.poo.main;

public class GamesStats {
    private int winsPlayerOne;
    private int winsPlayerTwo;

    public GamesStats() {
    }

    /**
     * Method to get the number of wins of player one
     * @return the number of wins of player one
     */
    public int getWinsPlayerOne() {
        return winsPlayerOne;
    }

    /**
     * Method to increment the number of wins of player one
     */
    public void incrementWinsPlayerOne() {
        winsPlayerOne++;
    }

    /**
     * Method to get the number of wins of player two
     * @return the number of wins of player two
     */
    public int getWinsPlayerTwo() {
        return winsPlayerTwo;
    }

    /**
     * Method to increment the number of wins of player two
     */
    public void incrementWinsPlayerTwo() {
        winsPlayerTwo++;
    }

    /**
     * Method to get the number of games played
     * @return the number of games played
     */
    public int getGamesPlayed() {
        return winsPlayerOne + winsPlayerTwo;
    }
}


