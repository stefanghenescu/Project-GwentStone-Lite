package org.poo.cardType;
import org.poo.fileio.CardInput;
import org.poo.main.GameCard;

/**
 * This class represents a TheCursedOne card game
 * This class is immutable and extends the {@link Minion} class
 */
public class TheCursedOne extends Minion {
    /**
     * As TheCursedOne extends Minion class, we use super to call the Minion constructor
     * @param cardInput the card input
     */
    public TheCursedOne(final CardInput cardInput) {
        super(cardInput);
    }

    /**
     * Verify if the card is a tank
     * @return false as TheCursedOne is not a tank
     */
    @Override
    public boolean isTank() {
        return false;
    }

    /**
     * TheCursedOne's ability is to swap the health and attack damage of the attacked card
     * @param attackedCard the card that will be attacked by TheCursedOne card
     */
    public void useAbility(final GameCard attackedCard) {
        int healthAttacked = attackedCard.getHealth();
        int attackDamageAttacked = attackedCard.getAttackDamage();

        attackedCard.setHealth(attackDamageAttacked);
        attackedCard.setAttackDamage(healthAttacked);
    }
}
