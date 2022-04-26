package com.company;

import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws IOException {
        Menu menu = new Menu();

        menu.run();

        // left for measuring the
     /*   ArrayList<Long> allT = new ArrayList<Long>();
        for(int i = 0; i < 10; i++) {


            long start = System.nanoTime();

            Sudoku s = menu.generateGame();

            s.prepareGameBoard(10);


            long elapsed = System.nanoTime() - start;
            allT.add(elapsed);

        }
        long total = 0;
        double avg = 0;
        for(int i = 0; i < allT.size(); i++){

            total += allT.get(i);
            avg = total / allT.size(); // finding their average value


        }
        System.out.println("The Average is: " + avg);


*/
    }


}
