package main;

import fileio.StartGameInput;

import java.util.ArrayList;

public class Hero {
    private int mana;
    private final int health = 30;
    private String description;
    private ArrayList<String> colors;
    private String name;

    public Hero() {
    }

    public int getMana() {
        return mana;
    }

    public void setMana(final int mana) {
        this.mana = mana;
    }

    public int getHealth() {
        return health;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public ArrayList<String> getColors() {
        return colors;
    }

    public void setColors(final ArrayList<String> colors) {
        this.colors = colors;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Hero{"
                + "mana="
                + mana
                + ", health="
                + health
                + ", description='"
                + description
                + '\''
                + ", colors="
                + colors
                + ", name='"
                + name
                + '\''
                + '}';
    }

    public static Hero initializeHeroPlayerOne(StartGameInput startGame) {
        Hero heroPlayerOne = new Hero();
        heroPlayerOne.setMana(startGame.getPlayerOneHero().getMana());
        heroPlayerOne.setDescription(startGame.getPlayerOneHero().getDescription());
        heroPlayerOne.setColors(startGame.getPlayerOneHero().getColors());
        heroPlayerOne.setName(startGame.getPlayerOneHero().getName());
        return heroPlayerOne;
    }

    public static Hero initializeHeroPlayerTwo(StartGameInput startGame) {
        Hero heroPlayerTwo = new Hero();
        heroPlayerTwo.setMana(startGame.getPlayerTwoHero().getMana());
        heroPlayerTwo.setDescription(startGame.getPlayerTwoHero().getDescription());
        heroPlayerTwo.setColors(startGame.getPlayerTwoHero().getColors());
        heroPlayerTwo.setName(startGame.getPlayerTwoHero().getName());
        return heroPlayerTwo;
    }
}
