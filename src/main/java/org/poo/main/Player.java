package org.poo.main;

import org.poo.cardType.*;
import org.poo.fileio.*;
import org.poo.main.Hero;


import javax.smartcardio.Card;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public final class Player {
    private int index;
    private int nrCardsInDeck;
    private int shuffleSeed;
    private ArrayList<GameCard> deck;
    private Hero hero;
    private ArrayList<GameCard> hand;
    private int playerMana;

    public Player() {
        hand = new ArrayList<>();
        playerMana = 1;
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

    public ArrayList<GameCard> getDeck() {
        return deck;
    }

    public void setDeck(final ArrayList<CardInput> deck) {
        ArrayList<GameCard> copyDeck = new ArrayList<>();
        for (CardInput card : deck) {
            String cardType = card.getName();
            switch (cardType) {
                case "The Ripper":
                    copyDeck.add(new TheRipper(card));
                    break;
                case "Miraj":
                    copyDeck.add(new Miraj(card));
                    break;
                case "The Cursed One":
                    copyDeck.add(new TheCursedOne(card));
                    break;
                case "Disciple":
                    copyDeck.add(new Disciple(card));
                    break;
                case "Goliath":
                    copyDeck.add(new Goliath(card));
                    break;
                case "Warden":
                    copyDeck.add(new Warden(card));
                    break;
                default:
                    copyDeck.add(new Minion(card));
            }
        }
        this.deck = copyDeck;
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(final Hero hero) {
        this.hero = hero;
    }

    public ArrayList<GameCard> getHand() {
        return hand;
    }

    public void setHand(final ArrayList<GameCard> hand) {
        this.hand = hand;
    }

    public int getPlayerMana() {
        return playerMana;
    }

    public void setPlayerMana(final int playerMana) {
        this.playerMana = playerMana;
    }

    public void addCardToHand() {
        if (this.getNrCardsInDeck() > 0) {
            // adaugam o carte din deck in mana
            this.getHand().add(this.getDeck().get(0));
            // scoatem o din deck
            this.getDeck().remove(0);
            // actualizam dimensiunea deck-ului
            this.setNrCardsInDeck(this.getNrCardsInDeck() - 1);
        }
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
        String heroName = startGame.getPlayerOneHero().getName();
        CardInput heroCard = startGame.getPlayerOneHero();
        Hero heroPlayerOne;
        switch (heroName) {
            case "Lord Royce":
                heroPlayerOne = new LordRoyce(heroCard);
                break;
            case "Empress Thorina":
                heroPlayerOne = new EmpressThorina(heroCard);
                break;
            case "King Mudface":
                heroPlayerOne = new KingMudface(heroCard);
                break;
            case "General Kocioraw":
                heroPlayerOne=  new GeneralKocioraw(heroCard);
                break;
            default:
                heroPlayerOne = new Hero(heroCard);
        }

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
        String heroName = startGame.getPlayerTwoHero().getName();
        CardInput heroCard = startGame.getPlayerTwoHero();
        Hero heroPlayerTwo;
        switch (heroName) {
            case "Lord Royce":
                heroPlayerTwo = new LordRoyce(heroCard);
                break;
            case "Empress Thorina":
                heroPlayerTwo = new EmpressThorina(heroCard);
                break;
            case "King Mudface":
                heroPlayerTwo = new KingMudface(heroCard);
                break;
            case "General Kocioraw":
                heroPlayerTwo=  new GeneralKocioraw(heroCard);
                break;
            default:
                heroPlayerTwo = new Hero(heroCard);
        }

        playerTwo.setHero(heroPlayerTwo);
        return playerTwo;
    }

}
