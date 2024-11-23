package org.poo.main;

import org.poo.cardType.Hero;
import org.poo.fileio.ActionsInput;

/**
 * Class that handles errors in the game and returns the appropriate error message.
 */
public final class ErrorHandle {
    private ErrorHandle() {
    }

    /**
     * Method that verifies if there is a tank card on the table to attack.
     * This is used when attacking a card that is not a tank, as first the tank card must
     * be attacked before attacking other cards.
     * @param table the table where the game is being played
     * @param playerTurn the current player
     * @return an error message if there is a tank card on the table, and it's not attacked,
     * otherwise null
     */
    private static String errorAttackTank(final Table table, final int playerTurn) {
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

    /**
     * Method that verifies if there is any errors when using a card's ability.
     * @param attackerCard the card that is using the ability
     * @param attackedCard the card on which the ability is being used
     * @param playerTurn the current player
     * @param action the action that is being performed and the command
     * @param table the table where the game is being played
     * @return an error message if there is an error, otherwise null
     */
    public static String errorUseAbility(final GameCard attackerCard, final GameCard attackedCard,
                                                final int playerTurn, final ActionsInput action,
                                                final Table table) {
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

    /**
     * Method that verifies if there is any errors when attacking a card.
     * @param attackerCard the card that is attacking
     * @param attackedCard the card that is being attacked
     * @param playerTurn the current player
     * @param action the action that is being performed and the command
     * @param table the table where the game is being played
     * @return an error message if there is an error, otherwise null
     */
    public static String errorUseAttack(final GameCard attackerCard, final GameCard attackedCard,
                                                final int playerTurn, final ActionsInput action,
                                                final Table table) {
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

    /**
     * Method that verifies if there is any errors when using a hero's ability.
     * @param currentPlayer the current player
     * @param currentHero the hero that is using the ability
     * @param action the action that is being performed and the command
     * @param playerTurn the current player
     * @return an error message if there is an error, otherwise null
     */
    public static String errorHeroUseAbility(final Player currentPlayer, final Hero currentHero,
                                                    final ActionsInput action,
                                                    final int playerTurn) {
        String error = null;
        if (currentPlayer.getPlayerMana() < currentHero.getMana()) {
            error = "Not enough mana to use hero's ability.";
        } else if (currentHero.hasAttackedThisTurn()) {
            error = "Hero has already attacked this turn.";
        } else if (currentHero.getName().equals("Lord Royce")
                || currentHero.getName().equals("Empress Thorina")) {
            if (playerTurn == 1 && action.getAffectedRow() > 1) {
                error = "Selected row does not belong to the enemy.";
            } else if (playerTurn == 2 && action.getAffectedRow() < 2) {
                error = "Selected row does not belong to the enemy.";
            }
        } else if (currentHero.getName().equals("General Kocioraw")
                || currentHero.getName().equals("King Mudface")) {
            if (playerTurn == 1 && action.getAffectedRow() < 2) {
                error = "Selected row does not belong to the current player.";
            } else if (playerTurn == 2 && action.getAffectedRow() > 1) {
                error = "Selected row does not belong to the current player.";
            }
        }
        return error;
    }

    /**
     * Method that verifies if there is any errors when attacking a hero.
     * @param attackerCard the card that is attacking
     * @param table the table where the game is being played
     * @param playerTurn the current player
     * @return an error message if there is an error, otherwise null
     */
    public static String errorHeroUseAttack(final GameCard attackerCard, final Table table,
                                                    final int playerTurn) {
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
