import java.util.Scanner;

public class TicTacToe {

    private static char[][] board = {
            {' ', ' ', ' '},
            {' ', ' ', ' '},
            {' ', ' ', ' '}
    };

    private static char currentPlayer = 'X';

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printBoard();

            if (currentPlayer == 'X') {
                playerMove(scanner);
            } else {
                computerMove();
            }

            if (checkWinner()) {
                printBoard();
                System.out.println(currentPlayer + " wins!");
                break;
            } else if (isBoardFull()) {
                printBoard();
                System.out.println("It's a tie!");
                break;
            }

            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
        }

        scanner.close();
    }

    private static void printBoard() {
        for (char[] row : board) {
            for (char cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void playerMove(Scanner scanner) {
        int row, col;

        do {
            System.out.print("Enter the row (0, 1, or 2): ");
            row = scanner.nextInt();
            System.out.print("Enter the column (0, 1, or 2): ");
            col = scanner.nextInt();

            if (isValidMove(row, col)) {
                break;
            } else {
                System.out.println("Cell already occupied or invalid input. Try again.");
            }
        } while (true);

        board[row][col] = 'X';
    }

    private static void computerMove() {
        int[] bestMove = getBestMove();
        board[bestMove[0]][bestMove[1]] = 'O';
    }

    private static int[] getBestMove() {
        int[] bestMove = new int[]{-1, -1};
        int bestEval = Integer.MIN_VALUE;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    board[i][j] = 'O';
                    int eval = minimax(board, 0, false);
                    board[i][j] = ' ';

                    if (eval > bestEval) {
                        bestEval = eval;
                        bestMove[0] = i;
                        bestMove[1] = j;
                    }
                }
            }
        }

        return bestMove;
    }

    private static int minimax(char[][] board, int depth, boolean maximizingPlayer) {
        if (checkWinner()) {
            return (currentPlayer == 'X') ? -1 : 1;
        }
        if (isBoardFull()) {
            return 0;
        }

        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == ' ') {
                        board[i][j] = 'O';
                        int eval = minimax(board, depth + 1, false);
                        board[i][j] = ' ';
                        maxEval = Math.max(maxEval, eval);
                    }
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == ' ') {
                        board[i][j] = 'X';
                        int eval = minimax(board, depth + 1, true);
                        board[i][j] = ' ';
                        minEval = Math.min(minEval, eval);
                    }
                }
            }
            return minEval;
        }
    }

    private static boolean isValidMove(int row, int col) {
        return row >= 0 && row < 3 && col >= 0 && col < 3 && board[row][col] == ' ';
    }

    private static boolean checkWinner() {
        // Check rows, columns, and diagonals for a win
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == currentPlayer && board[i][1] == currentPlayer && board[i][2] == currentPlayer) {
                return true;
            }
            if (board[0][i] == currentPlayer && board[1][i] == currentPlayer && board[2][i] == currentPlayer) {
                return true;
            }
        }
        if (board[0][0] == currentPlayer && board[1][1] == currentPlayer && board[2][2] == currentPlayer) {
            return true;
        }
        return board[0][2] == currentPlayer && board[1][1] == currentPlayer && board[2][0] == currentPlayer;
    }

    private static boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }
}
