package org.poo.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.ActionsInput;
import org.poo.fileio.GameInput;

/**
 * Class that represents one game
 * From here the game is played and every action is made calling the proper method
 */
public final class Game {
    static final int MAX_MANA_PER_ROUND = 10;
    static final int PLAYER_TURN_XOR = 3;

    private int turns;
    private int playerTurn;
    private Player playerOne;
    private Player playerTwo;

    public Game(final Player playerOne, final Player playerTwo) {
        this.turns = 0;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
    }

    /**
     * Method that sets the player whose turn is to play
     * This is done by checking if the number of turns is even or odd
     * If it is even then the player that started the game plays
     * If it is odd then the other player plays
     * @param startingPlayer the player that starts the game
     */
    public void setPlayerTurn(final int startingPlayer) {
        if (getTurns() % 2 == 0) {
            playerTurn = startingPlayer;
        } else {
            /* xor between 11 (base 2) = 3 (base 10) and index startingPlayer
            3 ^ 1 = 2
            3 ^ 2 = 1
            if the number of turns is odd then it is the other player's turn
            by the one who started the game */
            playerTurn = (PLAYER_TURN_XOR ^ startingPlayer);
        }
    }

    /**
     * Method that returns the current player
     * @return the player whose turn is to play
     */
    public Player getCurrentPlayer() {
        if (getPlayerTurn() == 1) {
            return playerOne;
        }
        return playerTwo;
    }

    /**
     * Method that ends the turn of the current player
     * @param table the table where the game is being played
     * @param game the input of the game
     */
    public void endTurn(final Table table, final GameInput game) {
        // check if there are frozen books and change their flag
        table.defrostCards(getPlayerTurn());

        // reset the flag of the cards that attacked this turn
        table.rechargeCardsAttack();

        Player currentPlayer = getCurrentPlayer();
        currentPlayer.getHero().setAttackedThisTurn(false);

        // increment the number of turns played
        increaseTurns();

        setPlayerTurn(game.getStartGame().getStartingPlayer());


        // if a round is over, each player draws a card
        // and his hand is updated
        if (getTurns() % 2 == 0) {
            playerOne.addCardToHand();
            playerTwo.addCardToHand();

            int manaRound = Math.min(getTurns() / 2 + 1, MAX_MANA_PER_ROUND);

            playerOne.setPlayerMana(playerOne.getPlayerMana() + manaRound);
            playerTwo.setPlayerMana(playerTwo.getPlayerMana() + manaRound);
        }
    }

    /**
     * Method that plays the game and makes the proper action
     * depending on the command of the action
     * It calls the proper method to make the action
     * @param objectMapper the object mapper used to create the output
     * @param output the output of the game
     * @param table the table where the game is being played
     * @param game the input of the game
     * @param action the action that is being performed
     * @param gamesStats the statistics of the games
     */
    public void actionOutput(final ObjectMapper objectMapper, final ArrayNode output,
                                    final Table table, final GameInput game,
                                    final ActionsInput action, final GamesStats gamesStats) {
        ObjectNode commandNode = objectMapper.createObjectNode();
        JsonOutput jsonOutput = new JsonOutput();
        switch (action.getCommand()) {
            case "getPlayerDeck":
                Action.actionDeck(playerOne, playerTwo, output, action, jsonOutput);
                break;
            case "getPlayerHero":
                Action.actionHero(playerOne, playerTwo, output, action, jsonOutput);
                break;
            case "getPlayerTurn":
                setPlayerTurn(game.getStartGame().getStartingPlayer());
                output.add(jsonOutput.generateOutput(action, getPlayerTurn()));
                break;
            case "getCardsInHand":
                Action.actionHand(playerOne, playerTwo, output, action, jsonOutput);
                break;
            case "endPlayerTurn":
                endTurn(table, game);
                break;
            case "placeCard":
                Player currentPlayer = getCurrentPlayer();
                Action.actionPlaceCard(currentPlayer, table, output, action, jsonOutput);
                break;
            case "getPlayerMana":
                Player player = (action.getPlayerIdx() == 1) ? playerOne : playerTwo;
                Action.actionGetMana(player, output, action, jsonOutput);
                break;
            case "getCardsOnTable":
                output.add(jsonOutput.generateOutputTable(action, table));
                break;
            case "cardUsesAttack":
                Action.actionUseAttack(action, table, getPlayerTurn(), output, jsonOutput);
                break;
            case "getCardAtPosition":
                output.add(jsonOutput.generateOutputCardAtPosition(action, table));
                break;
            case "cardUsesAbility":
                Action.actionUseAbility(action, table, getPlayerTurn(), output, jsonOutput);
                break;
            case "useAttackHero":
                Action.actionUseAttackOnHero(action, table, playerOne, playerTwo, getPlayerTurn(),
                                            output, gamesStats, jsonOutput);
                break;
            case "useHeroAbility":
                Action.actionHeroUseAbility(getCurrentPlayer(), action, getPlayerTurn(), table,
                                            output, jsonOutput);
                break;
            case "getFrozenCardsOnTable":
                output.add(jsonOutput.generateOutputFrozenCards(action, table));
                break;
            case "getPlayerOneWins":
                output.add(jsonOutput.generateOutput(action, gamesStats.getWinsPlayerOne()));
                break;
            case "getPlayerTwoWins":
                output.add(jsonOutput.generateOutput(action, gamesStats.getWinsPlayerTwo()));
                break;
            case "getTotalGamesPlayed":
                output.add(jsonOutput.generateOutput(action, gamesStats.getGamesPlayed()));
                break;
            default:
                break;
        }
    }

    /**
     * Method that increments the number of turns played
     */
    public void increaseTurns() {
        turns++;
    }

    public int getTurns() {
        return turns;
    }

    public int getPlayerTurn() {
        return playerTurn;
    }
}
