package ttp;

import java.io.IOException;
import java.io.PrintWriter;

import ttp.loader.Loader;
import ttp.model.Problem;

public class App {

    private final PrintWriter pw;

    public App(PrintWriter pw) {
        this.pw = pw;
    }

    public void run() {
        String resource = "ttp/trivial_0.ttp";
        Loader loader = new Loader();
        try {
            Problem problem = loader.load(resource);
            System.out.println(problem);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        PrintWriter pw = new PrintWriter(System.out);
        App app = new App(pw);
        app.run();
        pw.close();
    }
}
