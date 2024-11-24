package org.poo.cardtype.tanks;
import org.poo.cardtype.Minion;
import org.poo.fileio.CardInput;

/**
 * This class represents a Goliath card game
 * This class is immutable and extends the {@link Minion} class
 */
public class Goliath extends Minion {
    /**
     * As Goliath extends Minion class, we use super to call the Minion constructor
     * @param cardInput the card input
     */
    public Goliath(final CardInput cardInput) {
        super(cardInput);
    }

    /**
     * Verify if the card is a tank
     * @return true as Goliath is a tank
     */
    @Override
    public boolean isTank() {
        return true;
    }

}
