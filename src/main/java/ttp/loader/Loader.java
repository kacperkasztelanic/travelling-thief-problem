package ttp.loader;

import ttp.model.Problem;

public interface Loader {

    Problem load(String resource) throws LoadException;
}