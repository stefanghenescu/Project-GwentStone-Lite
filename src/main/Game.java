package main;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import fileio.CardInput;
import fileio.GameInput;

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

                if (playerOne.getPlayerMana() < 10) {
                    playerOne.setPlayerMana(playerOne.getPlayerMana() + (getTurns() / 2) + 1);
                }
                if (playerTwo.getPlayerMana() < 10) {
                    playerTwo.setPlayerMana(playerTwo.getPlayerMana() + (getTurns() / 2) + 1);
                }
            }
            /// trebuie sa mai verific daca exista carti frozen si sa le schimb flag-ul
        } else if (action.getCommand().equals("placeCard")) {
            String error;
            if (getPlayerTurn() == 1) {
                CardInput cardFromHand = playerOne.getHand().get(action.getHandIdx());
                error = table.addCardToRow(playerOne, cardFromHand);
                if (error == null) {
                    playerOne.setPlayerMana(playerOne.getPlayerMana() - cardFromHand.getMana());
                    playerOne.getHand().remove(action.getHandIdx());
                }
            } else {
                CardInput cardFromHand = playerTwo.getHand().get(action.getHandIdx());
                error = table.addCardToRow(playerTwo, cardFromHand);
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
