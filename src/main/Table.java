package main;

import fileio.CardInput;

import java.util.ArrayList;

public class Table {
   private final ArrayList<ArrayList<CardInput>> rows;

    public Table() {
        rows = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            rows.add(new ArrayList<>());
        }
    }

    public ArrayList<CardInput> getRow(int rowIndex) {
        return rows.get(rowIndex);
    }

    public String addCardToRow(Player player, CardInput card) {
        int playerIdx = player.getIndex();
        if (player.getPlayerMana() < card.getMana()) {
            return "Not enough mana to place card on table.";
        }
        int rowIndex;
        if (card.getName().equals("Sentinel") || card.getName().equals("Berserker")
            || card.getName().equals("The Cursed One") || card.getName().equals("Disciple")) {
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
        ArrayList<CardInput> row = getRow(rowIndex);
        if (row.size() < 5) {
            row.add(card);
        } else {
            return "Cannot place card on table since row is full.";
        }
        return null;
    }

    public void removeCardFromRow(int rowIndex, int cardIndex) {
        ArrayList<CardInput> row = getRow(rowIndex);

        row.remove(cardIndex);
        shiftLeft(row);
    }

   private void shiftLeft(ArrayList<CardInput> row) {
        for (int i = 1; i < row.size(); i++) {
            row.set(i - 1, row.get(i));
        }
        if (!row.isEmpty()) {
            row.remove(row.size() - 1);
        }
    }
}
