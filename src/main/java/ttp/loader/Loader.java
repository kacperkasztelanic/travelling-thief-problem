package ttp.loader;

import java.io.IOException;

import ttp.model.Problem;

public interface Loader {

    Problem load(String resource) throws IOException;
}