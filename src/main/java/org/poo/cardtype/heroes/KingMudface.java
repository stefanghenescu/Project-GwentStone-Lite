package org.poo.cardtype.heroes;

import org.poo.cardtype.Hero;
import org.poo.fileio.CardInput;
import org.poo.gamecomponents.GameCard;
import org.poo.gamecomponents.Table;

/**
 * This class represents a KingMudface card game
 * This class is immutable and extends the {@link Hero} class
 */
public class KingMudface extends Hero {
    /**
     * As KingMudface extends Hero class we use super to call the Hero constructor
     * @param hero the card input
     */
    public KingMudface(final CardInput hero) {
        super(hero);
    }

    /**
     * KingMudface's ability is to increase the health to all cards on a specific row
     * @param affectedRow the row that will be affected by the ability
     * @param table the table where the game is being played
     */
    @Override
    public void useAbility(final int affectedRow, final Table table) {
        for (GameCard card : table.getRow(affectedRow)) {
            card.setHealth(card.getHealth() + 1);
        }
    }
}
