package org.poo.main;

public class GamesStats {
    private int winsPlayerOne;
    private int winsPlayerTwo;

    public GamesStats() {
    }

    public int getWinsPlayerOne() {
        return winsPlayerOne;
    }

    public void incrementWinsPlayerOne() {
        winsPlayerOne++;
    }

    public int getWinsPlayerTwo() {
        return winsPlayerTwo;
    }

    public void incrementWinsPlayerTwo() {
        winsPlayerTwo++;
    }

    public int getGamesPlayed() {
        return winsPlayerOne + winsPlayerTwo;
    }
}


