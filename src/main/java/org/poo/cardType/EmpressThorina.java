package org.poo.cardType;

import org.poo.fileio.CardInput;
import org.poo.main.GameCard;
import org.poo.main.Hero;
import org.poo.main.Table;

public class EmpressThorina extends Hero {
    public EmpressThorina(CardInput hero) {
        super(hero);
    }

    @Override
    public void useAbility(int affectedRow, Table table) {
        GameCard destroyCard = table.getRow(affectedRow).getFirst();

        for (GameCard card : table.getRow(affectedRow)) {
            if (card.getHealth() > destroyCard.getHealth()) {
                destroyCard = card;
            }
        }

        table.removeCardFromTable(affectedRow, table.getRow(affectedRow).indexOf(destroyCard));
    }
}
