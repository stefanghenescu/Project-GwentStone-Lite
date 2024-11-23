package org.poo.cardType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CardInput;
import org.poo.main.GameCard;

/**
 * This class represents a Minion card game
 * This class is immutable and extends the {@link GameCard} class
 */
public class Minion extends GameCard {
    private boolean frozen;

    /**
     * Constructor for the Minion class, we use super to call the GameCard constructor
     * @param cardInput the card input
     */
    public Minion(final CardInput cardInput) {
        super(cardInput);
        frozen = false;
    }

    /**
     * Verify if the card is frozen
     * @return true if the card is frozen, false otherwise
     */
    @Override
    public boolean isFrozen() {
        return frozen;
    }

    /**
     * Set the card as frozen or not
     * @param frozen true if the card is frozen, false otherwise
     */
    @Override
    public void setFrozen(final boolean frozen) {
        this.frozen = frozen;
    }

    /**
     * Verify if the card is a tank
     * This method is overridden by the subclasses
     * @return false but it will be overridden by the subclasses
     */
    @Override
    public boolean isTank() {
        return false;
    }

    /**
     * Create a card node with the minion information
     * This method is used to create the minion output in the JSON file
     * @param objectMapper the object mapper
     * @return the card node with the minion information for the JSON file
     */
    @Override
    public ObjectNode createCardNode(final ObjectMapper objectMapper) {

        ObjectNode cardNode = objectMapper.createObjectNode();
        cardNode.put("mana", getMana());
        cardNode.put("attackDamage", getAttackDamage());
        cardNode.put("health", getHealth());
        cardNode.put("description", getDescription());

        ArrayNode colorsArray = cardNode.putArray("colors");
        for (String color : getColors()) {
            colorsArray.add(color);
        }
        cardNode.set("colors", colorsArray);

        cardNode.put("name", getName());

        return cardNode;
    }
}
