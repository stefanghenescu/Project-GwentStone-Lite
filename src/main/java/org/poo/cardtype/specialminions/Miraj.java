package org.poo.cardtype.specialminions;
import org.poo.cardtype.Minion;
import org.poo.fileio.CardInput;
import org.poo.gamecomponents.GameCard;

/**
 * This class represents a Miraj card game
 * This class is immutable and extends the {@link Minion} class
 */
public class Miraj extends Minion {
    /**
     * As Miraj extends Minion class, we use super to call the Minion constructor
     * @param cardInput the card input
     */
    public Miraj(final CardInput cardInput) {
        super(cardInput);
    }

    /**
     * Verify if the card is a tank
     * @return false as Miraj is not a tank
     */
    @Override
    public boolean isTank() {
        return false;
    }

    /**
     * Miraj's ability is to swap the health of the attacker and the attacked card
     * @param attackedCard the card that will be attacked by the Miraj card
     */
    public void useAbility(final GameCard attackedCard) {
        int healthAttacked = attackedCard.getHealth();
        int healthAttacker = getHealth();

        setHealth(healthAttacked);
        attackedCard.setHealth(healthAttacker);
    }
}
