package org.poo.cardType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CardInput;
import org.poo.main.GameCard;

public class Minion extends GameCard {
    private boolean frozen;

    public Minion(CardInput cardInput) {
        super(cardInput);
        frozen = false;
    }

    @Override
    public boolean isFrozen() {
        return frozen;
    }

    @Override
    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    @Override
    public boolean isTank() {
        return getName().equals("Goliath") || getName().equals("Warden");
    }

    @Override
    public ObjectNode createCardNode(ObjectMapper objectMapper) {

        ObjectNode cardNode = objectMapper.createObjectNode();
        cardNode.put("mana", getMana());
        cardNode.put("attackDamage", getAttackDamage());
        cardNode.put("health", getHealth());
        cardNode.put("description", getDescription());

        ArrayNode colorsArray = cardNode.putArray("colors");
        for (String color : getColors()) {
            colorsArray.add(color);
        }
        cardNode.set("colors", colorsArray);

        cardNode.put("name", getName());

        return cardNode;
    }


}
