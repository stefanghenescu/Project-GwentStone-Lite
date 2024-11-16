package org.poo.main;

import java.util.ArrayList;

public class Table {
   private final ArrayList<ArrayList<GameCard>> rows;

    public Table() {
        rows = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            rows.add(new ArrayList<>());
        }
    }

    public ArrayList<GameCard> getRow(int rowIndex) {
        return rows.get(rowIndex);
    }

    public String addCardToTable(Player player, GameCard gameCard) {
        int playerIdx = player.getIndex();
        if (player.getPlayerMana() < gameCard.getMana()) {
            return "Not enough mana to place card on table.";
        }
        int rowIndex;
        if (gameCard.getName().equals("Sentinel") || gameCard.getName().equals("Berserker")
            || gameCard.getName().equals("The Cursed One") || gameCard.getName().equals("Disciple")) {
            if (playerIdx == 1) {
                rowIndex = 3;
            } else {
                rowIndex = 0;
            }
        } else {
            if (playerIdx == 1) {
                rowIndex = 2;
            } else {
                rowIndex = 1;
            }
        }
        ArrayList<GameCard> row = getRow(rowIndex);
        if (row.size() < 5) {
            row.add(gameCard);
        } else {
            return "Cannot place card on table since row is full.";
        }
        return null;
    }

    public void defrostCards(int playerIdx) {
        if (playerIdx == 1) {
            for (GameCard gameCard : this.getRow(2)) {
                if (gameCard.isFrozen()) {
                    gameCard.setFrozen(false);
                }
            }
            for (GameCard gameCard : this.getRow(3)) {
                if (gameCard.isFrozen()) {
                    gameCard.setFrozen(false);
                }
            }
        } else if (playerIdx == 2) {
            for (GameCard card : this.getRow(0)) {
                if (card.isFrozen()) {
                    card.setFrozen(false);
                }
            }
            for (GameCard card : this.getRow(1)) {
                if (card.isFrozen()) {
                    card.setFrozen(false);
                }
            }
        }
    }

    public void rechargeCardsAttack() {
        for (GameCard card : this.getRow(2)) {
            if (card.hasAttackedThisTurn()) {
                card.setAttackedThisTurn(false);
            }
        }
        for (GameCard card : this.getRow(3)) {
            if (card.hasAttackedThisTurn()) {
                card.setAttackedThisTurn(false);
            }
        }
        for (GameCard card : this.getRow(0)) {
            if (card.hasAttackedThisTurn()) {
                card.setAttackedThisTurn(false);
            }
        }
        for (GameCard card : this.getRow(1)) {
            if (card.hasAttackedThisTurn()) {
                card.setAttackedThisTurn(false);
            }
        }
    }

    public void removeCardFromTable(int rowIndex, int cardIndex) {
        ArrayList<GameCard> row = getRow(rowIndex);
        row.remove(cardIndex);
    }
}
