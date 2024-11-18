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
        if (action.getCommand().equals("getPlayerDeck")) {
            Action.actionDeck(playerOne, playerTwo, output, action, jsonOutput);
        } else if (action.getCommand().equals("getPlayerHero")) {
            Action.actionHero(playerOne, playerTwo, output, action, jsonOutput);
        } else if (action.getCommand().equals("getPlayerTurn")) {
            setPlayerTurn(game.getStartGame().getStartingPlayer());
            output.add(jsonOutput.generateOutput(action, getPlayerTurn()));
        } else if (action.getCommand().equals("getCardsInHand")) {
            Action.actionHand(playerOne, playerTwo, output, action, jsonOutput);
        } else if (action.getCommand().equals("endPlayerTurn")) {
            endTurn(table, game);
        } else if (action.getCommand().equals("placeCard")) {
            Player currentPlayer = getCurrentPlayer();
            Action.actionPlaceCard(currentPlayer, table, output, action, jsonOutput);
        } else if (action.getCommand().equals("getPlayerMana")) {
            Player player;
            if (action.getPlayerIdx() == 1) {
                player = playerOne;
            } else {
                player = playerTwo;
            }
            Action.actionGetMana(player, output, action, jsonOutput);
        } else if (action.getCommand().equals("getCardsOnTable")) {
            output.add(jsonOutput.generateOutput(action, table));
        } else if (action.getCommand().equals("cardUsesAttack")) {
            int attackerRow = action.getCardAttacker().getX();
            int attackerColumn = action.getCardAttacker().getY();
            GameCard attackerCard = table.getRow(attackerRow).get(attackerColumn);

            int attackedRow = action.getCardAttacked().getX();
            int attackedColumn = action.getCardAttacked().getY();
            GameCard attackedCard = table.getRow(attackedRow).get(attackedColumn);

            String error = null;

            if (getPlayerTurn() == 1 && action.getCardAttacked().getX() > 1) {
                error = "Attacked card does not belong to the enemy.";
            } else if (getPlayerTurn() == 2 && action.getCardAttacked().getX() < 2) {
                error = "Attacked card does not belong to the enemy.";
            } else if (attackerCard.isFrozen()) {
                error = "Attacker card is frozen.";
            }else if (attackerCard.hasAttackedThisTurn()) {
                error = "Attacker card has already attacked this turn.";
            }else if (!attackedCard.isTank()) {
                if (getPlayerTurn() == 1) {
                    // randul din fata al adversarului
                    // acolo se pun tank urile
                    for (GameCard card : table.getRow(1)) {
                        if (card.isTank()) {
                            error = "Attacked card is not of type 'Tank'.";
                            break;
                        }
                    }
                } else if (getPlayerTurn() == 2) {
                    // randul din fata al adversarului
                    // acolo se pun tank urile
                    for (GameCard card : table.getRow(2)) {
                        if (card.isTank()) {
                            error = "Attacked card is not of type 'Tank’.";
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
        } else if (action.getCommand().equals("getCardAtPosition")) {
            output.add(jsonOutput.generateOutputCardAtPosition(action, table));
        } else if (action.getCommand().equals("cardUsesAbility")) {
            int attackerRow = action.getCardAttacker().getX();
            int attackerColumn = action.getCardAttacker().getY();
            GameCard attackerCard = table.getRow(attackerRow).get(attackerColumn);

            int attackedRow = action.getCardAttacked().getX();
            int attackedColumn = action.getCardAttacked().getY();
            GameCard attackedCard = table.getRow(attackedRow).get(attackedColumn);

            String error = null;
            if (attackerCard.isFrozen()) {
                error = "Attacker card is frozen.";
            } else if (attackerCard.hasAttackedThisTurn()) {
                error = "Attacker card has already attacked this turn.";
            } else if (attackerCard.getName().equals("Disciple")) {
                if (getPlayerTurn() == 1 && action.getCardAttacked().getX() < 2) {
                    error = "Attacked card does not belong to the current player.";
                } else if (getPlayerTurn() == 2 && action.getCardAttacked().getX() > 1) {
                    error = "Attacked card does not belong to the current player.";
                }
            } else {
                if (getPlayerTurn() == 1 && action.getCardAttacked().getX() > 1) {
                    error = "Attacked card does not belong to the enemy.";
                } else if (getPlayerTurn() == 2 && action.getCardAttacked().getX() < 2) {
                    error = "Attacked card does not belong to the enemy.";
                } else if (!attackedCard.isTank()) {
                    if (getPlayerTurn() == 1) {
                        // randul din fata al adversarului
                        // acolo se pun tank urile
                        for (GameCard card : table.getRow(1)) {
                            if (card.isTank()) {
                                error = "Attacked card is not of type 'Tank'.";
                                break;
                            }
                        }
                    } else if (getPlayerTurn() == 2) {
                        // randul din fata al adversarului
                        // acolo se pun tank urile
                        for (GameCard card : table.getRow(2)) {
                            if (card.isTank()) {
                                error = "Attacked card is not of type 'Tank'.";
                                break;
                            }
                        }
                    }
                }
            }

            if (error == null) {
                attackerCard.useAbility(attackedCard);
                if (attackedCard.getHealth() == 0) {
                    table.removeCardFromTable(attackedRow, attackedColumn);
                }
                attackerCard.setAttackedThisTurn(true);
            } else {
                output.add(jsonOutput.generateOutputCoordinates(action, error));
            }
        } else if (action.getCommand().equals("useAttackHero")) {
            int attackerRow = action.getCardAttacker().getX();
            int attackerColumn = action.getCardAttacker().getY();
            GameCard attackerCard = table.getRow(attackerRow).get(attackerColumn);

            String error = null;
            if (attackerCard.isFrozen()) {
                error = "Attacker card is frozen.";
            } else if (attackerCard.hasAttackedThisTurn()) {
                error = "Attacker card has already attacked this turn.";
            } else {
                if (getPlayerTurn() == 1) {
                    // randul din fata al adversarului
                    // acolo se pun tank urile
                    for (GameCard card : table.getRow(1)) {
                        if (card.isTank()) {
                            error = "Attacked card is not of type 'Tank'.";
                            break;
                        }
                    }
                } else if (getPlayerTurn() == 2) {
                    // randul din fata al adversarului
                    // acolo se pun tank urile
                    for (GameCard card : table.getRow(2)) {
                        if (card.isTank()) {
                            error = "Attacked card is not of type 'Tank'.";
                            break;
                        }
                    }
                }
            }

            if (error == null) {
                int attackDamageAttacker = attackerCard.getAttackDamage();

                if (getPlayerTurn() == 1) {
                    // daca este tura primului jucator, atunci este atacat eroul jucatorului 2
                    int heroHealthPlayerTwo = playerTwo.getHero().getHealth();
                    playerTwo.getHero().setHealth(heroHealthPlayerTwo - attackDamageAttacker);

                    if (playerTwo.getHero().getHealth() <= 0) {
                        // jucatorul 1 a castigat, eroul este mort
                        gamesStats.incrementWinsPlayerOne();
                        commandNode.put("gameEnded", "Player one killed the enemy hero.");
                        output.add(commandNode);
                    }
                } else if (getPlayerTurn() == 2) {
                    // daca este tura celui de al doilea jucator, atunci este atacat eroul
                    // jucatorului 1
                    int heroHealthPlayerOne = playerOne.getHero().getHealth();
                    playerOne.getHero().setHealth(heroHealthPlayerOne - attackDamageAttacker);

                    if (playerOne.getHero().getHealth() <= 0) {
                        // jucatorul 2 a castigat, eroul este mort
                        gamesStats.incrementWinsPlayerTwo();
                        commandNode.put("gameEnded", "Player two killed the enemy hero.");
                        output.add(commandNode);
                    }
                }
                attackerCard.setAttackedThisTurn(true);
            } else {
                ObjectNode coordinatesAttacker = objectMapper.createObjectNode();
                coordinatesAttacker.put("x", action.getCardAttacker().getX());
                coordinatesAttacker.put("y", action.getCardAttacker().getY());

                commandNode.put("command", action.getCommand());
                commandNode.set("cardAttacker", coordinatesAttacker);
                commandNode.put("error", error);
                output.add(commandNode);
            }
        } else if (action.getCommand().equals("useHeroAbility")) {
            String error = null;
            Player currentPlayer = getCurrentPlayer();

            Hero currentHero = currentPlayer.getHero();
            if (currentPlayer.getPlayerMana() < currentHero.getMana()) {
                error = "Not enough mana to use hero's ability.";
            } else if (currentHero.hasAttackedThisTurn()) {
                error = "Hero has already attacked this turn.";
            } else if (currentHero.getName().equals("Lord Royce") ||
                    currentHero.getName().equals("Empress Thorina")) {
                // verific daca randul selectat este al adversarului
                if (getPlayerTurn() == 1 && action.getAffectedRow() > 1) {
                    error = "Selected row does not belong to the enemy.";
                } else if (getPlayerTurn() == 2 && action.getAffectedRow() < 2) {
                    error = "Selected row does not belong to the enemy.";
                }
            } else if (currentHero.getName().equals("General Kocioraw") ||
                    currentHero.getName().equals("King Mudface")) {
                if (getPlayerTurn() == 1 && action.getAffectedRow() < 2) {
                    error = "Selected row does not belong to the current player.";
                } else if (getPlayerTurn() == 2 && action.getAffectedRow() > 1) {
                    error = "Selected row does not belong to the current player.";
                }
            }

            if (error == null) {
                int rowIndex = action.getAffectedRow();
                currentHero.useAbility(rowIndex, table);

                currentPlayer.setPlayerMana(currentPlayer.getPlayerMana() - currentHero.getMana());
                currentHero.setAttackedThisTurn(true);
            } else {
                output.add(jsonOutput.generateOutputAffectedRow(action, error));
            }
        } else if (action.getCommand().equals("getFrozenCardsOnTable")) {
            output.add(jsonOutput.generateOutputFrozenCards(action, table));
        } else if (action.getCommand().equals("getPlayerOneWins")) {
            output.add(jsonOutput.generateOutput(action, gamesStats.getWinsPlayerOne()));
        } else if (action.getCommand().equals("getPlayerTwoWins")) {
            output.add(jsonOutput.generateOutput(action, gamesStats.getWinsPlayerTwo()));
        } else if (action.getCommand().equals("getTotalGamesPlayed")) {
            output.add(jsonOutput.generateOutput(action, gamesStats.getGamesPlayed()));
        }
    }
}