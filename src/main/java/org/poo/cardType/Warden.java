package org.poo.cardType;
import org.poo.fileio.CardInput;

public class Warden extends Minion {
    public Warden(CardInput cardInput) {
        super(cardInput);
    }

    @Override
    public boolean isTank() {
        return true;
    }

}
