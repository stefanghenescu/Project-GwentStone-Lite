package org.poo.main;

import java.util.ArrayList;

public class Table {
    static final int NR_ROWS = 4;
    static final int MAX_ROW_SIZE = 5;
    static final int FRONT_ROW_PLAYER_ONE = 2;
    static final int BACK_ROW_PLAYER_ONE = 3;
    static final int FRONT_ROW_PLAYER_TWO = 1;
    static final int BACK_ROW_PLAYER_TWO = 0;

    private final ArrayList<ArrayList<GameCard>> rows;

    /**
     * Constructor for the Table class
     * Initializes the rows of the table
     */
    public Table() {
        rows = new ArrayList<>(NR_ROWS);
        for (int i = 0; i < NR_ROWS; i++) {
            rows.add(new ArrayList<>());
        }
    }

    /**
     * Method to get a specific row of the table
     * @param rowIndex the index of the row
     * @return the row at the given index
     */
    public ArrayList<GameCard> getRow(final int rowIndex) {
        return rows.get(rowIndex);
    }

    /**
     * Method to get the rows of the table for a player
     * @param playerIdx the index of the player
     * @return an array on arrays of the rows of the table for the player
     */
    private ArrayList<ArrayList<GameCard>> getRowsForPlayer(final int playerIdx) {
        ArrayList<ArrayList<GameCard>> playerRows = new ArrayList<>();
        if (playerIdx == 1) {
            playerRows.add(rows.get(FRONT_ROW_PLAYER_ONE));
            playerRows.add(rows.get(BACK_ROW_PLAYER_ONE));
        } else if (playerIdx == 2) {
            playerRows.add(rows.get(FRONT_ROW_PLAYER_TWO));
            playerRows.add(rows.get(BACK_ROW_PLAYER_TWO));
        }
        return playerRows;
    }

    /**
     * Method to determine the row index where the card should be placed
     * @param playerIdx the index of the player
     * @param gameCard the card to be placed
     * @return the index of the row where the card should be placed
     */
    private int determineRowIndex(final int playerIdx, final GameCard gameCard) {
        if (gameCard.getName().equals("Sentinel") || gameCard.getName().equals("Berserker")
                || gameCard.getName().equals("The Cursed One")
                || gameCard.getName().equals("Disciple")) {
            // if playerIdx is 1 return 3 else return 0
            return (playerIdx == 1) ? BACK_ROW_PLAYER_ONE : BACK_ROW_PLAYER_TWO;
        } else {
            // If playerIdx is 1 return 2 else return 1
            return (playerIdx == 1) ? FRONT_ROW_PLAYER_ONE : FRONT_ROW_PLAYER_TWO;
        }
    }

    /**
     * Method to add a card to the table
     * @param player the player that is adding the card
     * @param gameCard the card to be added
     * @return an error message if there is an error, otherwise null
     */
    public String addCardToTable(final Player player, final GameCard gameCard) {
        int playerIdx = player.getIndex();
        if (player.getPlayerMana() < gameCard.getMana()) {
            return "Not enough mana to place card on table.";
        }

        // Depending on the card type and the player, determine the row index
        int rowIndex = determineRowIndex(playerIdx, gameCard);
        ArrayList<GameCard> row = getRow(rowIndex);

        if (row.size() < MAX_ROW_SIZE) {
            row.add(gameCard);
        } else {
            return "Cannot place card on table since row is full.";
        }

        return null;
    }

    /**
     * Method to defrost the cards on the table
     * @param playerIdx the index of the player of which the cards are to be defrosted
     */
    public void defrostCards(final int playerIdx) {
        for (ArrayList<GameCard> row : getRowsForPlayer(playerIdx)) {
            for (GameCard card : row) {
                card.setFrozen(false);
            }
        }
    }

    /**
     * Method to reset the flag of the cards that attacked this turn
     */
    public void rechargeCardsAttack() {
        for (ArrayList<GameCard> row : rows) {
            for (GameCard card : row) {
                card.setAttackedThisTurn(false);
            }
        }
    }

    /**
     * Method to remove a card from the table
     * @param rowIndex the index of the row from which the card is to be removed
     * @param cardIndex the index of the card to be removed
     */
    public void removeCardFromTable(final int rowIndex, final int cardIndex) {
        ArrayList<GameCard> row = getRow(rowIndex);
        row.remove(cardIndex);
    }
}
