package br.com.leandro.retrofitpokemonapi.models;

import java.util.ArrayList;

public class PokemonResposta {

    private ArrayList<Pokemon> results;

    public ArrayList<Pokemon> getResults() {
        return results;
    }

    public void setResults(ArrayList<Pokemon> results) {
        this.results = results;
    }
}
