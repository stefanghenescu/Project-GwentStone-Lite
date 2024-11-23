package org.poo.main;

import org.poo.cardType.Hero;
import org.poo.fileio.ActionsInput;

public class ErrorHandle {
    public static String errorAttackTank(Table table, int playerTurn) {
        String error = null;
        if (playerTurn == 1) {
            for (GameCard card : table.getRow(1)) {
                if (card.isTank()) {
                    error = "Attacked card is not of type 'Tank'.";
                    break;
                }
            }
        } else {
            for (GameCard card : table.getRow(2)) {
                if (card.isTank()) {
                    error = "Attacked card is not of type 'Tank'.";
                    break;
                }
            }
        }
        return error;
    }

    public static String errorUseAbility(GameCard attackerCard, GameCard attackedCard, int playerTurn,
                              ActionsInput action, Table table) {
        String error = null;
        if (attackerCard.isFrozen()) {
            error = "Attacker card is frozen.";
        } else if (attackerCard.hasAttackedThisTurn()) {
            error = "Attacker card has already attacked this turn.";
        } else if (attackerCard.getName().equals("Disciple")) {
            if (playerTurn == 1 && action.getCardAttacked().getX() < 2) {
                error = "Attacked card does not belong to the current player.";
            } else if (playerTurn == 2 && action.getCardAttacked().getX() > 1) {
                error = "Attacked card does not belong to the current player.";
            }
        } else {
            if (playerTurn == 1 && action.getCardAttacked().getX() > 1) {
                error = "Attacked card does not belong to the enemy.";
            } else if (playerTurn == 2 && action.getCardAttacked().getX() < 2) {
                error = "Attacked card does not belong to the enemy.";
            } else if (!attackedCard.isTank()) {
                error = errorAttackTank(table, playerTurn);
            }
        }
        return error;
    }

    public static String errorUseAttack(GameCard attackerCard, GameCard attackedCard, int playerTurn,
                                        ActionsInput action, Table table) {
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
            error = errorAttackTank(table, playerTurn);
        }
        return error;
    }

    public static String errorHeroUseAbility(Player currentPlayer, Hero currentHero,
                                          ActionsInput action, int playerTurn) {
        String error = null;
        if (currentPlayer.getPlayerMana() < currentHero.getMana()) {
            error = "Not enough mana to use hero's ability.";
        } else if (currentHero.hasAttackedThisTurn()) {
            error = "Hero has already attacked this turn.";
        } else if (currentHero.getName().equals("Lord Royce") ||
                currentHero.getName().equals("Empress Thorina")) {
            if (playerTurn == 1 && action.getAffectedRow() > 1) {
                error = "Selected row does not belong to the enemy.";
            } else if (playerTurn == 2 && action.getAffectedRow() < 2) {
                error = "Selected row does not belong to the enemy.";
            }
        } else if (currentHero.getName().equals("General Kocioraw") ||
                currentHero.getName().equals("King Mudface")) {
            if (playerTurn == 1 && action.getAffectedRow() < 2) {
                error = "Selected row does not belong to the current player.";
            } else if (playerTurn == 2 && action.getAffectedRow() > 1) {
                error = "Selected row does not belong to the current player.";
            }
        }
        return error;
    }

    public static String errorHeroUseAttack(GameCard attackerCard, Table table, int playerTurn) {
        String error = null;
        if (attackerCard.isFrozen()) {
            error = "Attacker card is frozen.";
        } else if (attackerCard.hasAttackedThisTurn()) {
            error = "Attacker card has already attacked this turn.";
        } else {
            error = ErrorHandle.errorAttackTank(table, playerTurn);
        }
        return error;
    }
}
