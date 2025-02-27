package org.poo.cardtype.specialminions;
import org.poo.cardtype.Minion;
import org.poo.fileio.CardInput;
import org.poo.gamecomponents.GameCard;

/**
 * This class represents a Disciple card game
 * This class is immutable and extends the {@link Minion} class
 */
public final class Disciple extends Minion {
    /**
     *  As Disciple extends Minion class, and we use super to call the Minion constructor
     * @param cardInput the card input
     */
    public Disciple(final CardInput cardInput) {
        super(cardInput);
    }

    /**
     * Verify if the card is a tank
     * @return false as Disciple is not a tank
     */
    @Override
    public boolean isTank() {
        return false;
    }

    /**
     * Implement the ability of the Disciple card as this method is ovverriden in every type of card
     * @param attackedCard the card that will be attacked by the Disciple card
     */
    @Override
    public void useAbility(final GameCard attackedCard) {
        attackedCard.setHealth(attackedCard.getHealth() + 2);
    }
}
