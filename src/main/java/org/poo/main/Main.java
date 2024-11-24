package org.poo.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;

import org.poo.checker.Checker;
import org.poo.checker.CheckerConstants;
import org.poo.fileio.ActionsInput;
import org.poo.fileio.CardInput;
import org.poo.fileio.GameInput;
import org.poo.fileio.Input;
import org.poo.gamecomponents.Game;
import org.poo.gamecomponents.GamesStats;
import org.poo.gamecomponents.Player;
import org.poo.gamecomponents.Table;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * DO NOT MODIFY MAIN METHOD
     * Call the checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(CheckerConstants.TESTS_PATH);
        Path path = Paths.get(CheckerConstants.RESULT_PATH);

        if (Files.exists(path)) {
            File resultFile = new File(String.valueOf(path));
            for (File file : Objects.requireNonNull(resultFile.listFiles())) {
                file.delete();
            }
            resultFile.delete();
        }
        Files.createDirectories(path);

        for (File file : Objects.requireNonNull(directory.listFiles())) {
            String filepath = CheckerConstants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getName(), filepath);
            }
        }

        Checker.calculateScore();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Input inputData = objectMapper.readValue(new File(CheckerConstants.TESTS_PATH + filePath1),
                Input.class);

        ArrayNode output = objectMapper.createArrayNode();
        GamesStats gamesStats = new GamesStats();

        for (GameInput gameInput : inputData.getGames()) {
            int playerOneDeckIdx = gameInput.getStartGame().getPlayerOneDeckIdx();
            ArrayList<CardInput> deckPlayerOne = inputData.getPlayerOneDecks().getDecks().
                                                            get(playerOneDeckIdx);

            CardInput heroPlayerOne = gameInput.getStartGame().getPlayerOneHero();
            int shuffleSeed = gameInput.getStartGame().getShuffleSeed();

            int playerTwoDeckIdx = gameInput.getStartGame().getPlayerTwoDeckIdx();
            ArrayList<CardInput> deckPlayerTwo = inputData.getPlayerTwoDecks().getDecks().
                                                            get(playerTwoDeckIdx);

            CardInput heroPlayerTwo = gameInput.getStartGame().getPlayerTwoHero();

            Player playerOne = new Player(1, shuffleSeed, deckPlayerOne, heroPlayerOne);
            Player playerTwo = new Player(2, shuffleSeed, deckPlayerTwo, heroPlayerTwo);

            playerOne.addCardToHand();
            playerTwo.addCardToHand();

            Table table = new Table();
            Game game = new Game(playerOne, playerTwo);

            for (ActionsInput action : gameInput.getActions()) {
                game.actionOutput(objectMapper, output, table, gameInput, action, gamesStats);
            }
        }

        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePath2), output);
    }
}
