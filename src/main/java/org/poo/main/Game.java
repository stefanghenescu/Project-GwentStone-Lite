package org.poo.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.ActionsInput;
import org.poo.fileio.GameInput;

import javax.xml.namespace.QName;
import java.util.ArrayList;

public final class Game {
    private int turns;
    private int playerTurn;
    private Player playerOne;
    private Player playerTwo;

    public Game(Player playerOne, Player playerTwo) {
        this.turns = 0;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
    }

    public void increaseTurns() {
        turns++;
    }

    public int getTurns() {
        return turns;
    }

    public int getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(int startingPlayer) {
        if (getTurns() % 2 == 0) {
            playerTurn = startingPlayer;
        } else {
            /* xor intre 11(baza 2) = 3(baza 10) si index startingPlayer
            3 ^ 1 = 2
            3 ^ 2 = 1
            daca numarul de ture este impar atunci e randul celuilalt jucator fata
            de cel ce a inceput jocul */
            playerTurn = (3 ^ startingPlayer);
        }
    }

    public Player getCurrentPlayer() {
        if (getPlayerTurn() == 1)
            return playerOne;
        return playerTwo;
    }

    public void endTurn(Table table, GameInput game) {
        // verific daca exista carti frozen si le schimb flag-ul
        table.defrostCards(getPlayerTurn());

        // le resetez flag-ul cartilor care au atacat tura aceasta
        table.rechargeCardsAttack();

        Player currentPlayer = getCurrentPlayer();
        currentPlayer.getHero().setAttackedThisTurn(false);

        // incrementez numarul de ture jucate
        increaseTurns();

        setPlayerTurn(game.getStartGame().getStartingPlayer());

        // daca s-a terminat o runda, fiecare jucator trage o carte
        // si i se actualizeaza mana
        if (getTurns() % 2 == 0) {
            playerOne.addCardToHand();
            playerTwo.addCardToHand();

            int manaRound = Math.min(getTurns() / 2 + 1, 10);

            playerOne.setPlayerMana(playerOne.getPlayerMana() + manaRound);
            playerTwo.setPlayerMana(playerTwo.getPlayerMana() + manaRound);
        }
    }

    public void actionOutput(ObjectMapper objectMapper, ArrayNode output, Table table,
                             GameInput game, ActionsInput action, GamesStats gamesStats) {
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
                output.add(jsonOutput.generateOutput(action, table));
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
                Action.actionHeroUseAttack(action, table, playerOne, playerTwo, getPlayerTurn(),
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
        }
    }
}