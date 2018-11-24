package com.tulun;

import com.tulun.main.Run;

public class App {

    public static void main(String[] args) throws Exception{

        String path1 = "/Users/liguozheng/Documents/Java/java_2018_xia/marriage_mate/src/male.txt";
        String path2 = "/Users/liguozheng/Documents/Java/java_2018_xia/marriage_mate/src/female.txt";
        String path3 = "/Users/liguozheng/Documents/Java/java_2018_xia/marriage_mate/src/players.txt";

        Run a = new Run(path1,path2,path3);

        long start = System.currentTimeMillis();

        a.start();

        long end = System.currentTimeMillis();

        System.out.println(end-start+" ms");

    }
}

