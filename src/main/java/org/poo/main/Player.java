package org.poo.main;

import org.poo.cardType.*;
import org.poo.fileio.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public final class Player {
    private int index;
    private ArrayList<GameCard> deck;
    private Hero hero;
    private ArrayList<GameCard> hand;
    private int playerMana;

    /**
     * Constructor for the Player class
     * @param index the index of the player
     * @param shuffleSeed the seed for the shuffle of the deck
     * @param deckInput the input for the deck
     * @param heroInput the input for the hero
     */
    public Player(final int index, final int shuffleSeed, final ArrayList<CardInput> deckInput,
                      final CardInput heroInput) {
        this.index = index;
        this.hand = new ArrayList<>();
        this.playerMana = 1;

        // Initialize the deck
        this.deck = new ArrayList<>();
        for (CardInput card : deckInput) {
            String cardType = card.getName();
            switch (cardType) {
                case "The Ripper":
                    this.deck.add(new TheRipper(card));
                    break;
                case "Miraj":
                    this.deck.add(new Miraj(card));
                    break;
                case "The Cursed One":
                    this.deck.add(new TheCursedOne(card));
                    break;
                case "Disciple":
                    this.deck.add(new Disciple(card));
                    break;
                case "Goliath":
                    this.deck.add(new Goliath(card));
                    break;
                case "Warden":
                    this.deck.add(new Warden(card));
                    break;
                default:
                    this.deck.add(new Minion(card));
            }
        }

        // Shuffle the deck
        Random random = new Random(shuffleSeed);
        Collections.shuffle(this.deck, random);

        // Initialize the hero
        switch (heroInput.getName()) {
            case "Lord Royce":
                this.hero = new LordRoyce(heroInput);
                break;
            case "Empress Thorina":
                this.hero = new EmpressThorina(heroInput);
                break;
            case "King Mudface":
                this.hero = new KingMudface(heroInput);
                break;
            case "General Kocioraw":
                this.hero = new GeneralKocioraw(heroInput);
                break;
            default:
                this.hero = new Hero(heroInput);
        }
    }

    public int getIndex() {
        return index;
    }

    public ArrayList<GameCard> getDeck() {
        return deck;
    }

    public Hero getHero() {
        return hero;
    }

    public ArrayList<GameCard> getHand() {
        return hand;
    }

    public int getPlayerMana() {
        return playerMana;
    }

    public void setPlayerMana(final int playerMana) {
        this.playerMana = playerMana;
    }

    /**
     * Method to draw a card from the deck and add it to the hand
     */
    public void addCardToHand() {
        if (!deck.isEmpty()) {
            // Add a card from the deck to the hand
            this.getHand().add(this.getDeck().getFirst());

            // Remove the card from the deck
            this.getDeck().removeFirst();
        }
    }
}
