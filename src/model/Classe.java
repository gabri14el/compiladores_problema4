package model;

import java.util.HashMap;

/***
 * Representa o escopo de uma classe
 */
public class Classe extends Escopo{

    String classeMae;
    HashMap<String, Metodo> metodos;

    /**
     * Construtor para classe que herda de alguma outra classe
     * @param escopoPai
     * @param classeMae
     */
    public Classe(Escopo escopoPai, String classeMae) {
        super(escopoPai);
        metodos = new HashMap<>();
        this.classeMae = classeMae;
    }

    /**
     * Construtor para classe que nao herda de ninguem
     * @param escopoPai
     */
    public Classe(Escopo escopoPai) {
        super(escopoPai);
        metodos = new HashMap<>();
        this.classeMae = null;
    }

    /**
     * Retorna classe mae
     * @return nome da classe da mae
     */
    public String getClasseMae(){
        return classeMae;
    }

    /**
     * Retorna se a classe em questao herda de alguma outra classe
     * @return se classe herda ou nao
     */
    public boolean herdaDeAlguem(){
        return classeMae != null;
    }

    /**
     * Adiciona um novo metodo a classe
     * @param retorno retorno do metodo
     * @param nome nome do metodo
     */
    public void addMetodo(String retorno, String nome){
        metodos.put(nome, new Metodo(this, nome, retorno));
    }

    /**
     * Pega metodo passando nome
     * @param nome nome do metodo que deseja
     * @return objeto que representa o metodo
     */
    public Metodo getMetodo(String nome){
        return metodos.get(nome);
    }


    /**
     * Retorna se existe um metodo com um nome no escopo da classe
     * @param nome nome do metodo
     * @return se ele existe ou nao
     */
    public boolean metodoExiste(String nome){
        return getMetodo(nome) != null;
    }

}
