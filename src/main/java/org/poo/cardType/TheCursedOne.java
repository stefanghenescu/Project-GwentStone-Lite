package org.poo.cardType;
import org.poo.fileio.CardInput;

public class TheCursedOne extends Minion {
    public TheCursedOne(CardInput cardInput) {
        super(cardInput);
    }

    @Override
    public boolean isTank() {
        return false;
    }
}
