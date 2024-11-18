package org.poo.main;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.fileio.ActionsInput;

import java.util.ArrayList;

public class Action {
    public static void actionDeck(Player playerOne, Player playerTwo, ArrayNode output,
                                  ActionsInput action, JsonOutput jsonOutput) {
        ArrayList<GameCard> deck;
        if (action.getPlayerIdx() == 1) {
            deck = playerOne.getDeck();
        } else {
            deck = playerTwo.getDeck();
        }
        output.add(jsonOutput.generateOutput(action, deck));
    }

    public static void actionHero(Player playerOne, Player playerTwo, ArrayNode output,
                                  ActionsInput action, JsonOutput jsonOutput) {
        Hero hero;
        if (action.getPlayerIdx() == 1) {
            hero = playerOne.getHero();
        } else {
            hero = playerTwo.getHero();
        }
        output.add(jsonOutput.generateOutput(action, hero));
    }

    public static void actionHand(Player playerOne, Player playerTwo, ArrayNode output,
                                  ActionsInput action, JsonOutput jsonOutput) {
        ArrayList<GameCard> hand;
        if (action.getPlayerIdx() == 1) {
            hand = playerOne.getHand();
        } else {
            hand = playerTwo.getHand();
        }

        output.add(jsonOutput.generateOutput(action, hand));
    }


}
