package org.poo.cardType;

import org.poo.fileio.CardInput;
import org.poo.main.GameCard;
import org.poo.main.Hero;
import org.poo.main.Table;

public class GeneralKocioraw extends Hero {
    public GeneralKocioraw(CardInput hero) {
        super(hero);
    }

    @Override
    public void useAbility(int affectedRow, Table table) {
        for (GameCard card : table.getRow(affectedRow)) {
            card.setAttackDamage(card.getAttackDamage() + 1);
        }
    }
}
