package org.poo.cardType;
import org.poo.fileio.CardInput;

public class Goliath extends Minion {
    public Goliath(CardInput cardInput) {
        super(cardInput);
    }

    @Override
    public boolean isTank() {
        return true;
    }

}
