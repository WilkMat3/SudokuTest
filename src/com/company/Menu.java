package com.company;



import java.util.Scanner;

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
    public boolean checkInitialBoard( Sudoku s, int row , int column,int value){
        boolean result = true;
        if(s.getStartingBoard()[row][column] != 0){
            result = false;
          //  System.out.println("check if place is prepopulated" + result);
        }
        if(result == true){
            if(!s.isActionValid(value,row,column)){
                result = false;
               // System.out.println("check if action is valid" + result);
            }
        }
        return result;
    }

    public  boolean boardInputChecks (String row, String col, String val, Sudoku s){
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
            if( !checkInitialBoard(s,parsedRow,parsedCol,parsedVal)){
                result=false;
               // System.out.println(" check initial ");
            }
        }

        return result;
    }

    public String completeGame(Sudoku s){
        String result = " Congrats you completed the sudoku ";
        if(!s.finish()){
            result = "Better luck next time";
        }
        return result;
    }
    public  void manipulateBoard(Sudoku s){
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the row number of the filed you want to change");
        String rowChoice = input.nextLine();
        System.out.println("Enter the column number of the filed you want to change");
        String colChoice = input.nextLine();
        System.out.println("Enter the number between 0 - 9");
        String valueChoice = input.nextLine();
        if(!boardInputChecks(rowChoice,colChoice,valueChoice,s)){

            System.out.println("one of the inputs was wrong");

            inGameMenu(s);
        }else{
            int parsedRow = Integer.parseInt(rowChoice) - 1;
            int parsedCol = Integer.parseInt(colChoice) -1;
            int parsedVal = Integer.parseInt(valueChoice);
            s.setNum(new Action(parsedRow,parsedCol,parsedVal,s.getGameBoard()[parsedRow][parsedCol]));

            inGameMenu(s);
        }


    }
    public  Sudoku generateGame(){
        Sudoku s = new Sudoku();
        boolean valid = false;
        do {
            if (s.prefil(getRandomNum(8,0)) && s.prefil(getRandomNum(8,0))){
                valid = true;
                break;
            } else{
                generateGame();
            }
        }while( valid == false );
        while(!s.solve()){

        }
        return s;
    }
    private  void StartGame(String mode, int max , int min){

        System.out.println("You started game on "+ mode + " mode");

        Sudoku s = generateGame();
        s.prepareGameBoard(getRandomNum(max, min));
        inGameMenu(s);
    }

    private  void inGameMenu(Sudoku s) {

        Scanner input = new Scanner(System.in);  // Create a Scanner object
        s.printBoard();
        System.out.println("Select one of the options 1 - to enter a digit  2 - to undo  3 - to redo  4- to save game  5- to exit " );
        String choice = input.nextLine();




            switch (choice) {
                case "1":
                    manipulateBoard(s);

                case "2":
                    s.undo();
                    inGameMenu(s);

                case "3":
                    s.redo();
                    inGameMenu(s);

                case "4":
                    System.out.println("Save Game");

                case "5":
                    System.out.println("Finish");
                    completeGame(s);
                    break;

                default:
                    System.out.println("Enter a number within the range");
                    break;
            }

    }

    public  void run(){
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Hello user");
        System.out.println("Select one of the following options ");
        System.out.println("1)EASY ");
        System.out.println("2)Medium ");
        System.out.println("3)HARD ");
        System.out.println("4)LOAD ");
        System.out.println("5)Exit ");
        String choice = myObj.nextLine();

        switch (choice) {
            case "1":
                StartGame("Easy",31,26);

            case "2":
                StartGame("Medium",35,32);

            case "3":

                StartGame("HARD",40,36);

            case "4":
                System.out.println("Load Game");

            case "5":
                System.out.println("EXIT");
                break;

            default:
                System.out.println("Enter a number within the range");

        }

        }

        //   clearScreen();



    }


