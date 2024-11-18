package org.poo.cardType;
import org.poo.fileio.CardInput;
import org.poo.main.GameCard;

public class Disciple extends Minion {
    public Disciple(CardInput cardInput) {
        super(cardInput);
    }

    @Override
    public boolean isTank() {
        return false;
    }

    public void useAbility(GameCard attackedCard) {
        attackedCard.setHealth(attackedCard.getHealth() + 2);
    }
}
