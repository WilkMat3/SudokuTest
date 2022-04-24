package com.company;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import static com.company.Menu.getRandomNum;





public class Sudoku implements Serializable {
    private int[][] gameBoard = new int[9][9];
    private int[][] startingBoard = new int[9][9];
    private Stack<Action> moves;
    private Stack<Action> redoMoves;
    public Queue<Action> reverseMoves;
    private int[] counter = new int[9];
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
    public void setNum( Action action){
        action.setPreviousValue(this.gameBoard[action.getRow()][action.getColumn()]);
        this.gameBoard[action.getRow()][action.getColumn()] = action.getValue();
        moves.push(action);
        reverseMoves.add(action);
        redoMoves.removeAllElements();
    }

    public void undo( ){
        if(!this.moves.isEmpty()){
            Action action = moves.pop();

            this.gameBoard[action.getRow()][action.getColumn()] = action.getPreviousValue();
            redoMoves.push(action);
        }


    }

    public void redo( ){
        if(!this.redoMoves.isEmpty()) {
            Action action = redoMoves.pop();

            this.gameBoard[action.getRow()][action.getColumn()] = action.getValue();
            moves.push(action);
        }
    }
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
        trackNumbers();
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
    }

    // prefills the board before the solver to make the sudoku random
    public boolean prefil( int rowNum){
        boolean result = true;

        for(int col = 0; col <this.gameBoard.length;col++) {



            if ( this.gameBoard[rowNum][col] == 0 ){

                /// loop here
                while( this.gameBoard[rowNum][col] ==0){


                    int num = getRandomNum(9,1);

                    if (isActionValid(num, rowNum, col)) {
                        this.gameBoard[rowNum][col] = num;
                        num =getRandomNum(9,1);
                        if(prefil(  rowNum)){
                            result = true;
                            return result;
                        }else{
                            this.gameBoard[rowNum][col] = 0;
                            prefil(rowNum);
                        }


                    }else {
                        result= false;
                        return result;

                    }
                }
            }
        }
        return result;
    }
    // solves the board needs to return true for the board to be valid
    public boolean solve(){

        for(int row = 0; row <this.gameBoard.length;row++){
            for(int col = 0; col <this.gameBoard[row].length;col++){
                if( this.gameBoard[row][col] == 0) {
                    for(int number = 1  ; number <= this.getGameBoard().length; number ++ ){
                        if(isActionValid(number,row,col)){
                            this.gameBoard[row][col] = number;
                            if(solve()){
                                return true;
                            }else{
                                this.gameBoard[row][col] = 0;
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

// todo helper validation row column box    check input if not in initial board

    public boolean isValidRowCheck( int digit, int row){
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
        for( int i = 0 ; i < this.getGameBoard().length; i++ ){
            if( this.gameBoard[i][column] == digit){
                result = false;
            }
        }
        return result;
    }
    public boolean isValidBoxCheck(int digit, int row, int column ){
        boolean result = true;
        int boxRow = row - (row %  3);
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
    public boolean isActionValid( int digit, int row, int column){
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
