package org.poo.cardtype;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CardInput;
import org.poo.gamecomponents.GameCard;
import org.poo.gamecomponents.Table;

/**
 * This class represents a Hero card game
 * This class is immutable and extends the {@link GameCard} class
 */
public class Hero extends GameCard {
    static final int HERO_HEALTH = 30;

    /**
     * Constructor for the Hero class, we use super to call the GameCard constructor
     * The hero's health is set to 30 at the beginning of the game
     * The hero has not been attacked yet
     * @param cardInput the card input
     */
    public Hero(final CardInput cardInput) {
        super(cardInput);
        setHealth(HERO_HEALTH);
        setAttackedThisTurn(false);
    }

    /**
     * @param affectedRow the row that will be affected by the ability
     * @param table the table where the game is being played
     */
    public void useAbility(final int affectedRow, final Table table) {
        // To be implemented by the subclasses
    }

    /**
     * Create a card node with the hero information
     * This method is used to create the hero output in the JSON file
     * @param objectMapper the object mapper
     * @return the card node with the hero information for the JSON file
     */
    @Override
    public ObjectNode createCardNode(final ObjectMapper objectMapper) {
        ObjectNode heroNode = objectMapper.createObjectNode();
        heroNode.put("mana", getMana());
        heroNode.put("description", getDescription());

        ArrayNode colorsArray = objectMapper.createArrayNode();
        for (String color : getColors()) {
            colorsArray.add(color);
        }
        heroNode.set("colors", colorsArray);

        heroNode.put("name", getName());
        heroNode.put("health", getHealth());

        return heroNode;
    }
}
