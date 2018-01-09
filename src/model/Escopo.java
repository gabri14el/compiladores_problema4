package model;

import java.util.HashMap;


/**
 * Representa escopo menores como ifs, fors e etc
 */
public class Escopo {

    HashMap<String, String> variaveis;
    Escopo escopoPai;
    HashMap<String, String> vetores;

    public Escopo(Escopo escopoPai){
        variaveis = new HashMap<>();
        vetores = new HashMap<>();
        this.escopoPai = escopoPai;
    }

    public String tipoVariavel(String nome){
        return variaveis.get(nome);
    }

    public Escopo getEscopoPai(){
        return escopoPai;
    }

    public void addVetor(String tipo, String nome){
        vetores.put(nome, tipo);
        variaveis.put(nome, "vetor");
    }

    public String tipoVetor(String nome){
        return vetores.get(nome);
    }
    public void addVariavel(String tipo, String nome){
        variaveis.put(nome, tipo);
    }

    public boolean variavelExiste(String identificador){
        return variaveis.get(identificador) != null;
    }

}
