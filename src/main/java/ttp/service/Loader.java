package ttp.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import ttp.model.Problem;

public class Loader {

    public Problem load(String resource) throws IOException {
        return null;
    }

    private List<String> readLines(String resource) throws IOException {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(getClass().getClassLoader().getResourceAsStream(resource)))) {
            return br.lines().collect(Collectors.toList());
        }
    }
}
