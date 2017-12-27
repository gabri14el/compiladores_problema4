package model;

import java.util.HashMap;

public class EscopoArquivo extends Escopo {

    HashMap<String, Classe> classes;
    HashMap<String, String> constantes;

    public EscopoArquivo() {
        super(null);
        classes = new HashMap<>();
        constantes = new HashMap<>();
    }

    public String tipoDeConstante(String nome){
        return constantes.get(nome);
    }

    public void addConstante(String tipo, String nome){
        constantes.put(nome, tipo);
    }

    public Classe pegaClasse(String nome){
        return classes.get(nome);
    }

    public void addClasse(String nome, Classe classe){
        classes.put(nome, classe);
    }
}
