package org.poo.actionhandle;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.cardtype.Hero;
import org.poo.fileio.ActionsInput;
import org.poo.gamecomponents.GameCard;
import org.poo.gamecomponents.GamesStats;
import org.poo.gamecomponents.Player;
import org.poo.gamecomponents.Table;

import java.util.ArrayList;

/**
 * This class represents the actions that can be done in the game
 */
public final class Action {
    private Action() {
    }

    /**
     * This method is used to get the deck of a player and add it to the output as a JSON object
     * @param playerOne the first player
     * @param playerTwo the second player
     * @param output the output array for the JSON file
     * @param action the action input and the command message
     * @param jsonOutput the JSON output object
     */
    public static void actionDeck(final Player playerOne, final Player playerTwo,
                                  final ArrayNode output, final ActionsInput action,
                                  final JsonOutput jsonOutput) {
        ArrayList<GameCard> deck;
        if (action.getPlayerIdx() == 1) {
            deck = playerOne.getDeck();
        } else {
            deck = playerTwo.getDeck();
        }
        output.add(jsonOutput.generateOutputCards(action, deck));
    }

    /**
     * This method is used to get the hero of a player and add it to the output as a JSON object
     * @param playerOne the first player
     * @param playerTwo the second player
     * @param output the output array for the JSON file
     * @param action the action input and the command message
     * @param jsonOutput the JSON output object
     */
    public static void actionHero(final Player playerOne, final Player playerTwo,
                                      final ArrayNode output, final ActionsInput action,
                                      final JsonOutput jsonOutput) {
        Hero hero;
        if (action.getPlayerIdx() == 1) {
            hero = playerOne.getHero();
        } else {
            hero = playerTwo.getHero();
        }
        output.add(jsonOutput.generateOutputHero(action, hero));
    }

    /**
     * This method is used to get the hand of a player and add it to the output as a JSON object
     * @param playerOne the first player
     * @param playerTwo the second player
     * @param output the output array for the JSON file
     * @param action the action input and the command message
     * @param jsonOutput the JSON output object
     */
    public static void actionHand(final Player playerOne, final Player playerTwo,
                                      final ArrayNode output, final ActionsInput action,
                                      final JsonOutput jsonOutput) {
        ArrayList<GameCard> hand;
        if (action.getPlayerIdx() == 1) {
            hand = playerOne.getHand();
        } else {
            hand = playerTwo.getHand();
        }

        output.add(jsonOutput.generateOutputCards(action, hand));
    }

    /**
     * This method is used to place a card on the table
     * @param currentPlayer the current player
     * @param table the table where the game is being played
     * @param output the output array for the JSON file
     * @param action the action input and the command message
     * @param jsonOutput the JSON output object
     */
    public static void actionPlaceCard(final Player currentPlayer, final Table table,
                                            final ArrayNode output, final ActionsInput action,
                                            final JsonOutput jsonOutput) {
        String error;
        // get the card from the hand
        GameCard cardFromHand = currentPlayer.getHand().get(action.getHandIdx());

        // add the card to the table
        error = table.addCardToTable(currentPlayer, cardFromHand);

        // if the card was added to the table, remove it from the hand
        if (error == null) {
            // update the player mana
            currentPlayer.setPlayerMana(currentPlayer.getPlayerMana() - cardFromHand.getMana());
            currentPlayer.getHand().remove(action.getHandIdx());
        } else {
            output.add(jsonOutput.generateOutputTableError(action, error));
        }
    }

    /**
     * This method is used to get the mana of a player and add it to the output as a JSON object
     * @param player the player
     * @param output the output array for the JSON file
     * @param action the action input and the command message
     * @param jsonOutput the JSON output object
     */
    public static void actionGetMana(final Player player, final ArrayNode output,
                                        final ActionsInput action, final JsonOutput jsonOutput) {
        output.add(jsonOutput.generateOutputMana(action, player));
    }

    /**
     * This method is used to attack a card with another card
     * @param action the action input and the command message
     * @param table  the table where the game is being played
     * @param playerTurn the player that is attacking
     * @param output the output array for the JSON file
     * @param jsonOutput the JSON output object
     */
    public static void actionUseAttack(final ActionsInput action, final Table table,
                                            final int playerTurn, final ArrayNode output,
                                            final JsonOutput jsonOutput) {
        // get the attacker card
        int attackerRow = action.getCardAttacker().getX();
        int attackerColumn = action.getCardAttacker().getY();
        GameCard attackerCard = table.getRow(attackerRow).get(attackerColumn);

        // get the attacked card
        int attackedRow = action.getCardAttacked().getX();
        int attackedColumn = action.getCardAttacked().getY();
        GameCard attackedCard = table.getRow(attackedRow).get(attackedColumn);

        // check if the attack is valid
        String error = ErrorHandle.errorUseAttack(attackerCard, attackedCard, playerTurn,
                                                    action, table);

        if (error == null) {
            // update the health of the attacked card
            attackedCard.setHealth(attackedCard.getHealth() - attackerCard.getAttackDamage());

            // remove the attacked card from the table if it's health is 0
            if (attackedCard.getHealth() <= 0) {
                table.removeCardFromTable(attackedRow, attackedColumn);
            }
            attackerCard.setAttackedThisTurn(true);
        } else {
            // add the error to the output if the attack can't be done
            output.add(jsonOutput.generateErrorCardAttack(action, error));
        }
    }

    /**
     * This method uses the ability of a card on another card
     * @param action the action input and the command message
     * @param table the table where the game is being played
     * @param playerTurn the player that is attacking
     * @param output the output array for the JSON file
     * @param jsonOutput the JSON output object
     */
    public static void
    actionUseAbility(final ActionsInput action, final Table table, final int playerTurn,
                            final ArrayNode output, final JsonOutput jsonOutput) {
        // get the attacker card
        int attackerRow = action.getCardAttacker().getX();
        int attackerColumn = action.getCardAttacker().getY();
        GameCard attackerCard = table.getRow(attackerRow).get(attackerColumn);

        // get the attacked card
        int attackedRow = action.getCardAttacked().getX();
        int attackedColumn = action.getCardAttacked().getY();
        GameCard attackedCard = table.getRow(attackedRow).get(attackedColumn);

        // check if the ability can be used
        String error = ErrorHandle.errorUseAbility(attackerCard, attackedCard, playerTurn,
                                                    action, table);

        if (error == null) {
            // use the ability
            attackerCard.useAbility(attackedCard);

            // remove the attacked card from the table if it's health is 0
            if (attackedCard.getHealth() == 0) {
                table.removeCardFromTable(attackedRow, attackedColumn);
            }
            attackerCard.setAttackedThisTurn(true);
        } else {
            // add the error to the output if the ability can't be used
            output.add(jsonOutput.generateErrorCardAttack(action, error));
        }
    }

    /**
     * This method uses the attack on a hero and sees is the game is over
     * @param action the action input and the command message
     * @param table the table where the game is being played
     * @param playerOne the first player
     * @param playerTwo the second player
     * @param playerTurn the player that is attacking
     * @param output the output array for the JSON file
     * @param gamesStats the game stats over all the games
     * @param jsonOutput the JSON output object
     */
    public static void
    actionUseAttackOnHero(final ActionsInput action, final Table table, final Player playerOne,
                          final Player playerTwo, final int playerTurn, final ArrayNode output,
                          final GamesStats gamesStats, final JsonOutput jsonOutput) {
        // get the attacker card
        int attackerRow = action.getCardAttacker().getX();
        int attackerColumn = action.getCardAttacker().getY();
        GameCard attackerCard = table.getRow(attackerRow).get(attackerColumn);

        // check if the attack is valid
        String error = ErrorHandle.errorHeroUseAttack(attackerCard, table, playerTurn);

        if (error == null) {
            int attackDamageAttacker = attackerCard.getAttackDamage();
            int playerKilled = 0;

            if (playerTurn == 1) {
                int heroHealthPlayerTwo = playerTwo.getHero().getHealth();
                // update the health of the attacked hero
                playerTwo.getHero().setHealth(heroHealthPlayerTwo - attackDamageAttacker);

                // check if the hero is dead
                if (playerTwo.getHero().getHealth() <= 0) {
                    gamesStats.incrementWinsPlayerOne();
                    playerKilled = 1;
                }
            } else if (playerTurn == 2) {
                int heroHealthPlayerOne = playerOne.getHero().getHealth();
                // update the health of the attacked hero
                playerOne.getHero().setHealth(heroHealthPlayerOne - attackDamageAttacker);

                // check if the hero is dead
                if (playerOne.getHero().getHealth() <= 0) {
                    gamesStats.incrementWinsPlayerTwo();
                    playerKilled = 2;
                }
            }
            // add the game stats to the output if the game is over
            if (playerKilled != 0) {
                output.add(jsonOutput.generateOutputGameEnd(playerKilled));
            }
            attackerCard.setAttackedThisTurn(true);
        } else {
            // add the error to the output if the attack can't be done
            output.add(jsonOutput.generateOutputErrorAttackOnHero(action, error));
        }
    }

    /**
     * This method uses the ability of a hero on a row
     * @param currentPlayer the current player that is using the ability
     * @param action the action input and the command message
     * @param playerTurn the player that is attacking
     * @param table the table where the game is being played
     * @param output the output array for the JSON file
     * @param jsonOutput the JSON output object
     */
    public static void
    actionHeroUseAbility(final Player currentPlayer, final ActionsInput action,
                             final int playerTurn, final Table table,
                             final ArrayNode output, final JsonOutput jsonOutput) {
        String error = null;
        Hero currentHero = currentPlayer.getHero();
        // check if the ability can be used
        error = ErrorHandle.errorHeroUseAbility(currentPlayer, currentHero, action, playerTurn);

        if (error == null) {
            int rowIndex = action.getAffectedRow();
            // use the ability
            currentHero.useAbility(rowIndex, table);

            // update the mana of the player
            currentPlayer.setPlayerMana(currentPlayer.getPlayerMana() - currentHero.getMana());
            currentHero.setAttackedThisTurn(true);
        } else {
            // add the error to the output if the ability can't be used
            output.add(jsonOutput.generateErrorAbility(action, error));
        }
    }
}
