package sample;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;

class Solver {
    private static int gridSize;
    private static int currentLine = 0;
    private static int currentSide = 0;
    private static IntegerProperty[][] grid;
    private static int[][][] clues;
    private static int neededSteps = 0;

    static void initialize(int gridSize) {
        Solver.gridSize = gridSize;
        grid = new IntegerProperty[gridSize][gridSize];
        clues = new int[2][gridSize][];

        initGrid();

        clues[0][0] = new int[]{1, 3};
        clues[0][1] = new int[]{1, 1};
        clues[0][2] = new int[]{1, 1};
        clues[0][3] = new int[]{2, 2};
        clues[0][4] = new int[]{1, 1, 3};
        clues[0][5] = new int[]{1, 2, 5};
        clues[0][6] = new int[]{1, 9};
        clues[0][7] = new int[]{13};
        clues[0][8] = new int[]{10, 1};
        clues[0][9] = new int[]{10};
        clues[0][10] = new int[]{2, 3, 5};
        clues[0][11] = new int[]{1, 3, 3, 1};
        clues[0][12] = new int[]{4, 6, 1};
        clues[0][13] = new int[]{8, 1, 1};
        clues[0][14] = new int[]{1, 1, 3, 1};

        clues[1][0] = new int[]{1};
        clues[1][1] = new int[]{4};
        clues[1][2] = new int[]{5, 2, 3};
        clues[1][3] = new int[]{3, 3, 3};
        clues[1][4] = new int[]{1, 8};
        clues[1][5] = new int[]{7, 2};
        clues[1][6] = new int[]{8, 3};
        clues[1][7] = new int[]{2, 4, 3};
        clues[1][8] = new int[]{2, 9};
        clues[1][9] = new int[]{1, 8};
        clues[1][10] = new int[]{1, 9};
        clues[1][11] = new int[]{7, 3};
        clues[1][12] = new int[]{5, 2};
        clues[1][13] = new int[]{1, 2};
        clues[1][14] = new int[]{2};
    }

    static void nextStep() {
        boolean finished = true;

        for(int i = 0; i < gridSize; i++)
        {
            for(int j = 0; j < gridSize; j++)
            {
                if(grid[i][j].get() == 0)
                {
                    finished = false;
                    break;
                }
            }
        }

        if(finished)
        {
            System.out.println("Finished with " + neededSteps + " steps!");
        }
        else
        {
            int[] currentLineItems = new int[gridSize];
            int[] newLineItems;

            finished = true; //Reset finished to use it as finishedLine

            //Copy current Line into currentLineItems and check whether the line is already done
            if(currentSide == 0)
            {
                for(int i = 0; i < gridSize; i++)
                {
                    currentLineItems[i] = grid[i][currentLine].get();

                    if (grid[i][currentLine].get() == 0) {
                        finished = false;
                    }
                }
            }
            else
            {
                for(int i = 0; i < gridSize; i++)
                {
                    currentLineItems[i] = grid[currentLine][i].get();

                    if (grid[currentLine][i].get() == 0) {
                        finished = false;
                    }
                }
            }

            if(!finished) //Only execute if unfinished
            {
                newLineItems = solveLine(currentLineItems, clues[currentSide][currentLine]);

                if(currentSide == 0)
                {
                    for(int i = 0; i < gridSize; i++)
                    {
                        grid[i][currentLine].set(newLineItems[i]);
                    }
                }
                else
                {
                    for(int i = 0; i < gridSize; i++)
                    {
                        grid[currentLine][i].set(newLineItems[i]);
                    }
                }
            }

            //Execute always
            if(currentLine == gridSize - 1)
            {
                currentSide = (currentSide == 1 ? 0 : 1);
                currentLine = 0;
            }
            else
            {
                currentLine++;
            }

            if(finished) //Reenter function and leave without increasing neededSteps because it will be incremented afterwards
            {
                nextStep();
                return;
            }
            neededSteps++;
        }
    }

    static IntegerProperty[][] getGrid() {
        return grid;
    }

    private static ArrayList<int[]> getPossMoves(int[] clues)
    {
        ArrayList<int[]> possMoves = new ArrayList<>();
        int counter = 0;

        int sum = 0;
        for(int i = 0; i < clues.length; i++)
        {
            sum += clues[i];
        }
        sum += clues.length - 1;
        if(sum == gridSize)
        {
            //If there is only one way to place it (eg. 4 5 4 with gridSize 15 or 8 with gridSize 8)
            possMoves.add(new int[gridSize]);
            for(int i = 0; i < clues.length - 1; i++)
            {
                for(int j = 0; j < clues[i]; j++)
                {
                    possMoves.get(0)[counter] = 1;
                    counter++;
                }
                possMoves.get(0)[counter] = 0;
                counter++;
            }
            for(int i = 0; i < clues[clues.length - 1]; i++)
            {
                possMoves.get(0)[counter] = 1;
                counter++;
            }
        }
        else
        {
            int[] spaces = new int[clues.length]; //How many spaces in front of every clue

            spaces[0] = 0;
            for(int i = 1; i < spaces.length; i++)
            {
                spaces[i] = 1;
            }

            rec(possMoves, clues, spaces, 0);
        }
        return possMoves;
    }

    private static void rec(ArrayList<int[]> moves, int[] ones, int[] zeros, int counter)
    {
        while(neededSpaces(ones, zeros) <= gridSize)
        {
            if(counter < ones.length - 1)
            {
                rec(moves, ones, zeros, counter + 1);
                for(int i = counter + 1; i < zeros.length; i++)
                {
                    zeros[i] = 1;
                }
            }
            else
            {
                addToMoves(moves, ones, zeros);
            }
            zeros[counter]++;
        }
    }

    private static void addToMoves(ArrayList<int[]> moves, int[] ones, int[] zeros)
    {
        int counter = 0;
        moves.add(new int[gridSize]);
        for(int i = 0; i < ones.length; i++)
        {
            for(int j = 0; j < zeros[i]; j++)
            {
                moves.get(moves.size() - 1)[counter++] = 0;
            }
            for(int j = 0; j < ones[i]; j++)
            {
                moves.get(moves.size() - 1)[counter++] = 1;
            }
        }
    }

    private static int neededSpaces(int[] ones, int[] zeros)
    {
        int sum = 0;
        for(int i = 0; i < ones.length; i++)
        {
            sum += ones[i];
            sum += zeros[i];
        }
        return sum;
    }

    private static ArrayList<int[]> filterMoves(ArrayList<int[]> possMoves, int[] line)
    {
        ArrayList<int[]> toRemove = new ArrayList<>();
        for(int[] ia : possMoves)
        {
            for(int i = 0; i < gridSize; i++)
            {
                if(((ia[i] == 1) && (line[i] == -1)) || ((ia[i] == 0) && (line[i] == 1)))
                {
                    toRemove.add(ia);
                }
            }
        }

        possMoves.removeAll(toRemove);

        return possMoves;
    }

    private static int[] getSafePositions(ArrayList<int[]> possMoves)
    {
        int[] safePos = new int[gridSize];
        boolean safe;

        for(int i = 0; i < gridSize; i++)
        {
            safe = true;
            for(int j = 0; j < possMoves.size(); j++)
            {
                if(possMoves.get(j)[i] != possMoves.get(0)[i])
                {
                    safe = false;
                    break;
                }
            }
            if(safe)
            {
                if(possMoves.get(0)[i] == 1)
                {
                    safePos[i] = 1;
                }
                else
                {
                    safePos[i] = -1;
                }
            }
            else
            {
                safePos[i] = 0;
            }
        }

        return safePos;
    }

    private static int[] solveLine(int[] oldLine, int[] clues)
    {
        System.out.println("Original line:");
        for(int i = 0; i < oldLine.length; i++)
        {
            System.out.print(oldLine[i] + ", ");
        }
        System.out.println();

        System.out.println("Clues:");
        for(int i = 0; i < clues.length; i++)
        {
            System.out.print(clues[i] + ", ");
        }
        System.out.println();

        ArrayList<int[]> moves = getPossMoves(clues);

        System.out.println("Found possible moves:");
        for(int[] ia : moves)
        {
            for(int i : ia)
            {
                System.out.print(i + ", ");
            }
            System.out.println();
        }

        filterMoves(moves, oldLine);

        System.out.println("Moves left after filtering:");
        for(int[] ia : moves)
        {
            for(int i : ia)
            {
                System.out.print(i + ", ");
            }
            System.out.println();
        }

        int[] newLine = getSafePositions(moves);

        System.out.println("Safe positions:");
        for(int i = 0; i < newLine.length; i++)
        {
            System.out.print(newLine[i] + ", ");
        }
        System.out.println();

        return newLine;
    }

    private static void initGrid()
    {
        for(int i = 0; i < gridSize; i++)
        {
            for(int j = 0; j < gridSize; j++)
            {
                grid[i][j] = new SimpleIntegerProperty();
            }
        }
    }
}