package ai_assignment2;

import java.util.Arrays;
import java.util.PriorityQueue;

class PuzzleNode implements Comparable<PuzzleNode> {
    int[] state;
    PuzzleNode parent;

    PuzzleNode(int[] state, PuzzleNode parent) {
        this.state = state;
        this.parent = parent;
    }

    @Override
    public int compareTo(PuzzleNode other) {
        return Integer.compare(this.heuristic(), other.heuristic());
    }

    private int heuristic() {
        // Change the heuristic function as needed
        return h3();
    }

    private int h1() {
        // Misplaced tiles heuristic
        int[] goalState = {1, 2, 3, 4, 5, 6, 7, 8, 0};
        return (int) Arrays.stream(state).zipWithInt(goalState, (a, b) -> a == b ? 0 : 1).filter(x -> x == 1).count();
    }

    private int h2() {
        // Manhattan distance heuristic
        int totalDistance = 0;
        int[][] goalPositions = {{0, 0}, {0, 1}, {0, 2}, {1, 0}, {1, 1}, {1, 2}, {2, 0}, {2, 1}, {2, 2}};

        for (int i = 0; i < state.length; i++) {
            if (state[i] != 0) {
                int goalRow = goalPositions[state[i]][0];
                int goalCol = goalPositions[state[i]][1];
                int currentRow = i / 3;
                int currentCol = i % 3;
                totalDistance += Math.abs(goalRow - currentRow) + Math.abs(goalCol - currentCol);
            }
        }

        return totalDistance;
    }

    private int h3() {
        // Custom heuristic: Combination of misplaced tiles and Manhattan distance
        return h1() + h2();
    }
}

public class EightPuzzle {

    public static PuzzleNode greedyBestFirstSearch(int[] initialState) {
        PuzzleNode startNode = new PuzzleNode(initialState, null);
        PriorityQueue<PuzzleNode> priorityQueue = new PriorityQueue<>();
        priorityQueue.add(startNode);

        while (!priorityQueue.isEmpty()) {
            PuzzleNode currentNode = priorityQueue.poll();

            if (Arrays.equals(currentNode.state, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 0})) {
                // Goal state reached
                return currentNode;
            }

            // Generate next possible states
            int[][] neighbors = getNeighbors(currentNode);

            for (int[] neighborState : neighbors) {
                PuzzleNode neighborNode = new PuzzleNode(neighborState, currentNode);
                priorityQueue.add(neighborNode);
            }
        }

        return null;
    }

    private static int[][] getNeighbors(PuzzleNode node) {
        int blankIndex = indexOf(node.state, 0);
        int[][] neighbors = new int[4][9];

        int[] dx = {0, 0, -1, 1};
        int[] dy = {-1, 1, 0, 0};

        for (int i = 0; i < 4; i++) {
            int newRow = blankIndex / 3 + dx[i];
            int newCol = blankIndex % 3 + dy[i];

            if (newRow >= 0 && newRow < 3 && newCol >= 0 && newCol < 3) {
                int[] newState = Arrays.copyOf(node.state, 9);
                newState[blankIndex] = newState[newRow * 3 + newCol];
                newState[newRow * 3 + newCol] = 0;
                neighbors[i] = newState;
            }
        }

        return neighbors;
    }

    private static int indexOf(int[] array, int value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }

    private static void printSolution(PuzzleNode node) {
        if (node == null) {
            System.out.println("No solution found.");
            return;
        }

        while (node != null) {
            printState(node.state);
            System.out.println();
            node = node.parent;
        }
    }

    private static void printState(int[] state) {
        for (int i = 0; i < 9; i += 3) {
            System.out.println(Arrays.toString(Arrays.copyOfRange(state, i, i + 3)));
        }
    }

    public static void main(String[] args) {
        int[] initialState = {1, 2, 3, 4, 5, 6, 7, 8, 0};

        System.out.println("Greedy Best-First Search with h1 (Misplaced Tiles)");
        PuzzleNode resultH1 = greedyBestFirstSearch(initialState);
        printSolution(resultH1);

        System.out.println("Greedy Best-First Search with h2 (Manhattan Distance)");
        PuzzleNode resultH2 = greedyBestFirstSearch(initialState);
        printSolution(resultH2);

        System.out.println("Greedy Best-First Search with h3 (Custom Heuristic)");
        PuzzleNode resultH3 = greedyBestFirstSearch(initialState);
        printSolution(resultH3);
    }
}

