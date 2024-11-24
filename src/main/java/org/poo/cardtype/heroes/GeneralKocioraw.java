package org.poo.cardtype.heroes;

import org.poo.cardtype.Hero;
import org.poo.fileio.CardInput;
import org.poo.gamecomponents.GameCard;
import org.poo.gamecomponents.Table;

/**
 * This class represents a GeneralKocioraw card game
 * This class is immutable and extends the {@link Hero} class
 */
public class GeneralKocioraw extends Hero {
    /**
     * As GeneralKocioraw extends Hero class we use super to call the Hero constructor
     * @param hero the card input
     */
    public GeneralKocioraw(final CardInput hero) {
        super(hero);
    }

    /**
     * GeneralKocioraw's ability is to increase the attack damage to all cards on a specific row
     * @param affectedRow the row that will be affected by the ability
     * @param table the table where the game is being played
     */
    @Override
    public void useAbility(final int affectedRow, final Table table) {
        for (GameCard card : table.getRow(affectedRow)) {
            card.setAttackDamage(card.getAttackDamage() + 1);
        }
    }
}
