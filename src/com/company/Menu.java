package com.company;



import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class Menu
{


    public static   int getRandomNum(int max , int min){
        int num = (int)(Math.random()*(max-min+1)+min);

        return num;
    }


    public  boolean valueIsAnInt(String value) {
        boolean result = false;
        try {
            int toBeChecked = Integer.parseInt(value);
            result = true; //this line won't be executed if parsing fails
        } catch (NumberFormatException e) {
            System.out.println("Invalid input");

        }
        return result;
    }

    public  boolean valueIsAnIntWithRange(String value, int max , int min) {
        boolean result = false;
        try {
            int toBeChecked = Integer.parseInt(value);
            System.out.println(toBeChecked);
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
    // method that checks if number is an initial number - don't want user to be able to change them also checking if input is valid with current board
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
        if(result== true){
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

    public String completeGame(Sudoku sudoku){
        String result = " Congrats you completed the sudoku ";
        if(!sudoku.finish()){
            result = "Better luck next time";
        }
        return result;
    }
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

            inGameMenu(sudoku);
        }else{
            int parsedRow = Integer.parseInt(rowChoice) - 1;
            int parsedCol = Integer.parseInt(colChoice) -1;
            int parsedVal = Integer.parseInt(valueChoice);
            sudoku.setNum(new Action(parsedRow,parsedCol,parsedVal,sudoku.getGameBoard()[parsedRow][parsedCol]));

            inGameMenu(sudoku);
        }


    }
    public  Sudoku generateGame(){
        Sudoku sudoku = new Sudoku();
        boolean valid = false;
        do {
            if (sudoku.prefil(getRandomNum(8,0)) && sudoku.prefil(getRandomNum(8,0))){
                valid = true;
                break;
            } else{
                generateGame();
            }
        }while( valid == false );
        while(!sudoku.solve()){

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
        System.out.println("Fill in any row, column, or 3×3 region with the numbers 1-9 exactly once.");
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

                case "5":
                    System.out.println("Finish");

                    System.out.println(completeGame(sudoku));
                    run();
                    break;

                default:
                    System.out.println("Enter a number within the range");
                    break;
            }

    }

    public  void run() throws IOException {
        Scanner input = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Hello user");
        System.out.println("Select one of the following options ");
        System.out.println("1)EASY ");
        System.out.println("2)Medium ");
        System.out.println("3)HARD ");
        System.out.println("4)LOAD ");
        System.out.println("5)Exit ");
        String choice = input.nextLine();

        switch (choice) {
            case "1":
                StartGame("Easy",31,26);

            case "2":
                StartGame("Medium",36,32);

            case "3":

                StartGame("HARD",41,37);

            case "4":
                System.out.println("Enter File Name of you previous game");
                String filename = input.nextLine();
                readData(filename);

            case "5":
                System.out.println("EXIT");
                System.exit(0);
                break;

            default:
                System.out.println("Enter a number within the range");

        }

        }



    public void saveData(Sudoku sudoku, String fname) throws IOException {
        ArrayList<Object> allData = new ArrayList<>();
        allData.add(sudoku.getGameBoard());
        allData.add(sudoku.getStartingBoard());
        allData.add(sudoku.getMoves());
        allData.add(sudoku.getRedoMoves());
        try{
            FileOutputStream fileOutput = new FileOutputStream(fname+".ser");
            ObjectOutputStream output = new ObjectOutputStream(fileOutput);
            output.writeObject(allData);
            output.close();
            fileOutput.close();

        }catch (IOException e){
            System.out.println("Error when saving a file");
        }
    }

    public void readData(String fname) throws IOException {
        ArrayList<Object> loadedData = new ArrayList<>();

        try {
            FileInputStream fileInput = new FileInputStream(fname+".ser");
            ObjectInputStream input = new ObjectInputStream(fileInput);
            loadedData = (ArrayList<Object>)input.readObject();
            input.close();
            fileInput.close();
        } catch (IOException e) {
            System.out.println("Error when loading a file");
            return;
      } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        int[][] gameBoard = (int[][]) loadedData.get(0);
        int[][] startingBoard = (int[][]) loadedData.get(1);
        Stack<Action> moves = (Stack<Action>) loadedData.get(2);
        Stack<Action> redoMoves = (Stack<Action>) loadedData.get(3);


        Sudoku sudoku = new Sudoku();
        sudoku.setGameBoard(gameBoard);
        sudoku.setStartingBoard(startingBoard);
        sudoku.setMoves(moves);
        sudoku.setRedoMoves(redoMoves);
        inGameMenu(sudoku);


    }

    }


