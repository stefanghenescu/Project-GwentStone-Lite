package org.poo.cardType;

import org.poo.fileio.CardInput;
import org.poo.main.GameCard;
import org.poo.main.Table;

/**
 * This class represents a EmpressThorina card game
 * This class is immutable and extends the {@link Hero} class
 */
public class EmpressThorina extends Hero {
    /**
     * As EmpressThorina extends Hero class we use super to call the Hero constructor
     * @param hero the card input
     */
    public EmpressThorina(final CardInput hero) {
        super(hero);
    }

    /**
     * EmpressThorina's ability is to destroy the card with the highest health on a specific row
     * on the table
     * @param affectedRow the row that will be affected by the ability
     * @param table the table where the game is being played
     */
    @Override
    public void useAbility(final int affectedRow, final Table table) {
        GameCard destroyCard = table.getRow(affectedRow).getFirst();

        // Find the card with the highest health on the row
        for (GameCard card : table.getRow(affectedRow)) {
            if (card.getHealth() > destroyCard.getHealth()) {
                destroyCard = card;
            }
        }
        table.removeCardFromTable(affectedRow, table.getRow(affectedRow).indexOf(destroyCard));
    }
}
