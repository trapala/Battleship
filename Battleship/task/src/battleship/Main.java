package battleship;

import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        /*
         * Stage 5
         */
        String[] shipName = {"Aircraft Carrier", "Battleship", "Submarine", "Cruiser", "Destroyer"};
        int[] shipLength = {5, 4, 3, 3, 2};
        int gameFieldSize = 10;
        int xCounter = 0;
        int xCounter2 = 0;

        // PLAYER 1
        // making and printing board
        char[][] scheme = makeBoard(gameFieldSize, gameFieldSize);
        char[][] schemeInTheFog = makeBoard(gameFieldSize, gameFieldSize);
        System.out.println("Player 1, place your ships on the game field");
        printBoard(scheme);

        // asking for coordinates and positioning of warships

        for (int i = 0; i < shipLength.length; i++) {
            System.out.println("Enter the coordinates of the " + shipName[i] + " (" + shipLength[i] + " cells):");
            positioningBoard(scheme, shipName[i], shipLength[i]);
            printBoard(scheme);
        }
        System.out.println("Press Enter and pass the move to another player");
        pass();

        // PLAYER 2
        // making and printing board

        char[][] scheme2 = makeBoard(gameFieldSize, gameFieldSize);
        char[][] schemeInTheFog2 = makeBoard(gameFieldSize, gameFieldSize);
        System.out.println("Player 2, place your ships on the game field");
        printBoard(scheme2);

        // asking for coordinates and positioning of warships

        for (int i = 0; i < shipLength.length; i++) {
            System.out.println("Enter the coordinates of the " + shipName[i] + " (" + shipLength[i] + " cells):");
            positioningBoard(scheme2, shipName[i], shipLength[i]);
            printBoard(scheme2);
        }
        System.out.println("Press Enter and pass the move to another player");
        pass();

        // starting the game
        // PLAYER 1

        do {
            printBoard(schemeInTheFog2);
            System.out.println("---------------------");
            printBoard(scheme);
            System.out.println("Player 1, it's your turn:");
            switch (shot(scheme2, schemeInTheFog2)) {
                case 0 -> System.out.println("You missed!");
                case 1 -> {
                    if (xCounter + 1 == Arrays.stream(shipLength).sum()) {
                        System.out.println("You sank the last ship. You won. Congratulations!");
                    } else {
                        System.out.println("You sank a ship!");
                    }
                    xCounter++;
                }
                case 2 -> {
                    System.out.println("You hit a ship!");
                    xCounter++;
                }
                case 3 -> System.out.println("This field is already hit!");
            }
            pass();

            // PLAYER 2

            printBoard(schemeInTheFog);
            System.out.println("---------------------");
            printBoard(scheme2);
            System.out.println("Player 2, it's your turn:");
            switch (shot(scheme, schemeInTheFog)) {
                case 0 -> System.out.println("You missed!");
                case 1 -> {
                    if (xCounter2 + 1 == Arrays.stream(shipLength).sum()) {
                        System.out.println("You sank the last ship. You won. Congratulations!");
                    } else {
                        System.out.println("You sank a ship!");
                    }
                    xCounter2++;
                }
                case 2 -> {
                    System.out.println("You hit a ship!");
                    xCounter2++;
                }
                case 3 -> System.out.println("This field is already hit!");
            }
            pass();
        } while (xCounter != Arrays.stream(shipLength).sum() || xCounter2 != Arrays.stream(shipLength).sum());
    }

    public static char[][] makeBoard(int rows, int column) {

        char[][] warField = new char[rows + 1][column + 2];
        int rowsCounter = 65;
        warField[0][0] = ' ';

        for (int i = 0; i < rows; i++) {
            warField[i][0] = (char) rowsCounter;
            rowsCounter++;
            for (int j = 1; j < column + 1; j++) {
                warField[i][j] = '~';
            }
        }
        return warField;
    }

    public static void printBoard(char[][] warField) {

        System.out.print("\n  ");
        for (int i = 1; i < warField.length; i++) {
            if (i < 10) {
                System.out.print(i + " ");
            } else {
                System.out.print(i);
            }
        }
        for (char[] chars : warField) {
            System.out.println();
            for (int j = 0; j < warField[0].length; j++) {
                System.out.print(chars[j] + " ");
            }
        }
        System.out.println("\n");
    }

    public static void positioningBoard(char[][] board, String shipName, int shipLength) {

        String coordinates;
        coordinates = SCANNER.nextLine().toUpperCase();
        String[] parts = coordinates.split("\\s+");
        int realShipLength;
        boolean horizontalPosition = true;

        int int1 = Integer.parseInt(parts[0].replaceAll("\\D+", ""));
        int int2 = Integer.parseInt(parts[1].replaceAll("\\D+", ""));

        char char1 = (parts[0].replaceAll("[0-9]", "")).charAt(0);
        char char2 = (parts[1].replaceAll("[0-9]", "")).charAt(0);

        if (char1 == char2) {
            realShipLength = Math.abs(int1 - int2) + 1;
        } else if (int1 == int2) {
            realShipLength = Math.abs(char1 - char2) + 1;
            horizontalPosition = false;
        } else {
            System.out.println("Error! Wrong ship location! Try again:");
            positioningBoard(board, shipName, shipLength);
            return;
        }

        if (realShipLength != shipLength) {
            System.out.println(realShipLength + " " + shipLength);
            System.out.println("Error! Wrong length of the " + shipName + "! Try again:");
            positioningBoard(board, shipName, shipLength);
            return;
        }

        for (int i = 0; i < realShipLength; i++) {
            if (horizontalPosition) {
                if (checkClosedPosition(board, char1 - 65, Math.min(int1, int2) + i)) {
                    System.out.println("Error! You placed it too close to another one. Try again:");
                    positioningBoard(board, shipName, shipLength);
                    return;
                }
            } else {
                if (checkClosedPosition(board, Math.min(char1, char2) - 65 + i, int1)) {
                    System.out.println("Error! You placed it too close to another one. Try again:");
                    positioningBoard(board, shipName, shipLength);
                    return;
                }
            }
        }

        if (horizontalPosition) {
            for (int i = 0; i < realShipLength; i++) {
                board[char1 - 65][Math.min(int1, int2) + i] = 'O';
            }
        } else {
            for (int i = 0; i < realShipLength; i++) {
                board[Math.min(char1, char2) - 65 + i][int1] = 'O';
            }
        }
    }

    public static boolean checkClosedPosition(char[][] board, int int1, int int2) {
        boolean result = false;

        if (int1 == 0) {
            if (board[int1 + 1][int2] == 'O' ||
                    board[int1][int2 - 1] == 'O' ||
                    board[int1][int2 + 1] == 'O' ||
                    board[int1 + 1][int2 + 1] == 'O' ||
                    board[int1 + 1][int2 - 1] == 'O') {
                result = true;
            }
        } else {
            if (board[int1 - 1][int2] == 'O' ||
                    board[int1 + 1][int2] == 'O' ||
                    board[int1][int2 + 1] == 'O' ||
                    board[int1][int2 - 1] == 'O' ||
                    board[int1 - 1][int2 - 1] == 'O' ||
                    board[int1 + 1][int2 + 1] == 'O' ||
                    board[int1 + 1][int2 - 1] == 'O') {
                result = true;
            }
        }
        return result;
    }

    public static int shot(char[][] board, char[][] boardInTheFog) {
        boolean error;
        int int1 = 0;
        int int2 = 0;
        String coordinates;

        do {
            error = false;
            coordinates = SCANNER.nextLine().toUpperCase();

            try {
                int1 = coordinates.charAt(0) - 65;
                int2 = Integer.parseInt(coordinates.substring(1));
            } catch (Exception e) {
                System.out.println("Error! You entered the wrong coordinates! Try again:");
                error = true;
            }

            if (int1 > 10 || int1 < 0 || int2 > 10 || int2 < 0) {
                System.out.println("Error! You entered the wrong coordinates! Try again:");
                error = true;
            }
        } while (error);

        if (board[int1][int2] == 'O') {
            board[int1][int2] = 'X';
            boardInTheFog[int1][int2] = 'X';
            if (checkClosedPosition(board, int1, int2)) {
                return 2;
            } else {
                return 1;
            }
        } else if (board[int1][int2] == 'X') {
            return 3;
        } else {
            board[int1][int2] = 'M';
            boardInTheFog[int1][int2] = 'M';
            return 0;
        }
    }

    private static void pass() {
        System.out.println("Press Enter and pass the move to another player");
        SCANNER.nextLine();
    }
}