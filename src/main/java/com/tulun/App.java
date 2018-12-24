package com.tulun;


import com.tulun.main.Until;

public class App {

    public static void main(String[] args) throws Exception{

        String path1 = "src/male.txt";                   //
        String path2 = "src/female.txt";
        String path3 = "src/players.txt";


        long start = System.currentTimeMillis();
        Until objectObjectRun = new Until(path1,path2,path3);
        objectObjectRun.start();

        long end = System.currentTimeMillis();

        System.out.println(end-start+"ms");

    }


}

