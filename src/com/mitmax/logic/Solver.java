package com.mitmax.logic;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;
import java.util.Arrays;

public class Solver {
    private static int gridSize;
    private static int currentLineIndex;
    private static int currentSide;
    private static IntegerProperty[][] grid;
    private static int[][][] clues;
    private static int neededSteps;
    private static boolean isInitialized = false;
    private static boolean isCleared = true;
    private static boolean isFinished;

    public static void initialize(int gridSize) {
        Solver.gridSize = gridSize;
        Solver.currentLineIndex = 0;
        Solver.currentSide = 0;
        Solver.neededSteps = 0;

        grid = new IntegerProperty[gridSize][gridSize];
        for(int i = 0; i < gridSize; i++) {
            for(int j = 0; j < gridSize; j++) {
                grid[i][j] = new SimpleIntegerProperty();
            }
        }

        isInitialized = true;
        isCleared = true;
        isFinished = false;
    }

    public static void reset() {
        if(!isInitialized) {
            throw new IllegalStateException("Solver has not been initialized");
        }

        Solver.currentLineIndex = 0;
        Solver.currentSide = 0;
        Solver.neededSteps = 0;
        for(int i = 0; i < gridSize; i++) {
            for(int j = 0; j < gridSize; j++) {
                grid[i][j].set(0);
            }
        }

        isCleared = true;
        isFinished = false;
    }

    public static void nextStep() {
        if(!isInitialized) {
            throw new IllegalStateException("Solver has not been initialized");
        }

        if(isCleared) {
            isCleared = false;
        }

        if(!isFinished) {
            boolean finished = true;

            for(int i = 0; i < gridSize; i++) {
                for(int j = 0; j < gridSize; j++) {
                    if(grid[i][j].get() == 0) {
                        finished = false;
                        break;
                    }
                }
            }

            if(finished) {
                System.out.println("Finished with " + neededSteps + " steps!");
                isFinished = true;
            }
            else {
                int[] currentLine = new int[gridSize];
                int[] newLineItems;

                finished = true; //Reset finished to use it as finishedLine

                //Copy current Line into currentLine and check whether the line is already done
                if(currentSide == 0) {
                    for(int i = 0; i < gridSize; i++) {
                        currentLine[i] = grid[i][currentLineIndex].get();

                        if(grid[i][currentLineIndex].get() == 0) {
                            finished = false;
                        }
                    }
                }
                else {
                    for(int i = 0; i < gridSize; i++) {
                        currentLine[i] = grid[currentLineIndex][i].get();

                        if(grid[currentLineIndex][i].get() == 0) {
                            finished = false;
                        }
                    }
                }

                if(!finished) //Only execute if unfinished
                {
                    newLineItems = solveLine(currentLine, clues[currentSide][currentLineIndex]);

                    if(currentSide == 0) {
                        for(int i = 0; i < gridSize; i++) {
                            grid[i][currentLineIndex].set(newLineItems[i]);
                        }
                    }
                    else {
                        for(int i = 0; i < gridSize; i++) {
                            grid[currentLineIndex][i].set(newLineItems[i]);
                        }
                    }
                }

                //Execute always
                if(currentLineIndex == gridSize - 1) {
                    currentSide = (currentSide + 1) % 2;
                    currentLineIndex = 0;
                }
                else {
                    currentLineIndex++;
                }

                if(finished) //Reenter function and leave without increasing neededSteps because it will be incremented afterwards
                {
                    nextStep();
                    return;
                }
                neededSteps++;
            }
        }
    }

    public static IntegerProperty[][] getGrid() {
        return grid;
    }

    public static boolean isFinished() {
        return isFinished;
    }

    public static void setClues(int[][][] clues) {
        if(!isCleared) {
            reset();
        }

        Solver.clues = clues;
    }

    private static ArrayList<int[]> getPossMoves(int[] clues) {
        ArrayList<int[]> possMoves = new ArrayList<>();
        int counter = 0;

        //If there is only one way to place it (eg. 4 5 4 with gridSize 15 or 8 with gridSize 8)
        if(Arrays.stream(clues).sum() + clues.length - 1 == gridSize) {
            int[] onlyMove = new int[gridSize];

            for(int i = 0; i < clues.length; i++) {
                for(int j = 0; j < clues[i]; j++) {
                    onlyMove[counter] = 1;
                    counter++;
                }

                if(i != clues.length - 1) {
                    onlyMove[counter] = 0;
                    counter++;
                }
            }

            possMoves.add(onlyMove);
        }
        else {
            int[] spaces = new int[clues.length]; //How many spaces in front of every clue

            for(int i = 1; i < spaces.length; i++) {
                spaces[i] = 1;
            }

            rec(possMoves, clues, spaces, 0);
        }
        return possMoves;
    }

    private static void rec(ArrayList<int[]> moves, int[] ones, int[] zeros, int counter) {
        while (Arrays.stream(ones).sum() + Arrays.stream(zeros).sum() <= gridSize) {
            if(counter < ones.length - 1) {
                rec(moves, ones, zeros, counter + 1);

                for(int i = counter + 1; i < zeros.length; i++) {
                    zeros[i] = 1;
                }
            }
            else {
                addToMoves(moves, ones, zeros);
            }
            zeros[counter]++;
        }
    }

    private static void addToMoves(ArrayList<int[]> moves, int[] ones, int[] zeros) {
        int counter = 0;
        int[] newMove = new int[gridSize];

        for(int i = 0; i < ones.length; i++) {
            for(int j = 0; j < zeros[i]; j++) {
                newMove[counter++] = 0;
            }

            for(int j = 0; j < ones[i]; j++) {
                newMove[counter++] = 1;
            }
        }

        moves.add(newMove);
    }

    private static void filterMoves(ArrayList<int[]> possMoves, int[] line) {
        ArrayList<int[]> toRemove = new ArrayList<>();
        for(int[] possMove : possMoves) {
            for(int i = 0; i < gridSize; i++) {
                if(((possMove[i] == 1) && (line[i] == -1)) || ((possMove[i] == 0) && (line[i] == 1))) {
                    toRemove.add(possMove);
                    break;
                }
            }
        }

        possMoves.removeAll(toRemove);
    }

    private static int[] getSafePositions(ArrayList<int[]> possMoves) {
        int[] safePos = new int[gridSize];
        boolean safe;
        int value;

        for(int i = 0; i < gridSize; i++) {
            safe = true;
            value = possMoves.get(0)[i];
            for(int[] possMove : possMoves) {
                if(possMove[i] != value) {
                    safe = false;
                    break;
                }
            }
            if(safe) {
                safePos[i] = (value == 1) ? 1 : -1;
            }
            else {
                safePos[i] = 0;
            }
        }

        return safePos;
    }

    private static int[] solveLine(int[] oldLine, int[] clues) {
        printArray("Original line:", oldLine);
        printArray("Clues:", clues);

        ArrayList<int[]> moves = getPossMoves(clues);

        printArrayList("Found possible moves:", moves);

        filterMoves(moves, oldLine);

        printArrayList("Moves left after filtering:", moves);

        int[] newLine = getSafePositions(moves);

        printArray("Safe positions:", newLine);

        return newLine;
    }

    private static void printArray(String title, int[] array) {
        System.out.println(title);
        for(int value : array) {
            System.out.print(value + ", ");
        }
        System.out.println();
    }

    private static void printArrayList(String title, ArrayList<int[]> arrayList) {
        System.out.println(title);
        for(int[] array : arrayList) {
            for(int value : array) {
                System.out.print(value + ", ");
            }
            System.out.println();
        }
        System.out.println();
    }
}