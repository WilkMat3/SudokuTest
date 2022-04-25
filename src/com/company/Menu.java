package com.company;

import java.io.*;
import java.util.*;
/**
 * Class represents game menu
 */
public class Menu
{

        // method used to get a random number within specified range
    public static   int getRandomNum(int max , int min){
        int num = (int)(Math.random()*(max-min+1)+min);

        return num;
    }


// checking if a string can be casted into int and if it is within specified range

    public  boolean valueIsAnIntWithRange(String value, int max , int min) {
        boolean result = false;
        try {
            int toBeChecked = Integer.parseInt(value);

            if(toBeChecked >= min && toBeChecked <= max){
                result = true; //this line won't be executed if parsing fails
             //   System.out.println(" comparison" + result);
            }else{
                System.out.println("Number not within range");
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid input");

        }
        return result;
    }
    // method that checks if number is a number on the initial board - don't want user to be able to change them also checking if input is valid with current board
    public boolean checkInitialBoard( Sudoku sudoku, int row , int column,int value){
        boolean result = true;
        if(sudoku.getStartingBoard()[row][column] != 0){
            result = false;
          //  System.out.println("check if place is prepopulated" + result);
        }
        if(result == true){
            if(!sudoku.isActionValid(value,row,column)){
                result = false;
               // System.out.println("check if action is valid" + result);
            }
        }
        return result;
    }
// checks if the input is valid using helper methods that check if inpu is a valid int within range and if it is not on the initial board
    public  boolean boardInputChecks (String row, String col, String val, Sudoku sudoku){
        boolean result = true;
        if(!valueIsAnIntWithRange(row,9,1)){
            result = false;
       //     System.out.println(" row r" + result);
        }
        if (! valueIsAnIntWithRange(col,9,1)){
            result = false;
         //   System.out.println(" col r" + result);
        }
        if ( !valueIsAnIntWithRange(val,9,0)){
            result = false;
        //    System.out.println(" val r" +result);
        }
        if(result== true){ // only checking this if other checks are returning true
            int parsedRow = Integer.parseInt(row) - 1;
            int parsedCol = Integer.parseInt(col) -1;
            int parsedVal = Integer.parseInt(val);
            if( !checkInitialBoard(sudoku,parsedRow,parsedCol,parsedVal)){
                result=false;
               // System.out.println(" check initial ");
            }
        }

        return result;
    }
// method that returns a string depending on the completion of the board
    public String completeGame(Sudoku sudoku){
        String result = " Congrats you completed the sudoku ";
        if(!sudoku.finish()){
            result = "Better luck next time";
        }
        return result;
    }
    // takes user input and if it is vale
    public  void manipulateBoard(Sudoku sudoku) throws IOException {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the row number of the filed you want to change");
        String rowChoice = input.nextLine();
        System.out.println("Enter the column number of the filed you want to change");
        String colChoice = input.nextLine();
        System.out.println("Enter the number between 0 - 9");
        String valueChoice = input.nextLine();
        if(!boardInputChecks(rowChoice,colChoice,valueChoice,sudoku)){

            System.out.println("one of the inputs was wrong");

            inGameMenu(sudoku); // go back to menu
        }else{
            int parsedRow = Integer.parseInt(rowChoice) - 1; // -1 because grid runs from 0 to 8
            int parsedCol = Integer.parseInt(colChoice) -1;
            int parsedVal = Integer.parseInt(valueChoice);
            sudoku.setNum(new Action(parsedRow,parsedCol,parsedVal,sudoku.getGameBoard()[parsedRow][parsedCol])); // setting the number

            inGameMenu(sudoku);
        }


    }
    // used to play previous moves
    public  void automaticMove(Sudoku sudoku) throws IOException {


            sudoku.setNum(sudoku.getReverseMoves().poll()); // using the queue

        automaticGameMenu(sudoku); // go back to the menu
        }



    public  Sudoku generateGame(){
        Sudoku sudoku = new Sudoku();
        boolean valid = false;
        do {
            if (sudoku.prefil(getRandomNum(8,0)) && sudoku.prefil(getRandomNum(8,0))){ //prefill 2 random rows they can be the same
                valid = true;
                break;
            } else{
                generateGame();
            }
        }while( valid == false ); // runs until the random rows don't contradict each other in any way
        while(!sudoku.solve()){ // runs until it is solved

        }
        return sudoku;
    }
    private  void StartGame(String mode, int max , int min) throws IOException {

        System.out.println("You started game on "+ mode + " mode");

        Sudoku sudoku = generateGame();
        sudoku.prepareGameBoard(getRandomNum(max, min));
        inGameMenu(sudoku);
    }

    private  void inGameMenu(Sudoku sudoku) throws IOException {

        Scanner input = new Scanner(System.in);  // Create a Scanner object
        sudoku.printBoard();
        System.out.println("Rules:");
        System.out.println("Fill each row, column, or 3×3 region with the numbers each number between 1 and 9 exactly once.");
        System.out.println("Options:");
        System.out.println("Select one of the options 1 - to enter a digit  2 - to undo  3 - to redo  4- to save game  5- when you completed the sudoku or you want to give up " );
        String choice = input.nextLine();




            switch (choice) {
                case "1":
                    manipulateBoard(sudoku);

                case "2":
                    sudoku.undo();
                    inGameMenu(sudoku);

                case "3":
                    sudoku.redo();
                    inGameMenu(sudoku);

                case "4":
                    System.out.println("Enter File Name of your game");
                    String filename = input.nextLine();
                    saveData(sudoku,filename);
                    inGameMenu(sudoku);
                case "5":
                    System.out.println("Finish");

                    System.out.println(completeGame(sudoku));
                    System.out.println("Do you want to save this game ? Y/N");
                    String saveFinish = input.nextLine();
                    if(saveFinish.equalsIgnoreCase("Y")){
                        System.out.println("Enter File Name of your game");
                        String name = input.nextLine();
                        saveData(sudoku,name);
                        run();
                    }else{
                    run();
                    }


                    break;

                default:
                    System.out.println("Enter a number within the range");
                    inGameMenu(sudoku);
                    break;
            }

    }
// main menu
    public  void run() throws IOException {
        Scanner input = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Hello user, welcome to Sudoku game");
        System.out.println("Select one of the following options ");
        System.out.println("1)EASY ");
        System.out.println("2)Medium ");
        System.out.println("3)HARD ");
        System.out.println("4)LOAD ");
        System.out.println("5)Exit ");
        String choice = input.nextLine();

        switch (choice) {
            case "1":
                StartGame("Easy",31,26); // max and min specify range of empty spaces actual number is random

            case "2":
                StartGame("Medium",36,32);

            case "3":

                StartGame("HARD",41,37);

            case "4": // option to load the game
                System.out.println("Enter file name of the game you want to load ");
                String filename = input.nextLine();
                System.out.println("Do you want to automatically replay the game ? Y/N");
                String decison = input.nextLine();
                if(decison.equalsIgnoreCase("Y")){
                    readData(filename,true);
                }else if ( decison.equalsIgnoreCase("N")){
                    readData(filename,false);
                }else{
                    System.out.println("your input was invalid");
                   run();
                }


            case "5":
                System.out.println("EXIT");
                System.exit(0);
                break;

            default:
                System.out.println("Enter a number within the range");
                run();

        }

        }


// serialasing data and saving to file
    public void saveData(Sudoku sudoku, String fname) throws IOException {
        ArrayList<Object> allData = new ArrayList<>(); // various data types saved to this arraylist
        allData.add(sudoku.getGameBoard());
        allData.add(sudoku.getStartingBoard());
        allData.add(sudoku.getMoves());
        allData.add(sudoku.getRedoMoves());
        allData.add(sudoku.getReverseMoves());
        try{
            FileOutputStream fileOutput = new FileOutputStream(fname+".ser"); // saving to file
            ObjectOutputStream output = new ObjectOutputStream(fileOutput);
            output.writeObject(allData);
            output.close();
            fileOutput.close();

        }catch (IOException e){
            System.out.println("Error when saving a file");
        }
    }

    public void readData(String fname, boolean isAutmaticRepalyActive) throws IOException {
        ArrayList<Object> loadedData = new ArrayList<>();

        try {
            FileInputStream fileInput = new FileInputStream(fname+".ser");
            ObjectInputStream input = new ObjectInputStream(fileInput);
            loadedData = (ArrayList<Object>)input.readObject();
            input.close();
            fileInput.close();
        } catch (IOException e) {
            System.out.println("Error when loading a file");
            run();
            return;
      } catch (ClassNotFoundException e) {
            e.printStackTrace();
            run();
            return;
        }

        int[][] gameBoard = (int[][]) loadedData.get(0);
        int[][] startingBoard = (int[][]) loadedData.get(1);
        Stack<Action> moves = (Stack<Action>) loadedData.get(2);
        Stack<Action> redoMoves = (Stack<Action>) loadedData.get(3);
        Queue<Action> reverseMoves = (Queue<Action>) loadedData.get(4);
// rebuilding the sudoku
        Sudoku sudoku = new Sudoku();
        sudoku.setGameBoard(gameBoard);
        sudoku.setStartingBoard(startingBoard);
        sudoku.setMoves(moves);
        sudoku.setRedoMoves(redoMoves);
        sudoku.setReverseMoves(reverseMoves);
        if(isAutmaticRepalyActive){ // checks what user selected
            sudoku.getMoves().removeAllElements(); // need to do it as actions from the queue are added to the moves stack
            automaticGameMenu(sudoku);
        }else{
            inGameMenu(sudoku);
        }



    }
// automatic game menu  allows for replaying the game move by move
    private void automaticGameMenu(Sudoku sudoku) throws IOException {

        Scanner input = new Scanner(System.in);  // Create a Scanner object
        sudoku.printBoard();


        System.out.println("Options:");
        System.out.println("Select one of the options 1- auto play next move 2- exit  ");
        String choice = input.nextLine();



        switch (choice) {

            case "1":
                if(!sudoku.getReverseMoves().isEmpty()){ // checking for eerors
                    automaticMove(sudoku);
                }else{
                    System.out.println("No moves to replay");
                    automaticGameMenu(sudoku);
                }


            case "2":
                System.out.println("Do you want to finish the game yourself? Y/N");
                String playQ = input.nextLine();
                if(playQ.equalsIgnoreCase("Y")){
                    inGameMenu(sudoku);
                }else if ( playQ.equalsIgnoreCase("N")){
                   run();
                }else{
                    System.out.println("your input was invalid");
                    automaticGameMenu( sudoku);
                }

                break;


            default:
                System.out.println("Number not within range,try again");
                automaticGameMenu(sudoku);

        }
    }

}


