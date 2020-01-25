int grid_size = 15;
int currentLine = 0;
int currentSide = 0;
int grid[][] = new int[grid_size][grid_size];
int[][][] clues = new int[2][grid_size][];
int boxsize = 50;
int neededSteps = 0;
int thick;


void setup()
{
    size(1000, 1000);
    initGrid();
    fill(0);
    textAlign(CENTER, CENTER);
    
    if(grid_size == 15)
    {
        thick = 5;
    }
    else 
    {
        thick = 4;
    }
    
    /*
    clues[0][0] = new IntList(1, 3);
    clues[0][1] = new IntList(1, 1);
    clues[0][2] = new IntList(1, 1);
    clues[0][3] = new IntList(2, 2);
    clues[0][4] = new IntList(1, 1, 3);
    clues[0][5] = new IntList(1, 2, 5);
    clues[0][6] = new IntList(1, 9);
    clues[0][7] = new IntList(13);
    clues[0][8] = new IntList(10, 1);
    clues[0][9] = new IntList(10);
    clues[0][10] = new IntList(2, 3, 5);
    clues[0][11] = new IntList(1, 3, 3, 1);
    clues[0][12] = new IntList(4, 6, 1);
    clues[0][13] = new IntList(8, 1, 1);
    clues[0][14] = new IntList(1, 1, 3, 1);
    
    clues[1][0] = new IntList(1);
    clues[1][1] = new IntList(4);
    clues[1][2] = new IntList(5, 2, 3);
    clues[1][3] = new IntList(3, 3, 3);
    clues[1][4] = new IntList(1, 8);
    clues[1][5] = new IntList(7, 2);
    clues[1][6] = new IntList(8, 3);
    clues[1][7] = new IntList(2, 4, 3);
    clues[1][8] = new IntList(2, 9);
    clues[1][9] = new IntList(1, 8);
    clues[1][10] = new IntList(1, 9);
    clues[1][11] = new IntList(7, 3);
    clues[1][12] = new IntList(5, 2);
    clues[1][13] = new IntList(1, 2);
    clues[1][14] = new IntList(2);
    */
    
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

ArrayList<int[]> getPossMoves(int[] clues)
{
    ArrayList<int[]> possMoves = new ArrayList<int[]>();
    int counter = 0;
    
    int sum = 0;
    for(int i = 0; i < clues.length; i++)
    {
        sum += clues[i];
    }
    sum += clues.length - 1;
    if(sum == grid_size)
    {
        // If there is only one way to place it (eg. 4 5 4 with gridsize 15 or 8 with gridsize 8)
        possMoves.add(new int[grid_size]);
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
        int[] spaces = new int[clues.length]; //How many spaces infront of every clue
    
        spaces[0] = 0;
        for(int i = 1; i < spaces.length; i++)
        {
            spaces[i] = 1;
        }
        
        rec(possMoves, clues, spaces, 0);
    }
    return possMoves;
}

void rec(ArrayList<int[]> moves, int[] ones, int[] zeros, int counter)
{
    while(neededSpaces(ones, zeros) <= grid_size)
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

void addToMoves(ArrayList<int[]> moves, int[] ones, int[] zeros)
{
    int counter = 0;
    moves.add(new int[grid_size]);
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

int neededSpaces(int[] ones, int[] zeros)
{
    int sum = 0;
    for(int i = 0; i < ones.length; i++) 
    {
        sum += ones[i];
        sum += zeros[i];
    }
    return sum;
}

ArrayList<int[]> filterMoves(ArrayList<int[]> possMoves, int[] line)
{
    ArrayList<int[]> toRemove = new ArrayList<int[]>();
    for(int[] ia : possMoves)
    {
        for(int i = 0; i < grid_size; i++)
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

int[] getSafePositions(ArrayList<int[]> possMoves)
{
    println("DEBUG: possMoves.size() = " + possMoves.size());
    int[] safePos = new int[grid_size];
    boolean safe;
    
    for(int i = 0; i < grid_size; i++)
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
        if(safe == true)
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

int[] solveLine(int[] oldLine, int[] clues)
{    
    println("Original line:");
    for(int i = 0; i < oldLine.length; i++)
    {
        print(oldLine[i] + ", ");
    }
    println();
    
    println("Clues:");
    for(int i = 0; i < clues.length; i++)
    {
        print(clues[i] + ", ");
    }
    println();
    
    ArrayList<int[]> moves = getPossMoves(clues);
    
    println("Found possible moves:");
    for(int[] ia : moves)
    {
        for(int i : ia)
        {
            print(i + ", ");
        }
        println();
    }
    
    moves = filterMoves(moves, oldLine);
        
    println("Moves left after filtering:");
    for(int[] ia : moves)
    {
        for(int i : ia)
        {
            print(i + ", ");
        }
        println();
    }
    
    int[] newLine = getSafePositions(moves);
    
    println("Safe positions:");
    for(int i = 0; i < newLine.length; i++)
    {
        print(newLine[i] + ", ");
    }
    println();
    
    return newLine;
}

void draw()
{
    background(255);
    fill(0);
    textSize(30);
    
    for(int i = 0; i < grid_size; i++)
    {
        for(int j = 0; j < grid_size; j++)
        {
            //text(grid[i][j], i * boxsize + 100 + boxsize/2, j * boxsize + 100 + boxsize/2);
            if(grid[i][j] == 1)
            {
                fill(0);
            }
            else if(grid[i][j] == -1)
            {
                fill(255, 0, 0);
            }
            else
            {
                fill(255);
            }
            
            rect(100 + boxsize * i, 100 + boxsize * j, boxsize, boxsize);
        }
    }
    
    for(int i = 0; i <= grid_size; i++)
    {
        if(i % thick == 0)
        {
            strokeWeight(3);
        }
        line(100 + boxsize * i, 100, 100 + boxsize * i, 100 + boxsize * grid_size);
        line(100, 100 + boxsize * i,100 + boxsize * grid_size, 100 + boxsize * i);
        if(i % thick == 0)
        {
            strokeWeight(1);
        }
    }
}

void initGrid()
{
    for(int i = 0; i < grid_size; i++) 
    {
        for(int j = 0; j < grid_size; j++) 
        {
            grid[i][j] = 0;
        }
    }
}

void keyPressed()
{
    boolean finished = true;
    
    for(int i = 0; i < grid_size; i++)
    {
        for(int j = 0; j < grid_size; j++) 
        {
            if(grid[i][j] == 0)
            {
                finished = false;
                break;
            }
        }
    }
    
    
    if(finished == true)
    {
        println("Finished with " + neededSteps + " steps!");
    }
    else
    {
        println();
        println("CurrentLine = " + currentLine);
        
        int[] currentLineItems = new int[grid_size];
        int[] newLineItems = new int[grid_size];
        
        finished = true; // reset finished to use it as finishedLine
        
        //Copy current Line into currentLineItems and check whether the line is already done
        if(currentSide == 0)
        {
            println("DEBUG: currentSide = 0");
            for(int i = 0; i < grid_size; i++)
            {
                currentLineItems[i] = grid[i][currentLine];
                
                if(grid[i][currentLine] == 0)
                {
                    finished = false;
                }
            }
        }
        else
        {
            println("DEBUG: currentSide = 1");
            for(int i = 0; i < grid_size; i++)
            {
                currentLineItems[i] = grid[currentLine][i];
                
                if(grid[currentLine][i] == 0)
                {
                    finished = false;
                }
            }
        }
        
        if(finished == false) // only execute if unfinished
        {
            newLineItems = solveLine(currentLineItems, clues[currentSide][currentLine]);
        
            if(currentSide == 0)
            {
                for(int i = 0; i < grid_size; i++)
                {
                    grid[i][currentLine] = newLineItems[i];
                }
            }
            else
            {
                for(int i = 0; i < grid_size; i++)
                {
                    grid[currentLine][i] = newLineItems[i];
                }
            }
        }
        
        //Execute always
        if(currentLine == grid_size - 1)
        {
            currentSide = (currentSide == 1 ? 0 : 1);
            currentLine = 0;
        }
        else
        {
            currentLine++;
        }
        
        if(finished == true) // reenter function and leave without increasing neededSteps because it will be incremented afterwards
        {
            keyPressed();
            return;
        }
        neededSteps++;
    }
}

/*

newLineItems = solveLine(currentLine, vClues.get(currentPosV));

for(int i = 0; i < grid_size; i++)
{
    grid[currentPosV][i] = newLineItems[i];
}

if(currentPosV == grid_size - 1)
{
    currentlySolvingH = true;
    currentPosV = 0;
}
else
{
    currentPosV++;
}

*/
