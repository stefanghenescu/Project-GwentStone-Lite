package org.poo.cardType;
import org.poo.fileio.CardInput;
import org.poo.main.GameCard;

public class TheCursedOne extends Minion {
    public TheCursedOne(CardInput cardInput) {
        super(cardInput);
    }

    @Override
    public boolean isTank() {
        return false;
    }

    public void useAbility(GameCard attackedCard) {
        int healthAttacked = attackedCard.getHealth();
        int attackDamageAttacked = attackedCard.getAttackDamage();

        attackedCard.setHealth(attackDamageAttacked);
        attackedCard.setAttackDamage(healthAttacked);
    }
}
