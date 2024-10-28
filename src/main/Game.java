package main;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import fileio.CardInput;
import fileio.GameInput;

import java.util.ArrayList;

public final class Game {
    private Game() {
    }

    public static void actionOutput(ObjectMapper objectMapper, ArrayNode output, GameInput game, Player playerOne, Player playerTwo, ActionsInput action) {
        ObjectNode commandNode = objectMapper.createObjectNode();
        commandNode.put("command", action.getCommand());
        if (action.getPlayerIdx() != 0) {
            commandNode.put("playerIdx", action.getPlayerIdx());
        }

        if (action.getCommand().equals("getPlayerDeck")) {
            ArrayList<CardInput> deck;
            if (action.getPlayerIdx() == 1) {
                deck = playerOne.getDeck();
            } else {
                deck = playerTwo.getDeck();
            }

            ArrayNode deckArray = objectMapper.createArrayNode();
            for (CardInput card : deck) {
                ObjectNode cardNode = createCardNode(card, objectMapper);
                deckArray.add(cardNode);
            }
            commandNode.set("output", deckArray);
        } else if (action.getCommand().equals("getPlayerHero")) {
            if (action.getPlayerIdx() == 1) {
                ObjectNode heroNode = createHeroNode(playerOne.getHero(), objectMapper);
                commandNode.set("output", heroNode);
            } else {
                ObjectNode heroNode = createHeroNode(playerTwo.getHero(), objectMapper);
                commandNode.set("output", heroNode);
            }
        } else if (action.getCommand().equals("getPlayerTurn")) {
            commandNode.put("output", game.getStartGame().getStartingPlayer());
        }
        output.add(commandNode);
    }


    private static ObjectNode createCardNode(CardInput card, ObjectMapper mapper) {
        ObjectNode cardNode = mapper.createObjectNode();
        cardNode.put("mana", card.getMana());
        cardNode.put("attackDamage", card.getAttackDamage());
        cardNode.put("health", card.getHealth());
        cardNode.put("description", card.getDescription());

        // Add colors array
        ArrayNode colorsArray = mapper.createArrayNode();
        for (String color : card.getColors()) {
            colorsArray.add(color);
        }
        cardNode.set("colors", colorsArray);

        cardNode.put("name", card.getName());

        return cardNode;
    }


    private static ObjectNode createHeroNode(Hero hero, ObjectMapper mapper) {
        ObjectNode heroNode = mapper.createObjectNode();
        heroNode.put("mana", hero.getMana());
        heroNode.put("description", hero.getDescription());

        // Add colors array
        ArrayNode colorsArray = mapper.createArrayNode();
        for (String color : hero.getColors()) {
            colorsArray.add(color);
        }
        heroNode.set("colors", colorsArray);

        heroNode.put("name", hero.getName());
        heroNode.put("health", hero.getHealth());

        return heroNode;
    }
}
