package ttp;

import java.io.IOException;
import java.io.PrintWriter;

import ttp.loader.Loader;
import ttp.loader.LoaderFactory;
import ttp.model.Problem;

public class App {

    private final PrintWriter pw;
    private final PrintWriter epw;

    public App(PrintWriter pw, PrintWriter epw) {
        this.pw = pw;
        this.epw = epw;
    }

    public void run() {
        String resource = "ttp/trivial_0.ttp";
        Loader loader = LoaderFactory.getInstance();
        Problem problem = null;
        try {
            problem = loader.load(resource);
        } catch (IOException e) {
            epw.println("Could not load resource: " + resource);
            return;
        }
        pw.println(problem);
    }

    public static void main(String[] args) {
        PrintWriter pw = new PrintWriter(System.out);
        PrintWriter epw = new PrintWriter(System.err);
        App app = new App(pw, epw);
        app.run();
        pw.close();
        epw.close();
    }
}
