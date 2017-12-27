package model;

import java.util.HashMap;

public class Classe extends Escopo{

    String classeMae;
    HashMap<String, Metodo> metodos;
    public Classe(Escopo escopoPai, String classeMae) {
        super(escopoPai);
        metodos = new HashMap<>();
        this.classeMae = classeMae;
    }

    public Classe(Escopo escopoPai) {
        super(escopoPai);
        metodos = new HashMap<>();
        this.classeMae = null;
    }

    public String getClasseMae(){
        return classeMae;
    }

    public boolean herdaDeAlguem(){
        return classeMae != null;
    }

    public void addMetodo(String retorno, String nome){
        metodos.put(nome, new Metodo(this, nome, retorno));
    }

    public Metodo getMetodo(String nome){
        return metodos.get(nome);
    }

}
