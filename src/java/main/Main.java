package main;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        // Determines which user interface to initialise depending on the provided argument to the program.
        if(args.length > 0 && args[0].equals(Constants.GUI)) {
            UserInterface.init();
        }
        else {
            CommandLineInterface.init();
        }
    }
}