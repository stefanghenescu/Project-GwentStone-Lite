package main;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import fileio.CardInput;
import fileio.GameInput;

import javax.smartcardio.Card;
import java.util.ArrayList;

public final class Game {
    private int turns;
    private int playerTurn;

    public Game() {
        this.turns = 0;
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

    public void setPlayerTurn(int playerTurn) {
        this.playerTurn = playerTurn;
    }

    public void actionOutput(ObjectMapper objectMapper, ArrayNode output, Table table,
                             GameInput game, Player playerOne, Player playerTwo,
                             ActionsInput action) {
        ObjectNode commandNode = objectMapper.createObjectNode();

        if (action.getCommand().equals("getPlayerDeck")) {
            commandNode.put("command", action.getCommand());
            commandNode.put("playerIdx", action.getPlayerIdx());
            ArrayList<CardInput> deck;
            if (action.getPlayerIdx() == 1) {
                deck = playerOne.getDeck();
            } else {
                deck = playerTwo.getDeck();
            }

            ArrayNode deckArray = objectMapper.createArrayNode();
            for (CardInput card : deck) {
                ObjectNode cardNode = createCardNode(card, objectMapper);
                deckArray.add(cardNode);
            }
            commandNode.set("output", deckArray);
            output.add(commandNode);
        } else if (action.getCommand().equals("getPlayerHero")) {
            commandNode.put("command", action.getCommand());
            commandNode.put("playerIdx", action.getPlayerIdx());
            if (action.getPlayerIdx() == 1) {
                ObjectNode heroNode = createHeroNode(playerOne.getHero(), objectMapper);
                commandNode.set("output", heroNode);
            } else {
                ObjectNode heroNode = createHeroNode(playerTwo.getHero(), objectMapper);
                commandNode.set("output", heroNode);
            }
            output.add(commandNode);
        } else if (action.getCommand().equals("getPlayerTurn")) {
            commandNode.put("command", action.getCommand());
            if (getTurns() % 2 == 0) {
               setPlayerTurn(game.getStartGame().getStartingPlayer());
               commandNode.put("output", game.getStartGame().getStartingPlayer());
            } else {
                /* xor intre 11(baza 2) = 3(baza 10) si index startingPlayer
                3 ^ 1 = 2
                3 ^ 2 = 1
                daca numarul de ture este impar atunci e randul celuilalt jucator fata
                de cel ce a inceput jocul */
                setPlayerTurn(3 ^ game.getStartGame().getStartingPlayer());
                commandNode.put("output", getPlayerTurn());
            }
            output.add(commandNode);
        } else if (action.getCommand().equals("getCardsInHand")) {
            commandNode.put("command", action.getCommand());
            commandNode.put("playerIdx", action.getPlayerIdx());
            ArrayList<CardInput> hand;
            if (action.getPlayerIdx() == 1) {
                hand = playerOne.getHand();
            } else {
                hand = playerTwo.getHand();
            }

            ArrayNode handArray = objectMapper.createArrayNode();
            for (CardInput card : hand) {
                ObjectNode cardNode = createCardNode(card, objectMapper);
                handArray.add(cardNode);
            }
            commandNode.set("output", handArray);
            output.add(commandNode);
        } else if (action.getCommand().equals("endPlayerTurn")) {
            // verific daca exista carti frozen si le schimb flag-ul
            int playerIdx = getPlayerTurn();
            Player.defrostCards(playerIdx, table);

            // le resetez flag-ul cartilor care au atacat tura aceasta
            Player.rechargeCardsAttack(table);

            if (getPlayerTurn() == 1) {
                playerOne.getHero().setAttackedThisTurn(false);
            } else if (getPlayerTurn() == 2) {
                playerTwo.getHero().setAttackedThisTurn(false);
            }

            // incrementez numarul de ture jucate
            increaseTurns();

            if (getTurns() % 2 == 0) {
                setPlayerTurn(game.getStartGame().getStartingPlayer());
            } else {
                /* xor intre 11(baza 2) = 3(baza 10) si index startingPlayer
                3 ^ 1 = 2
                3 ^ 2 = 1
                daca numarul de ture este impar atunci e randul celuilalt jucator fata
                de cel ce a inceput jocul */
                setPlayerTurn(3 ^ game.getStartGame().getStartingPlayer());
            }

            // daca s-a terminat o runda, fiecare jucator trage o carte
            // si i se actualizeaza mana
            if (getTurns() % 2 == 0) {
                playerOne.addCardToHand();
                playerTwo.addCardToHand();

                int manaRound = getTurns() / 2 + 1;
                if (manaRound > 10) {
                    manaRound = 10;
                }
                playerOne.setPlayerMana(playerOne.getPlayerMana() + manaRound);
                playerTwo.setPlayerMana(playerTwo.getPlayerMana() + manaRound);
            }
        } else if (action.getCommand().equals("placeCard")) {
            String error;
            if (getPlayerTurn() == 1) {
                CardInput cardFromHand = playerOne.getHand().get(action.getHandIdx());
                error = table.addCardToTable(playerOne, cardFromHand);
                if (error == null) {
                    playerOne.setPlayerMana(playerOne.getPlayerMana() - cardFromHand.getMana());
                    playerOne.getHand().remove(action.getHandIdx());
                }
            } else {
                CardInput cardFromHand = playerTwo.getHand().get(action.getHandIdx());
                error = table.addCardToTable(playerTwo, cardFromHand);
                if (error == null) {
                    playerTwo.setPlayerMana(playerTwo.getPlayerMana() - cardFromHand.getMana());
                    playerTwo.getHand().remove(action.getHandIdx());
                }
            }
            if (error != null) {
                commandNode.put("command", action.getCommand());
                commandNode.put("handIdx", action.getHandIdx());
                commandNode.put("error", error);
                output.add(commandNode);
            }
        } else if (action.getCommand().equals("getPlayerMana")) {
            commandNode.put("command", action.getCommand());
            commandNode.put("playerIdx", action.getPlayerIdx());
            if (action.getPlayerIdx() == 1) {
                commandNode.put("output", playerOne.getPlayerMana());
            } else {
                commandNode.put("output", playerTwo.getPlayerMana());
            }
            output.add(commandNode);
        } else if (action.getCommand().equals("getCardsOnTable")) {
            commandNode.put("command", action.getCommand());
            ArrayNode cardsOnTable = objectMapper.createArrayNode();

            for (int rowIndex = 0; rowIndex < 4; rowIndex++) {
                ArrayNode rowNode = objectMapper.createArrayNode();
                ArrayList<CardInput> row = table.getRow(rowIndex);

                for (CardInput card : row) {
                    ObjectNode cardNode = createCardNode(card, objectMapper);
                    rowNode.add(cardNode);
                }
                cardsOnTable.add(rowNode);
            }
            commandNode.set("output", cardsOnTable);
            output.add(commandNode);
        } else if (action.getCommand().equals("cardUsesAttack")) {
            int attackerRow = action.getCardAttacker().getX();
            int attackerColumn = action.getCardAttacker().getY();
            CardInput attackerCard = table.getRow(attackerRow).get(attackerColumn);

            int attackedRow = action.getCardAttacked().getX();
            int attackedColumn = action.getCardAttacked().getY();
            CardInput attackedCard = table.getRow(attackedRow).get(attackedColumn);

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
                    for (CardInput card : table.getRow(1)) {
                        if (card.isTank()) {
                            error = "Attacked card is not of type 'Tank'.";
                            break;
                        }
                    }
                } else if (getPlayerTurn() == 2) {
                    // randul din fata al adversarului
                    // acolo se pun tank urile
                    for (CardInput card : table.getRow(2)) {
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
                ObjectNode coordinatesAttacker = objectMapper.createObjectNode();
                coordinatesAttacker.put("x", action.getCardAttacker().getX());
                coordinatesAttacker.put("y", action.getCardAttacker().getY());

                ObjectNode coordinatesAttacked = objectMapper.createObjectNode();
                coordinatesAttacked.put("x", action.getCardAttacked().getX());
                coordinatesAttacked.put("y", action.getCardAttacked().getY());

                commandNode.put("command", action.getCommand());
                commandNode.set("cardAttacker", coordinatesAttacker);
                commandNode.set("cardAttacked", coordinatesAttacked);
                commandNode.put("error", error);
                output.add(commandNode);
            }
        } else if (action.getCommand().equals("getCardAtPosition")) {
            commandNode.put("command", action.getCommand());
            commandNode.put("x", action.getX());
            commandNode.put("y", action.getY());

            String error = null;
            if (table.getRow(action.getX()).size() <= action.getY()) {
                error = "No card available at that position.";
                commandNode.put("output", error);
            } else {
                CardInput card = table.getRow(action.getX()).get(action.getY());
                ObjectNode cardNode = createCardNode(card, objectMapper);
                commandNode.set("output", cardNode);
            }
            output.add(commandNode);
        } else if (action.getCommand().equals("cardUsesAbility")) {
            int attackerRow = action.getCardAttacker().getX();
            int attackerColumn = action.getCardAttacker().getY();
            CardInput attackerCard = table.getRow(attackerRow).get(attackerColumn);

            int attackedRow = action.getCardAttacked().getX();
            int attackedColumn = action.getCardAttacked().getY();
            CardInput attackedCard = table.getRow(attackedRow).get(attackedColumn);

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
                        for (CardInput card : table.getRow(1)) {
                            if (card.isTank()) {
                                error = "Attacked card is not of type 'Tank'.";
                                break;
                            }
                        }
                    } else if (getPlayerTurn() == 2) {
                        // randul din fata al adversarului
                        // acolo se pun tank urile
                        for (CardInput card : table.getRow(2)) {
                            if (card.isTank()) {
                                error = "Attacked card is not of type 'Tank'.";
                                break;
                            }
                        }
                    }
                }
            }

            if (error == null) {
                if (attackerCard.getName().equals("Disciple")) {
                    attackedCard.setHealth(attackedCard.getHealth() + 2);

                    attackerCard.setAttackedThisTurn(true);
                } else if (attackerCard.getName().equals("Miraj")) {
                    int healthAttacker = attackerCard.getHealth();
                    int healthAttacked = attackedCard.getHealth();

                    attackerCard.setHealth(healthAttacked);
                    attackedCard.setHealth(healthAttacker);

                    attackerCard.setAttackedThisTurn(true);
                } else if (attackerCard.getName().equals("The Ripper")) {
                    int attackDamageAttacked = attackedCard.getAttackDamage();
                    attackedCard.setAttackDamage(attackDamageAttacked - 2);
                    if (attackedCard.getAttackDamage() <= 0) {
                        attackedCard.setAttackDamage(0);
                    }

                    attackerCard.setAttackedThisTurn(true);
                } else if (attackerCard.getName().equals("The Cursed One")) {
                    int healthAttacked = attackedCard.getHealth();
                    int attackDamageAttacked = attackedCard.getAttackDamage();

                    attackedCard.setHealth(attackDamageAttacked);
                    attackedCard.setAttackDamage(healthAttacked);

                    if (attackedCard.getHealth() == 0) {
                        table.removeCardFromTable(attackedRow, attackedColumn);
                    }

                    attackerCard.setAttackedThisTurn(true);
                }
            } else {
                ObjectNode coordinatesAttacker = objectMapper.createObjectNode();
                coordinatesAttacker.put("x", action.getCardAttacker().getX());
                coordinatesAttacker.put("y", action.getCardAttacker().getY());

                ObjectNode coordinatesAttacked = objectMapper.createObjectNode();
                coordinatesAttacked.put("x", action.getCardAttacked().getX());
                coordinatesAttacked.put("y", action.getCardAttacked().getY());

                commandNode.put("command", action.getCommand());
                commandNode.set("cardAttacker", coordinatesAttacker);
                commandNode.set("cardAttacked", coordinatesAttacked);
                commandNode.put("error", error);
                output.add(commandNode);
            }
        } else if (action.getCommand().equals("useAttackHero")) {
            int attackerRow = action.getCardAttacker().getX();
            int attackerColumn = action.getCardAttacker().getY();
            CardInput attackerCard = table.getRow(attackerRow).get(attackerColumn);

            String error = null;
            if (attackerCard.isFrozen()) {
                error = "Attacker card is frozen.";
            } else if (attackerCard.hasAttackedThisTurn()) {
                error = "Attacker card has already attacked this turn.";
            } else {
                if (getPlayerTurn() == 1) {
                    // randul din fata al adversarului
                    // acolo se pun tank urile
                    for (CardInput card : table.getRow(1)) {
                        if (card.isTank()) {
                            error = "Attacked card is not of type 'Tank'.";
                            break;
                        }
                    }
                } else if (getPlayerTurn() == 2) {
                    // randul din fata al adversarului
                    // acolo se pun tank urile
                    for (CardInput card : table.getRow(2)) {
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
            Player currentPlayer;
            if (getPlayerTurn() == 1) {
                currentPlayer = playerOne;
            } else {
                currentPlayer = playerTwo;
            }

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
                if (currentHero.getName().equals("Lord Royce")) {
                    for (CardInput card : table.getRow(rowIndex)) {
                        card.setFrozen(true);
                    }
                } else if (currentHero.getName().equals("Empress Thorina")) {
                    CardInput destroyCard = table.getRow(rowIndex).get(0);
                    for (CardInput card : table.getRow(rowIndex)) {
                        if (card.getHealth() > destroyCard.getHealth()) {
                            destroyCard = card;
                        }
                    }
                    table.removeCardFromTable(rowIndex,
                            table.getRow(rowIndex).indexOf(destroyCard));
                } else if (currentHero.getName().equals("General Kocioraw")) {
                    for (CardInput card : table.getRow(rowIndex)) {
                        card.setAttackDamage(card.getAttackDamage() + 1);
                    }
                } else if (currentHero.getName().equals("King Mudface")) {
                    for (CardInput card : table.getRow(rowIndex)) {
                        card.setHealth(card.getHealth() + 1);
                    }
                }
                currentPlayer.setPlayerMana(currentPlayer.getPlayerMana() - currentHero.getMana());
                currentHero.setAttackedThisTurn(true);
            } else {
                commandNode.put("command", action.getCommand());
                commandNode.put("affectedRow", action.getAffectedRow());
                commandNode.put("error", error);
                output.add(commandNode);
            }
        } else if (action.getCommand().equals("getFrozenCardsOnTable")) {
            commandNode.put("command", action.getCommand());
            ArrayNode frozenCardsOnTable = objectMapper.createArrayNode();

            for (int rowIndex = 0; rowIndex < 4; rowIndex++) {
                ArrayNode rowNode = objectMapper.createArrayNode();
                ArrayList<CardInput> row = table.getRow(rowIndex);

                for (CardInput card : row) {
                    if (card.isFrozen()) {
                        ObjectNode cardNode = createCardNode(card, objectMapper);
                        frozenCardsOnTable.add(cardNode);
                    }
                }
            }
            commandNode.set("output", frozenCardsOnTable);
            output.add(commandNode);
        }
    }


    public static ObjectNode createCardNode(CardInput card, ObjectMapper mapper) {
        ObjectNode cardNode = mapper.createObjectNode();
        cardNode.put("mana", card.getMana());
        cardNode.put("attackDamage", card.getAttackDamage());
        cardNode.put("health", card.getHealth());
        cardNode.put("description", card.getDescription());

        // Add colors array
        ArrayNode colorsArray = mapper.createArrayNode();
        for (String color : card.getColors()) {
            colorsArray.add(color);
        }
        cardNode.set("colors", colorsArray);

        cardNode.put("name", card.getName());

        return cardNode;
    }


    private static ObjectNode createHeroNode(Hero hero, ObjectMapper mapper) {
        ObjectNode heroNode = mapper.createObjectNode();
        heroNode.put("mana", hero.getMana());
        heroNode.put("description", hero.getDescription());

        // Add colors array
        ArrayNode colorsArray = mapper.createArrayNode();
        for (String color : hero.getColors()) {
            colorsArray.add(color);
        }
        heroNode.set("colors", colorsArray);

        heroNode.put("name", hero.getName());
        heroNode.put("health", hero.getHealth());

        return heroNode;
    }
}
