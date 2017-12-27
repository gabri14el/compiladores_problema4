package model;

import java.util.ArrayList;

public class Metodo extends Escopo {

    ArrayList<String> ordemAtributos;
    public Metodo(Escopo escopoPai, String nome, String retorno) {
        super(escopoPai);
        variaveis.put(nome, retorno);
        ordemAtributos = new ArrayList<>();
    }

    public void addAtributo(String tipo, String nome){
        variaveis.put(nome, tipo);
        ordemAtributos.add(tipo);
    }
}
