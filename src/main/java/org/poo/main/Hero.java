package org.poo.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CardInput;
import org.poo.fileio.StartGameInput;

public class Hero extends GameCard {

    public Hero(CardInput cardInput) {
        super(cardInput);
        setHealth(30);
        setAttackedThisTurn(false);
    }

    public static Hero initializeHeroPlayerOne(StartGameInput startGame) {
        return new Hero(startGame.getPlayerOneHero());
    }

    public static Hero initializeHeroPlayerTwo(StartGameInput startGame) {
        return new Hero(startGame.getPlayerTwoHero());
    }

    @Override
    public ObjectNode createCardNode(ObjectMapper objectMapper) {
        ObjectNode heroNode = objectMapper.createObjectNode();
        heroNode.put("mana", getMana());
        heroNode.put("description", getDescription());

        // Add colors array
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
