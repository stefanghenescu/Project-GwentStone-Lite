package org.poo.cardType;
import org.poo.fileio.CardInput;
import org.poo.main.GameCard;

/**
 * This class represents a TheRipper card game
 * This class is immutable and extends the {@link Minion} class
 */
public class TheRipper extends Minion {
    /**
     * As TheRipper extends Minion class, we use super to call the Minion constructor
     * @param cardInput the card input
     */
    public TheRipper(final CardInput cardInput) {
        super(cardInput);
    }

    /**
     * Verify if the card is a tank
     * @return false as TheRipper is not a tank
     */
    @Override
    public boolean isTank() {
        return false;
    }

    /**
     * TheRipper's ability is to reduce the attack damage of the attacked card by 2
     * @param attackedCard the card that will be attacked by TheRipper card
     */
    public void useAbility(final GameCard attackedCard) {
        attackedCard.setAttackDamage(attackedCard.getAttackDamage() - 2);

        if (attackedCard.getAttackDamage() <= 0) {
            attackedCard.setAttackDamage(0);
        }
    }
}
