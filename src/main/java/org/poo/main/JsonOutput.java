package org.poo.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.ActionsInput;

import java.util.ArrayList;

/**
 * Class that provides methods to generate JSON outputs for the game actions
 * It uses the ObjectMapper class from the Jackson library to create JSON objects
 */
public class JsonOutput {
    static final int NR_ROWS_TABLE = 4;
    private final ObjectMapper objectMapper;

    public JsonOutput() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Method that generates the JSON output for the cards in the hand or in the deck
     * @param action the action that was made and it's details
     * @param cardArray the array of cards to be output
     * @return the JSON object that contains the cards
     */
    public ObjectNode generateOutputCards(final ActionsInput action,
                                              final ArrayList<GameCard> cardArray) {
        ObjectNode commandNode = objectMapper.createObjectNode();
        commandNode.put("command", action.getCommand());
        commandNode.put("playerIdx", action.getPlayerIdx());

        ArrayNode jsonArray = objectMapper.createArrayNode();
        for (GameCard gameCard : cardArray) {
            // Create a JSON object for each card in the array
            ObjectNode cardNode = gameCard.createCardNode(objectMapper);
            jsonArray.add(cardNode);
        }

        commandNode.set("output", jsonArray);
        return commandNode;
    }


    /**
     * Method that generates the JSON output for the hero
     * @param action the action that was made and it's details
     * @param gameCard the hero card
     * @return the JSON object that contains the hero card
     */
    public ObjectNode generateOutputHero(final ActionsInput action, final GameCard gameCard) {
        ObjectNode commandNode = objectMapper.createObjectNode();
        commandNode.put("command", action.getCommand());
        commandNode.put("playerIdx", action.getPlayerIdx());

        // Create a JSON object for the hero card
        ObjectNode cardNode = gameCard.createCardNode(objectMapper);
        commandNode.set("output", cardNode);
        return commandNode;
    }

    /**
     * Method that generates the JSON output for a command with generates a single number
     * Some commands are player turn and the numbers of wins for each player
     * @param action the action that was made and it's details
     * @param playerOutput the output that has to be displayed
     * @return the JSON object that contains the output
     */
    public ObjectNode generateOutput(final ActionsInput action, final int playerOutput) {
        ObjectNode commandNode = objectMapper.createObjectNode();
        commandNode.put("command", action.getCommand());
        commandNode.put("output", playerOutput);
        return commandNode;
    }

    /**
     * Method that generates the JSON output in case an error occurres when placing a card
     * on the table
     * @param action the action that was made and it's details
     * @param error the error message that has to be displayed
     * @return the JSON object that contains the error message
     */
    public ObjectNode generateOutputTableError(final ActionsInput action, final String error) {
        ObjectNode commandNode = objectMapper.createObjectNode();
        commandNode.put("command", action.getCommand());
        commandNode.put("handIdx", action.getHandIdx());
        commandNode.put("error", error);
        return commandNode;
    }

    /**
     * Method that generates the JSON output for the mana of a player
     * @param action the action that was made and it's details
     * @param currentPlayer the player whose mana has to be displayed
     * @return the JSON object that contains the player's mana
     */
    public ObjectNode generateOutputMana(final ActionsInput action, final Player currentPlayer) {
        ObjectNode commandNode = objectMapper.createObjectNode();
        commandNode.put("command", action.getCommand());
        commandNode.put("playerIdx", action.getPlayerIdx());
        commandNode.put("output", currentPlayer.getPlayerMana());
        return commandNode;
    }

    /**
     * Method that generates the JSON output for the cards on the table
     * @param action the action that was made and it's details
     * @param table the table where the game is being played
     * @return the JSON object that contains the table cards
     */
    public ObjectNode generateOutputTable(final ActionsInput action, final Table table) {
        ObjectNode commandNode = objectMapper.createObjectNode();
        commandNode.put("command", action.getCommand());
        ArrayNode cardsOnTable = objectMapper.createArrayNode();

        // iterate over the rows of the table
        for (int rowIndex = 0; rowIndex < NR_ROWS_TABLE; rowIndex++) {
            ArrayNode rowNode = objectMapper.createArrayNode();
            ArrayList<GameCard> row = table.getRow(rowIndex);

            // iterate over the cards in the row
            for (GameCard card : row) {
                // Create a JSON object for each card in the row
                rowNode.add(card.createCardNode(objectMapper));
            }
            cardsOnTable.add(rowNode);
        }
        commandNode.set("output", cardsOnTable);
        return commandNode;
    }

    /**
     * Method that generates the JSON output for an error during the attack of a card
     * @param action the action that was made and it's details
     * @param message the error message that has to be displayed
     * @return the JSON object that contains the error message
     */
    public ObjectNode generateErrorCardAttack(final ActionsInput action, final String message) {
        ObjectNode commandNode = objectMapper.createObjectNode();
        ObjectNode coordinatesAttacker = objectMapper.createObjectNode();
        coordinatesAttacker.put("x", action.getCardAttacker().getX());
        coordinatesAttacker.put("y", action.getCardAttacker().getY());

        ObjectNode coordinatesAttacked = objectMapper.createObjectNode();
        coordinatesAttacked.put("x", action.getCardAttacked().getX());
        coordinatesAttacked.put("y", action.getCardAttacked().getY());

        commandNode.put("command", action.getCommand());
        commandNode.set("cardAttacker", coordinatesAttacker);
        commandNode.set("cardAttacked", coordinatesAttacked);
        commandNode.put("error", message);

        return commandNode;
    }

    /**
     * Method that generates the JSON output for an error during the use of a card's ability
     * @param action the action that was made and it's details
     * @param error the error message that has to be displayed
     * @return the JSON object that contains the error message
     */
    public ObjectNode generateErrorAbility(final ActionsInput action, final String error) {
        ObjectNode commandNode = objectMapper.createObjectNode();
        commandNode.put("command", action.getCommand());
        commandNode.put("affectedRow", action.getAffectedRow());
        commandNode.put("error", error);
        return commandNode;
    }

    /**
     * Method that generates the JSON output for the frozen cards on the table
     * @param action the action that was made and it's details
     * @param table the table where the game is being played
     * @return the JSON object that contains the frozen cards
     */
    public ObjectNode generateOutputFrozenCards(final ActionsInput action, final Table table) {
        ObjectNode commandNode = objectMapper.createObjectNode();
        commandNode.put("command", action.getCommand());
        ArrayNode frozenCardsOnTable = objectMapper.createArrayNode();

        // iterate over the rows of the table
        for (int rowIndex = 0; rowIndex < NR_ROWS_TABLE; rowIndex++) {
            ArrayNode rowNode = objectMapper.createArrayNode();
            ArrayList<GameCard> row = table.getRow(rowIndex);

            // iterate over the cards in the row
            for (GameCard card : row) {
                if (card.isFrozen()) {
                    // Create a JSON object for each frozen card in the row
                    frozenCardsOnTable.add(card.createCardNode(objectMapper));
                }
            }
        }
        commandNode.set("output", frozenCardsOnTable);
        return commandNode;
    }

    /**
     * Method that generates the JSON output for the card at a specific position on the table
     * @param action the action that was made and it's details
     * @param table the table where the game is being played
     * @return the JSON object that contains the card at the specified position
     */
    public ObjectNode generateOutputCardAtPosition(final ActionsInput action, final Table table) {
        ObjectNode commandNode = objectMapper.createObjectNode();
        commandNode.put("command", action.getCommand());
        commandNode.put("x", action.getX());
        commandNode.put("y", action.getY());

        String error = null;
        // Check if the row exists and if there is a card at the specified position
        if (table.getRow(action.getX()).size() <= action.getY()) {
            error = "No card available at that position.";
            commandNode.put("output", error);
        } else {
            // Create a JSON object for the card at the specified position
            GameCard card = table.getRow(action.getX()).get(action.getY());
            commandNode.set("output", card.createCardNode(objectMapper));
        }
        return commandNode;
    }

    /**
     * Method that generates the JSON output for the error that occurs when attacking a hero
     * @param action the action that was made and it's details
     * @param error the error message that has to be displayed
     * @return the JSON object that contains the error message
     */
    public ObjectNode generateOutputErrorAttackOnHero(final ActionsInput action,
                                                         final String error) {
        ObjectNode commandNode = objectMapper.createObjectNode();
        ObjectNode coordinatesAttacker = objectMapper.createObjectNode();
        coordinatesAttacker.put("x", action.getCardAttacker().getX());
        coordinatesAttacker.put("y", action.getCardAttacker().getY());

        commandNode.put("command", action.getCommand());
        commandNode.set("cardAttacker", coordinatesAttacker);
        commandNode.put("error", error);
        return commandNode;
    }

    /**
     * Method that generates the JSON output for the end of a game
     * @param playerKilled the player that was killed
     * @return the JSON object that contains the end of a game message
     */
    public ObjectNode generateOutputGameEnd(final int playerKilled) {
        ObjectNode commandNode = objectMapper.createObjectNode();
        if (playerKilled == 1) {
            commandNode.put("gameEnded", "Player one killed the enemy hero.");
        } else {
            commandNode.put("gameEnded", "Player two killed the enemy hero.");
        }
        return commandNode;
    }
}
