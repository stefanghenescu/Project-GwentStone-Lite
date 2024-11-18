package org.poo.main;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.fileio.ActionsInput;

import java.util.ArrayList;

public class Action {
    public static void actionDeck(Player playerOne, Player playerTwo, ArrayNode output,
                                  ActionsInput action, JsonOutput jsonOutput) {
        ArrayList<GameCard> deck;
        if (action.getPlayerIdx() == 1) {
            deck = playerOne.getDeck();
        } else {
            deck = playerTwo.getDeck();
        }
        output.add(jsonOutput.generateOutput(action, deck));
    }

    public static void actionHero(Player playerOne, Player playerTwo, ArrayNode output,
                                  ActionsInput action, JsonOutput jsonOutput) {
        Hero hero;
        if (action.getPlayerIdx() == 1) {
            hero = playerOne.getHero();
        } else {
            hero = playerTwo.getHero();
        }
        output.add(jsonOutput.generateOutput(action, hero));
    }

    public static void actionHand(Player playerOne, Player playerTwo, ArrayNode output,
                                  ActionsInput action, JsonOutput jsonOutput) {
        ArrayList<GameCard> hand;
        if (action.getPlayerIdx() == 1) {
            hand = playerOne.getHand();
        } else {
            hand = playerTwo.getHand();
        }

        output.add(jsonOutput.generateOutput(action, hand));
    }

    public static void actionPlaceCard(Player currentPlayer, Table table, ArrayNode output,
                                       ActionsInput action, JsonOutput jsonOutput) {
        String error;
        GameCard cardFromHand = currentPlayer.getHand().get(action.getHandIdx());
        error = table.addCardToTable(currentPlayer, cardFromHand);
        if (error == null) {
            currentPlayer.setPlayerMana(currentPlayer.getPlayerMana() - cardFromHand.getMana());
            currentPlayer.getHand().remove(action.getHandIdx());
        } else {
            output.add(jsonOutput.generateOutput(action, error));
        }
    }

    public static void actionGetMana(Player player, ArrayNode output,
                                     ActionsInput action, JsonOutput jsonOutput) {
        output.add(jsonOutput.generateOutput(action, player));
    }

    public static void actionUseAttack(ActionsInput action, Table table, int playerTurn,
                                       ArrayNode output, JsonOutput jsonOutput) {
        int attackerRow = action.getCardAttacker().getX();
        int attackerColumn = action.getCardAttacker().getY();
        GameCard attackerCard = table.getRow(attackerRow).get(attackerColumn);

        int attackedRow = action.getCardAttacked().getX();
        int attackedColumn = action.getCardAttacked().getY();
        GameCard attackedCard = table.getRow(attackedRow).get(attackedColumn);

        String error = null;

        if (playerTurn == 1 && action.getCardAttacked().getX() > 1) {
            error = "Attacked card does not belong to the enemy.";
        } else if (playerTurn == 2 && action.getCardAttacked().getX() < 2) {
            error = "Attacked card does not belong to the enemy.";
        } else if (attackerCard.isFrozen()) {
            error = "Attacker card is frozen.";
        } else if (attackerCard.hasAttackedThisTurn()) {
            error = "Attacker card has already attacked this turn.";
        } else if (!attackedCard.isTank()) {
            if (playerTurn == 1) {
                for (GameCard card : table.getRow(1)) {
                    if (card.isTank()) {
                        error = "Attacked card is not of type 'Tank'.";
                        break;
                    }
                }
            } else if (playerTurn == 2) {
                for (GameCard card : table.getRow(2)) {
                    if (card.isTank()) {
                        error = "Attacked card is not of type 'Tank'.";
                        break;
                    }
                }
            }
        }

        if (error == null) {
            attackedCard.setHealth(attackedCard.getHealth() - attackerCard.getAttackDamage());
            if (attackedCard.getHealth() <= 0) {
                table.removeCardFromTable(attackedRow, attackedColumn);
            }
            attackerCard.setAttackedThisTurn(true);
        } else {
            output.add(jsonOutput.generateOutputCoordinates(action, error));
        }
    }
}
