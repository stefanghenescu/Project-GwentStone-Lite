# GwentStone Lite
### Ghenescu Stefan 322CA

## Introduction:

This project aims to simulate a simplified version of the Gwent card game.  
I implemented the game using the **OOP** paradigm in Java, without the graphics  
of the game, only the mechanics behind. The game is played between two players,  
each having a deck of cards, a hero and a table on which they can place their cards.  
The game ends when one hero dies. In this project, the players can play multiple games.

## Packaging:

1. ### The _cardtype_ package
This package defines the various types of cards used in the game.  
It is divided into three sub-packages:

- **`heroes`**:  
  Contains type of hero classes that define each one's ability. Classes included:
  - **`EmpressThorina`**
  - **`GeneralKocioraw`**
  - **`KingMudface`**
  - **`LordRoyce`**


- **`specialminions`**:  
  Represents minion cards with abilities. Classes included:
  - **`Disciple`**
  - **`Miraj`**
  - **`TheCursedOne`**
  - **`TheRipper`**


- **`tanks`**:  
  Includes minions that are tanks: Classes included:
  - **`Goliath`**
  - **`Warden`**

We also have the following classes that every one that I listed before extends:
- **`Hero`** (a base class for all the other hero types classes).
- **`Minion`** (a base class for all the other minion types classes).

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
- **`Action`**: Responsible for handling specific actions performed by players during the game (e.g., card attacks, abilities).


- **`ErrorHandle`**: Ensures rules are respected during the game. For instance, it validates actions such as placing cards on the table or attacking.


- **`JsonOutput`**: A utility class for formatting game commands and results into JSON format.

4. ### The _main_ package contains the following classes:
   The main entry point for the application. It includes:
- **`Main`**: The primary class for running the games. From here we start every game with its setup and actions.


- **`Test`**: Class used for testing

## Code Flow:
Starting from the main class we create the games. For every game we then  
create the players (decks) and the table. After every component is created we start the game.

In the Game class, all actions are managed by a method that processes the command  
names with a switch case. From here we call the static methods from the Action class, in  
order to perform every action. These methods can call, depending on the command, other  
methods from the Player class, Table class, Hero class, Minion class or classes that  
extend these ones.

The output is generated with the help of classes like `JsonNode` and `ErrorHandle`.

## Project Feedback

This project provided a great opportunity to deepen my understanding of OOP concepts  
like Polymorphism and Inheritance. I highly recommend it to anyone looking to build a strong  
foundation in object-oriented programming. It was both challenging and rewarding as in the  
end I managed to implement a game :)  
