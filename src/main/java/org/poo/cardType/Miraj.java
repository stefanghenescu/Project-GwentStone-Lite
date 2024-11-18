package org.poo.cardType;
import org.poo.fileio.CardInput;
import org.poo.main.GameCard;

public class Miraj extends Minion{
    public Miraj(CardInput cardInput) {
        super(cardInput);
    }

    @Override
    public boolean isTank() {
        return false;
    }

    public void useAbility(GameCard attackedCard) {
        int healthAttacked = attackedCard.getHealth();
        int healthAttacker = getHealth();

        setHealth(healthAttacked);
        attackedCard.setHealth(healthAttacker);
    }
}
