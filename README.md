# Tema 0 - GwentStone Lite
### Ghenescu Stefan 322CA


## Introduction:

This project aims to simulate a simplified version of the Gwent card game. I implemented
the game using the **OOP** paradigm in Java, without the graphics o the game, only the mechanics
behind. The game is played between two players, each having a deck of cards, a hero and a table
on which they can place their cards. The game ends when one hero dies.
In this project the players can play multiple games.

## Packaging:

1. ###  The _cardtype_ package
This package defines the various types of cards used in the game.
It is divided into three sub-packages:

- **`heroes`**:
  Contains type of heroes classes that define each one's ability. Classes include:
    - **`EmpressThorina`**
    - **`GeneralKocioraw`**
    - **`KingMudface`**
    - **`LordRoyce`**  
  

- **`specialminions`**:
  Represents minion cards with abilities. Classes include:
    - **`Disciple`**
    - **`Miraj`**
    - **`TheCursedOne`**
    - **`TheRipper`**

- **`tanks`**:
  Includes tanks classes. Classes include:
    - **`Goliath`**
    - **`Warden`**

We also have the following classes that every one that I listed before extends:
  - **`Hero`** (a base class for all other hero types).
  - **`Minion`** (a base class for all other minion types).

   
2. ### The _**gamecomponents**_ package:
This package defines the elements of the game. It contains:
- **`Player`**:
  Represents a player in the game. It handles player-specific attributes such as their deck, mana, hero, and cards in hand.
- **`GameCard`**:
  A base class for all card types in the game. This class is like a copy of CardInput class.
- **`Game`**:
  Manages the flow of a single match. It handles the actions in a game with a switch statement that calls the appropriate method for each command.  
  It also takes care of the turns of the players.
- **`GameStats`**:
  Tracks statistics across multiple games, such as the total number of games played and individual player victories.
- **`Table`**:
  Represents the table where players place cards. It provides methods to add, remove, defrost cards on the table.

3. ### The _actionhandle_ package:
This package manages game actions and error handling. It includes:
- **`Action`**: Responsible for handling specific actions performed by players during the game (e.g., card attacks, spells).
- **`ErrorHandle`**: Ensures rules and constraints are respected during gameplay. For instance, it validates actions such as placing cards on the table or attacking.
- **`JsonOutput`**: A utility class for formatting game commands and results into JSON format for input/output operations.

4. ### The _main_ package contains the following classes:
    The main entry point for the application. It includes:
- **`Main`**:
  The primary class for running the games. From here we start every game with it's setup and actions.
- **`Test`**:
  Class used for testing


## Code Flow:
Starting from the main package, in the main class we create an instace of the
GamesSetup class in which we create instances of the Game class for each of
the games received in the input.

In the Game class all actions are taken care of with the use of a public method
that handles the command name of all the actions within a game/series of games.
After that, depending on the command we will go through a private method
located in the Game class that later on calls methods located in other classes
such as: Cards (and by polymorphism the inheriting classes), Player, GameTable.

The output is generated with the help of the utility class JsonNode in the
Game class and merged in the GamesSetup class.

## Project Feedback

This was an interesting project that helped me learn a lot about the basics
of **OOP**. I really would recommend to anyone that wants to have a solid
knowledge about primary OOP concepts such as Polymorphism and Inheritance
take on the assignment.
#### Thank you for your attention!

![Homework done](https://media.tenor.com/9qooEZ2uscQAAAAM/sleepy-tom-and-jerry.gif)

