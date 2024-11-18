package org.poo.cardType;
import org.poo.fileio.CardInput;
import org.poo.main.GameCard;

public class TheRipper extends Minion {
    public TheRipper(CardInput cardInput) {
        super(cardInput);

    }

    @Override
    public boolean isTank() {
        return false;
    }
}
