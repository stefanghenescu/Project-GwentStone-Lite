package org.poo.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CardInput;

import java.util.ArrayList;

public class GameCard {
    private int mana;
    private int attackDamage;
    private int health;
    private String description;
    private ArrayList<String> colors;
    private String name;
    private boolean attackedThisTurn;

    public GameCard(CardInput cardInput) {
        attackDamage = cardInput.getAttackDamage();
        health = cardInput.getHealth();
        mana = cardInput.getMana();
        description = cardInput.getDescription();
        colors = cardInput.getColors();
        name = cardInput.getName();
        attackedThisTurn = false;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(final int mana) {
        this.mana = mana;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(final int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(final int health) {
        this.health = health;
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

    public boolean isFrozen() {
        return false;
    }

    public void setFrozen(final boolean frozen) {
    }

    public boolean hasAttackedThisTurn() {
        return attackedThisTurn;
    }

    public void setAttackedThisTurn(final boolean attackedThisTurn) {
        this.attackedThisTurn = attackedThisTurn;
    }

    public boolean isTank() {
        return false;
    }

    public ObjectNode createCardNode(ObjectMapper objectMapper) {
        return null;
    }
}
