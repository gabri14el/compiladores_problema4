package model;

import java.util.HashMap;


/**
 * Representa escopo menores como ifs, fors e etc
 */
public class Escopo {

    //=============================================
    HashMap<String, String> variaveis;
    Escopo escopoPai;
    HashMap<String, String> vetores;
    //=============================================

    /**
     * Construtor padrao de um escopo
     * @param escopoPai escopo pai
     */
    public Escopo(Escopo escopoPai){
        variaveis = new HashMap<>();
        vetores = new HashMap<>();
        this.escopoPai = escopoPai;
    }

    /**
     * Metodo retorna o tipo de uma variavel a partir de seu nome
     * @param nome nome da variavel
     * @return tipo da variavel
     */
    public String tipoVariavel(String nome){
        return variaveis.get(nome);
    }


    /**
     * Metodo retorna o escopo pai de um escopo
     * @return escopo pai
     */
    public Escopo getEscopoPai(){
        return escopoPai;
    }

    /**
     * Adiciona vetor ao escopo
     * @param tipo tipo do vetor
     * @param nome nome do vetor
     */
    public void addVetor(String tipo, String nome){
        vetores.put(nome, tipo);
        variaveis.put(nome, "vetor");
    }

    /**
     * Metodo retorna o tipo de um vetor
     * @param nome nome do vetor
     * @return tipo do vetor
     */
    public String tipoVetor(String nome){
        return vetores.get(nome);
    }

    /**
     * Metodo que adiciona variavel ao escopo
     * @param tipo tipo da variavel
     * @param nome nome da variavel
     */
    public void addVariavel(String tipo, String nome){
        variaveis.put(nome, tipo);
    }

    /**
     * Metodo que verifica se uma variavel existe no escopo
     * @param identificador nome da variavel
     * @return se a variavel existe ou nao
     */
    public boolean variavelExiste(String identificador){
        return variaveis.get(identificador) != null;
    }

    /**
     * Metodo que verifica se um vetor existe no escopo
     * @param identificador nome do vetor
     * @return se o vetor existe ou nao
     */
    public boolean vetorExiste(String identificador){
        return vetores.get(identificador) != null;
    }
}
