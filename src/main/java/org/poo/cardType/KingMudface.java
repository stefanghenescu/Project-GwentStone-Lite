package org.poo.cardType;

import org.poo.fileio.CardInput;
import org.poo.main.GameCard;
import org.poo.main.Hero;
import org.poo.main.Table;

public class KingMudface extends Hero {
    public KingMudface(CardInput hero) {
        super(hero);
    }

    @Override
    public void useAbility(int affectedRow, Table table) {
        for (GameCard card : table.getRow(affectedRow)) {
            card.setHealth(card.getHealth() + 1);
        }
    }
}
