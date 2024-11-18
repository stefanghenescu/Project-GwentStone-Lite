package org.poo.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.ActionsInput;

import java.util.ArrayList;


public class JsonOutput {
    private final ObjectMapper objectMapper;

    public JsonOutput() {
        this.objectMapper = new ObjectMapper();
    }

    public ObjectNode generateOutput(ActionsInput action, ArrayList<GameCard> cardArray) {
        ObjectNode commandNode = objectMapper.createObjectNode();
        commandNode.put("command", action.getCommand());
        commandNode.put("playerIdx", action.getPlayerIdx());

        ArrayNode jsonArray = objectMapper.createArrayNode();
        for (GameCard gameCard : cardArray) {
            ObjectNode cardNode = gameCard.createCardNode(objectMapper);
            jsonArray.add(cardNode);
        }

        commandNode.set("output", jsonArray);
        return commandNode;
    }

    public ObjectNode generateOutput(ActionsInput action, GameCard gameCard) {
        ObjectNode commandNode = objectMapper.createObjectNode();
        commandNode.put("command", action.getCommand());
        commandNode.put("playerIdx", action.getPlayerIdx());

        ObjectNode cardNode = gameCard.createCardNode(objectMapper);
        commandNode.set("output", cardNode);
        return commandNode;
    }

    public ObjectNode generateOutput(ActionsInput action, int playerIndex) {
        ObjectNode commandNode = objectMapper.createObjectNode();
        commandNode.put("command", action.getCommand());
        commandNode.put("output", playerIndex);
        return commandNode;
    }

    public ObjectNode generateOutput(ActionsInput action, String error) {
        ObjectNode commandNode = objectMapper.createObjectNode();
        commandNode.put("command", action.getCommand());
        commandNode.put("handIdx", action.getHandIdx());
        commandNode.put("error", error);
        return commandNode;
    }

    public ObjectNode generateOutput(ActionsInput action, Player currentPlayer) {
        ObjectNode commandNode = objectMapper.createObjectNode();
        commandNode.put("command", action.getCommand());
        commandNode.put("playerIdx", action.getPlayerIdx());
        commandNode.put("output", currentPlayer.getPlayerMana());
        return commandNode;
    }

    public ObjectNode generateOutput(ActionsInput action, Table table) {
        ObjectNode commandNode = objectMapper.createObjectNode();
        commandNode.put("command", action.getCommand());
        ArrayNode cardsOnTable = objectMapper.createArrayNode();

        for (int rowIndex = 0; rowIndex < 4; rowIndex++) {
            ArrayNode rowNode = objectMapper.createArrayNode();
            ArrayList<GameCard> row = table.getRow(rowIndex);

            for (GameCard card : row) {
                rowNode.add(card.createCardNode(objectMapper));
            }
            cardsOnTable.add(rowNode);
        }
        commandNode.set("output", cardsOnTable);
        return commandNode;
    }
}