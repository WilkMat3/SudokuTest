package com.company;


import java.util.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Stack;

import static com.company.Menu.getRandomNum;

/**
 * Class represents sudoku game
 *
 */



public class Sudoku implements Serializable {
    private int[][] gameBoard = new int[9][9]; // where game is played
    private int[][] startingBoard = new int[9][9]; // tracks the starting board
    private Stack<Action> moves; // all moves made to date
    private Stack<Action> redoMoves; // used for redoing moves
    public Queue<Action> reverseMoves; // used for automatic replay
    private int[] counter = new int[9]; // tracks the number of number between 1-9 on the board
    // getters & setters
    public int[][] getGameBoard() {
        return gameBoard;
    }
    public void setGameBoard(int[][] gameBoard) {
        this.gameBoard = gameBoard;
    }

    public int[][] getStartingBoard() {
        return startingBoard;
    }

    public void setStartingBoard(int[][] startingBoard) {
        this.startingBoard = startingBoard;
    }

    public Stack getMoves() {
        return moves;
    }

    public void setMoves(Stack moves) {
        this.moves = moves;
    }

    public Stack getRedoMoves() {
        return redoMoves;
    }

    public void setRedoMoves(Stack redoMoves) {
        this.redoMoves = redoMoves;
    }

    public Queue<Action> getReverseMoves() {
        return reverseMoves;
    }

    public void setReverseMoves(Queue<Action> reverseMoves) {
        this.reverseMoves = reverseMoves;
    }

    // constructor
    public Sudoku(){
        moves = new Stack<Action>();
        redoMoves = new Stack<Action>();
        reverseMoves = new LinkedList<>();
        for( int row = 0 ;row < this.gameBoard.length; row++){
            for(int column = 0 ; column < this.gameBoard.length; column++){
                this.gameBoard[row][column] = 0;
            }

        }


    }
    // adds the number to the board and to correct stack/queue
    public void setNum( Action action){
        action.setPreviousValue(this.gameBoard[action.getRow()][action.getColumn()]); // keep track of previous value on the field
        this.gameBoard[action.getRow()][action.getColumn()] = action.getValue();
        moves.push(action);
        reverseMoves.add(action);
        redoMoves.removeAllElements(); // this is done to not break the sequence of moves
    }
    // undo method - adding to correct stack
    public void undo( ){
        if(!this.moves.isEmpty()){
            Action action = moves.pop();

            this.gameBoard[action.getRow()][action.getColumn()] = action.getPreviousValue();
            redoMoves.push(action);
        }


    }
    // redo method adding to correct stack
    public void redo( ){
        if(!this.redoMoves.isEmpty()) {
            Action action = redoMoves.pop();

            this.gameBoard[action.getRow()][action.getColumn()] = action.getValue();
            moves.push(action);
        }
    }

    // replaces x amount of fields with 0s to create the initial board
    public void prepareGameBoard(int fieldsToRemove ){
        int row ;
        int col ;
        for( int i = 0; i< fieldsToRemove; i++ ) {
            row = getRandomNum(8,0);
            col = getRandomNum(8,0);
            if( this.gameBoard[row][col] == 0){
                while(  this.gameBoard[row][col] == 0){
                    row = getRandomNum(8,0);
                    col = getRandomNum(8,0);
                }
                this.gameBoard[row][col] = 0;
            }else{
                this.gameBoard[row][col] = 0;
            }
            this.startingBoard = Arrays.stream(this.gameBoard).map(int[]::clone).toArray(int[][]::new);


        }
    }


    //  printing the board in desired format

    public void printBoard(){

        for( int row = 0 ;row < this.gameBoard.length; row++){
            if(row == 0 ){
                System.out.println("    1  2  3   4  5  6   7  8  9 ");
                System.out.println("  - - - - - - - - - - - - - - - - ");
            }else if( row %3 == 0 ){
                System.out.println("  - - - - - - - - - - - - - - - - ");
            }
            for(int column = 0 ; column < this.gameBoard.length; column++){

                if(column == 0 ){
                    System.out.print((row+1)+" |");
                }else if((column %3) == 0  ){
                    System.out.print("|");
                }
                System.out.print(" "+ this.gameBoard[row][column]+" ");

                if(column == 8 && row == 8){
                    System.out.print(" | ");
                    System.out.println();
                    System.out.println("  - - - - - - - - - - - - - - - - ");

                }else if ( column == 8 && row != 8){
                    System.out.print(" | ");
                    System.out.println();
                }

            }
        }
        trackNumbers(); // printing the tracked numbers
        for( int i = 0;i <9 ;i++){
            if(i == 0){
                System.out.print("Numbers : " + (i+1) +" |");
            }else{
                System.out.print(" "+(i+1)+" |");
            }
        }
        System.out.println();
        for( int i = 0;i <9 ;i++){
            if(i == 0){
                System.out.print("Counter : " + counter[i] +" |");
            }else{
                System.out.print(" "+counter[i]+" |");
            }

        }
        System.out.println();
        this.counter = new int[9]; // reassigning the counter so the method does not double the values
    }


    // solves the board needs to return true for the board to be valid
    public boolean solve(){
        List<Integer> numbers = createRandomArray();
        for(int row = 0; row <this.gameBoard.length;row++){ // loop through the board
            for(int col = 0; col <this.gameBoard[row].length;col++){
                if( this.gameBoard[row][col] == 0) { // check if the field should be filled
                    for(int counter = 0  ; counter < numbers.size(); counter ++ ){// try all numbers in the shuffled list
                        if(isActionValid(numbers.get(counter),row,col)){ // check if number can be placed
                            this.gameBoard[row][col] = numbers.get(counter); // place the board
                            if(solve()){ // recursive call for backtracking
                                return true;
                            }else{ // set the field back to 0 if the solution is not valid
                                this.gameBoard[row][col] = 0;
                            }
                        }
                    }
                    return false; // only if it is unable to place any number in the row
                }
            }
        }
        return true; // if it gets through the outer loop
    }
    // returns an unordered array list of numbers between 1 - 9
    public List<Integer> createRandomArray(){
        Integer [] ordered = new Integer[9];
        for(int i =0; i <ordered.length; i++){
            ordered[i]= i +1;
        }
// Shuffle the elements in the array
        List<Integer> shuffled = Arrays.asList(ordered);
        // System.out.println(shuffled);
        Collections.shuffle(shuffled);
        //  System.out.println(shuffled);
        return shuffled;
    }


    //helper methods  for validation in row, column and box.

    public boolean isValidRowCheck( int digit, int row){ // checks all numbers in the row
        boolean result = true;
        for ( int i = 0 ; i < this.getGameBoard().length; i++ ){
            if( this.gameBoard[row][i] == digit ){
                result = false;
            }
        }

        return result;
    }
    public boolean isValidColumnCheck(int digit, int column){
        boolean result = true;
        for( int i = 0 ; i < this.getGameBoard().length; i++ ){ // checks all numbers in the column
            if( this.gameBoard[i][column] == digit){
                result = false;
            }
        }
        return result;
    }
    public boolean isValidBoxCheck(int digit, int row, int column ){ // finds the topleft positon of the 3x3 box and loops through numbers there
        boolean result = true;
        int boxRow = row - (row %  3); //get the top left row of the
        int boxColumn = column - (column % 3);
        for(int i = boxRow; i< boxRow+3; i++){
            for( int j = boxColumn; j < boxColumn + 3; j++){
                if( this.gameBoard[i][j] == digit){
                    result = false;
                }
            }
        }
        return result;
    }

    public boolean isActionValid( int digit, int row, int column){ // use all helper methods to check validity
        boolean result = false;
        if (isValidRowCheck(digit, row) && isValidColumnCheck(digit, column) && isValidBoxCheck(digit,row, column) ){
            result = true;
        }
        return result;
    }
    public boolean finish(){
        boolean result = true;
        for(int row = 0 ; row < this.getGameBoard().length ;row++){
            for (int col = 0; col < this.getGameBoard().length ; col++){
                if(this.getGameBoard()[row][col] == 0){
                    result = false;
                }
            }
        }
        return result;
    }

    // used to track numbers on the board - the last line of print method would reset the counter array, so it's not doubling the values
    public void trackNumbers(){
        for( int row = 0 ; row < this.gameBoard.length; row ++){

            for(int col = 0; col < this.gameBoard.length; col++ ){
                if(this.gameBoard[row][col] != 0){
                    counter[this.gameBoard[row][col] -1 ] +=1;
                }

            }
        }
    }


}