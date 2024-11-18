package org.poo.cardType;
import org.poo.fileio.CardInput;

public class Miraj extends Minion{
    public Miraj(CardInput cardInput) {
        super(cardInput);
    }

    @Override
    public boolean isTank() {
        return false;
    }
}
