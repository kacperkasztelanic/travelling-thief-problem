package ttp.loader.problem;

import ttp.loader.exception.LoadException;
import ttp.model.Problem;

public interface Loader {

    Problem load(String resource) throws LoadException;
}