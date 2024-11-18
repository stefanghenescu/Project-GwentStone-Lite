package org.poo.cardType;
import org.poo.fileio.CardInput;

public class Disciple extends Minion {
    public Disciple(CardInput cardInput) {
        super(cardInput);
    }

    @Override
    public boolean isTank() {
        return false;
    }
}
