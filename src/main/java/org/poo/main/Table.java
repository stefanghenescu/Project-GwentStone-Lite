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

    private ArrayList<ArrayList<GameCard>> getRowsForPlayer(int playerIdx) {
        ArrayList<ArrayList<GameCard>> playerRows = new ArrayList<>();
        if (playerIdx == 1) {
            playerRows.add(rows.get(2));
            playerRows.add(rows.get(3));
        } else if (playerIdx == 2) {
            playerRows.add(rows.get(0));
            playerRows.add(rows.get(1));
        }
        return playerRows;
    }

    private int determineRowIndex(int playerIdx, GameCard gameCard) {
        if (gameCard.getName().equals("Sentinel") || gameCard.getName().equals("Berserker")
                || gameCard.getName().equals("The Cursed One") || gameCard.getName().equals("Disciple")) {
            // if playerIdx is 1 return 3 else return 0
            return (playerIdx == 1) ? 3 : 0;
        } else {
            // If playerIdx is 1 return 2 else return 1
            return (playerIdx == 1) ? 2 : 1;
        }
    }

    public String addCardToTable(Player player, GameCard gameCard) {
        int playerIdx = player.getIndex();
        if (player.getPlayerMana() < gameCard.getMana()) {
            return "Not enough mana to place card on table.";
        }

        int rowIndex = determineRowIndex(playerIdx, gameCard);
        ArrayList<GameCard> row = getRow(rowIndex);

        if (row.size() < 5) {
            row.add(gameCard);
        } else {
            return "Cannot place card on table since row is full.";
        }

        return null;
    }

    public void defrostCards(int playerIdx) {
        for (ArrayList<GameCard> row : getRowsForPlayer(playerIdx)) {
            for (GameCard card : row) {
                card.setFrozen(false);
            }
        }
    }

    public void rechargeCardsAttack() {
        for (ArrayList<GameCard> row : rows) {
            for (GameCard card : row) {
                card.setAttackedThisTurn(false);
            }
        }
    }

    public void removeCardFromTable(int rowIndex, int cardIndex) {
        ArrayList<GameCard> row = getRow(rowIndex);
        row.remove(cardIndex);
    }
}
