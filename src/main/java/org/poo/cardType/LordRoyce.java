package org.poo.cardType;

import org.poo.fileio.CardInput;
import org.poo.main.GameCard;
import org.poo.main.Hero;
import org.poo.main.Table;

public class LordRoyce extends Hero {

    public LordRoyce(CardInput hero) {
        super(hero);
    }

    @Override
    public void useAbility(int affectedRow, Table table) {
        for (GameCard card : table.getRow(affectedRow)) {
            card.setFrozen(true);
        }
    }
}
