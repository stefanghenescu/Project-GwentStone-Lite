package main;

import fileio.CardInput;
import fileio.GameInput;
import fileio.Input;
import fileio.StartGameInput;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public final class Player {
    private int index;
    private int nrCardsInDeck;
    private int shuffleSeed;
    private ArrayList<CardInput> deck;
    private Hero hero;
    private ArrayList<CardInput> hand;
    private int playerMana;

    public Player() {
        this.hand = new ArrayList<>();
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(final int index) {
        this.index = index;
    }

    public int getNrCardsInDeck() {
        return nrCardsInDeck;
    }

    public void setNrCardsInDeck(final int nrCardsInDeck) {
        this.nrCardsInDeck = nrCardsInDeck;
    }

    public int getShuffleSeed() {
        return shuffleSeed;
    }

    public void setShuffleSeed(final int shuffleSeed) {
        this.shuffleSeed = shuffleSeed;
    }

    public ArrayList<CardInput> getDeck() {
        return deck;
    }

    public void setDeck(final ArrayList<CardInput> deck) {
        this.deck = deck;
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(final Hero hero) {
        this.hero = hero;
    }

    public ArrayList<CardInput> getHand() {
        return hand;
    }

    public void setHand(final ArrayList<CardInput> hand) {
        this.hand = hand;
    }

    public int getPlayerMana() {
        return playerMana;
    }

    public void setPlayerMana(final int playerMana) {
        this.playerMana = playerMana;
    }

    public static Player initializePlayerOne(GameInput game, Input inputData) {
        Player playerOne = new Player();
        playerOne.setIndex(1);

        StartGameInput startGame = game.getStartGame();

        // aflam cate carti sunt in deck-ul fiecarui jucator
        playerOne.setNrCardsInDeck(inputData.getPlayerOneDecks().getNrCardsInDeck());

        // shuffleSeed-ul fiecarui joc
        playerOne.setShuffleSeed(startGame.getShuffleSeed());

        // la fiecare meci selectam deck-ul cu care va juca fiecare jucator
        int indexDeckPlayerOne = startGame.getPlayerOneDeckIdx();
        playerOne.setDeck(inputData.getPlayerOneDecks().getDecks().get(indexDeckPlayerOne));

        // se amesteca deck-urile
        Random randomPlayerOne = new Random(playerOne.getShuffleSeed());
        Collections.shuffle(playerOne.getDeck(), randomPlayerOne);

        // fiecare jucator primeste un erou
        Hero heroPlayerOne = Hero.initializeHeroPlayerOne(playerOne, startGame);

        playerOne.setHero(heroPlayerOne);
        return playerOne;
    }

    public static Player initializePlayerTwo(GameInput game, Input inputData) {
        Player playerTwo = new Player();
        playerTwo.setIndex(2);

        StartGameInput startGame = game.getStartGame();

        // aflam cate carti sunt in deck-ul fiecarui jucator
        playerTwo.setNrCardsInDeck(inputData.getPlayerTwoDecks().getNrCardsInDeck());

        // shuffleSeed-ul fiecarui joc
        playerTwo.setShuffleSeed(startGame.getShuffleSeed());

        // la fiecare meci selectam deck-ul cu care va juca fiecare jucator
        int indexDeckPlayerTwo = startGame.getPlayerTwoDeckIdx();
        playerTwo.setDeck(inputData.getPlayerTwoDecks().getDecks().get(indexDeckPlayerTwo));

        // se amesteca deck-urile
        Random randomPlayerTwo = new Random(playerTwo.getShuffleSeed());
        Collections.shuffle(playerTwo.getDeck(), randomPlayerTwo);

        // fiecare jucator primeste un erou
        Hero heroPlayerTwo = Hero.initializeHeroPlayerTwo(playerTwo, startGame);

        playerTwo.setHero(heroPlayerTwo);
        return playerTwo;
    }
}
