package org.poo.main;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.cardType.Hero;
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

        String error = ErrorHandle.errorUseAttack(attackerCard, attackedCard, playerTurn,
                                                    action, table);

        if (error == null) {
            attackedCard.setHealth(attackedCard.getHealth() - attackerCard.getAttackDamage());
            if (attackedCard.getHealth() <= 0) {
                table.removeCardFromTable(attackedRow, attackedColumn);
            }
            attackerCard.setAttackedThisTurn(true);
        } else {
            output.add(jsonOutput.generateErrorCardAttack(action, error));
        }
    }

    public static void
    actionUseAbility(ActionsInput action, Table table, int playerTurn, ArrayNode output,
                     JsonOutput jsonOutput) {
        int attackerRow = action.getCardAttacker().getX();
        int attackerColumn = action.getCardAttacker().getY();
        GameCard attackerCard = table.getRow(attackerRow).get(attackerColumn);

        int attackedRow = action.getCardAttacked().getX();
        int attackedColumn = action.getCardAttacked().getY();
        GameCard attackedCard = table.getRow(attackedRow).get(attackedColumn);

        String error = ErrorHandle.errorUseAbility(attackerCard, attackedCard, playerTurn,
                                                    action, table);

        if (error == null) {
            attackerCard.useAbility(attackedCard);
            if (attackedCard.getHealth() == 0) {
                table.removeCardFromTable(attackedRow, attackedColumn);
            }
            attackerCard.setAttackedThisTurn(true);
        } else {
            output.add(jsonOutput.generateErrorCardAttack(action, error));
        }
    }

    public static void
    actionHeroUseAttack(ActionsInput action, Table table, Player playerOne, Player playerTwo,
                        int playerTurn, ArrayNode output, GamesStats gamesStats,
                        JsonOutput jsonOutput) {
        int attackerRow = action.getCardAttacker().getX();
        int attackerColumn = action.getCardAttacker().getY();
        GameCard attackerCard = table.getRow(attackerRow).get(attackerColumn);

        String error = ErrorHandle.errorHeroUseAttack(attackerCard, table, playerTurn);

        if (error == null) {
            int attackDamageAttacker = attackerCard.getAttackDamage();
            int playerKilled = 0;

            if (playerTurn == 1) {
                int heroHealthPlayerTwo = playerTwo.getHero().getHealth();
                playerTwo.getHero().setHealth(heroHealthPlayerTwo - attackDamageAttacker);

                if (playerTwo.getHero().getHealth() <= 0) {
                    gamesStats.incrementWinsPlayerOne();
                    playerKilled = 1;
                }
            } else if (playerTurn == 2) {
                int heroHealthPlayerOne = playerOne.getHero().getHealth();
                playerOne.getHero().setHealth(heroHealthPlayerOne - attackDamageAttacker);

                if (playerOne.getHero().getHealth() <= 0) {
                    gamesStats.incrementWinsPlayerTwo();
                    playerKilled = 2;
                }
            }
            if (playerKilled != 0) {
                output.add(jsonOutput.generateOutputGameStats(playerKilled));
            }
            attackerCard.setAttackedThisTurn(true);
        } else {
            output.add(jsonOutput.generateOutputErrorHeroAttack(action, error));
        }
    }

    public static void
    actionHeroUseAbility(Player currentPlayer, ActionsInput action, int playerTurn, Table table,
                         ArrayNode output, JsonOutput jsonOutput) {
        String error = null;
        Hero currentHero = currentPlayer.getHero();
        error = ErrorHandle.errorHeroUseAbility(currentPlayer, currentHero, action, playerTurn);

        if (error == null) {
            int rowIndex = action.getAffectedRow();
            currentHero.useAbility(rowIndex, table);

            currentPlayer.setPlayerMana(currentPlayer.getPlayerMana() - currentHero.getMana());
            currentHero.setAttackedThisTurn(true);
        } else {
            output.add(jsonOutput.generateErrorAffectedRow(action, error));
        }
    }
}
