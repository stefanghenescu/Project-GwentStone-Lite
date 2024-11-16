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

    public ObjectNode generateOutput (ActionsInput action, int playerIndex) {
        ObjectNode commandNode = objectMapper.createObjectNode();
        commandNode.put("command", action.getCommand());
        commandNode.put("output", playerIndex);
        return commandNode;
    }
}