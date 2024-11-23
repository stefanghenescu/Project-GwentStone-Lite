package org.poo.cardType;
import org.poo.fileio.CardInput;

/**
 * This class represents a Warden card game
 * This class is immutable and extends the {@link Minion} class
 */
public class Warden extends Minion {
    /**
     * As Warden extends Minion class, we use super to call the Minion constructor
     * @param cardInput the card input
     */
    public Warden(final CardInput cardInput) {
        super(cardInput);
    }

    /**
     * Verify if the card is a tank
     * @return true as Warden is a tank
     */
    @Override
    public boolean isTank() {
        return true;
    }

}
