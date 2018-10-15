package ttp.loader.properties;

import java.util.Properties;

import ttp.loader.exception.LoadException;

public interface PropertyLoader {

    Properties load(String file) throws LoadException;
}