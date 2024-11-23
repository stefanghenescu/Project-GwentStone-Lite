package org.poo.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CardInput;

import java.util.ArrayList;

/**
 * Class that represents a card in the game
 * It has all the attributes of a card and the methods to use them
 * It is the parent class of all the cards in the game
 * It is like a copy of the CardInput class
 */
public class GameCard {
    private int mana;
    private int attackDamage;
    private int health;
    private String description;
    private ArrayList<String> colors;
    private String name;
    private boolean attackedThisTurn;

    public GameCard(final CardInput cardInput) {
        attackDamage = cardInput.getAttackDamage();
        health = cardInput.getHealth();
        mana = cardInput.getMana();
        description = cardInput.getDescription();
        colors = cardInput.getColors();
        name = cardInput.getName();
        attackedThisTurn = false;
    }

    /**
     * Method to get the mana of the card
     * @return the mana of the card
     */
    public int getMana() {
        return mana;
    }

    /**
     * Method to set the mana of the card
     * @param mana the mana to set
     */
    public void setMana(final int mana) {
        this.mana = mana;
    }

    /**
     * Method to get the attack damage of the card
     * @return the attack damage of the card
     */
    public int getAttackDamage() {
        return attackDamage;
    }


    /**
     * Method to set the attack damage of the card
     * @param attackDamage the attack damage to set
     */
    public void setAttackDamage(final int attackDamage) {
        this.attackDamage = attackDamage;
    }

    /**
     * Method to get the health of the card
     * @return the health of the card
     */
    public int getHealth() {
        return health;
    }

    /**
     * Method to set the health of the card
     * @param health the health to set
     */
    public void setHealth(final int health) {
        this.health = health;
    }

    /**
     * Method to get the description of the card
     * @return the description of the card
     */
    public String getDescription() {
        return description;
    }

    /**
     * Method to set the description of the card
     * @param description the description to set
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Method to get the colors of the card
     * @return the colors of the card
     */
    public ArrayList<String> getColors() {
        return colors;
    }

    /**
     * Method to set the colors of the card
     * @param colors the colors to set
     */
    public void setColors(final ArrayList<String> colors) {
        this.colors = colors;
    }

    /**
     * Method to get the name of the card
     * @return the name of the card
     */
    public String getName() {
        return name;
    }

    /**
     * Method to set the name of the card
     * @param name the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Method to get if the card is frozen
     * This method is overwritten in the child classes
     * @return false
     */
    public boolean isFrozen() {
        return false;
    }

    /**
     * Method to set if the card is frozen
     * This method is overwritten in the child classes
     * @param frozen the value to set
     */
    public void setFrozen(final boolean frozen) {
    }

    /**
     * Method to get if the card has attacked this turn
     * @return true if the card has attacked this turn, false otherwise
     */
    public boolean hasAttackedThisTurn() {
        return attackedThisTurn;
    }

    /**
     * Method to set if the card has attacked this turn
     * @param attackedThisTurn the value to set
     */
    public void setAttackedThisTurn(final boolean attackedThisTurn) {
        this.attackedThisTurn = attackedThisTurn;
    }

    /**
     * Method to get if the card is a tank
     * This method is overwritten in the child classes
     * @return false
     */
    public boolean isTank() {
        return false;
    }

    /**
     * Method to use the ability of the card
     * This method is overwritten in the child classes
     */
    public void useAbility(final GameCard attackedCard) {
    }

    /**
     * Method to create a card node for the json file for output
     * @param objectMapper the object mapper used to create the node
     * @return the card node created for the json file
     */
    public ObjectNode createCardNode(final ObjectMapper objectMapper) {
        return null;
    }
}
