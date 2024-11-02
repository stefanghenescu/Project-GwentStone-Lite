package fileio;

import javax.smartcardio.Card;
import java.util.ArrayList;

public final class CardInput {
    private int mana;
    private int attackDamage;
    private int health;
    private String description;
    private ArrayList<String> colors;
    private String name;
    private int frozen;

    public CardInput() {
    }

    public int getMana() {
        return mana;
    }

    public void setMana(final int mana) {
        this.mana = mana;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(final int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(final int health) {
        this.health = health;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public ArrayList<String> getColors() {
        return colors;
    }

    public void setColors(final ArrayList<String> colors) {
        this.colors = colors;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getFrozen() {
        return frozen;
    }

    public void setFrozen(final int frozen) {
        this.frozen = frozen;
    }

    public boolean isFrozen() {
        return frozen == 1;
    }


    public static CardInput copyCard(CardInput card) {
        CardInput copiedCard = new CardInput();

        copiedCard.setMana(card.getMana());
        copiedCard.setAttackDamage(card.getAttackDamage());
        copiedCard.setHealth(card.getHealth());
        copiedCard.setDescription(card.getDescription());
        copiedCard.setColors(new ArrayList<>(card.getColors()));
        copiedCard.setName(card.getName());
        copiedCard.setFrozen(card.getFrozen());

        return copiedCard;
    }

    @Override
    public String toString() {
        return "CardInput{"
                +  "mana="
                + mana
                +  ", attackDamage="
                + attackDamage
                + ", health="
                + health
                +  ", description='"
                + description
                + '\''
                + ", colors="
                + colors
                + ", name='"
                +  ""
                + name
                + '\''
                + '}';
    }
}
