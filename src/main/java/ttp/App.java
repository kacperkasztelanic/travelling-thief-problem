package ttp;

import java.io.PrintWriter;

public class App {

    private final PrintWriter pw;

    public App(PrintWriter pw) {
        this.pw = pw;
    }

    public void run() {
        pw.println("It works!");
    }

    public static void main(String[] args) {
        PrintWriter pw = new PrintWriter(System.out);
        App app = new App(pw);
        app.run();
        pw.close();
    }
}
