package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Queue;

public class Metodo extends Escopo {


    //========================================
    ArrayList<String> ordemAtributos;
    HashMap<String, String> atributos;
    //========================================


    /***
     * Construtor
     * @param escopoPai escopo pai
     * @param nome nome do método
     * @param retorno retorno do método
     */
    public Metodo(Escopo escopoPai, String nome, String retorno) {
        super(escopoPai);
        variaveis.put(nome, retorno);
        ordemAtributos = new ArrayList<>();
        atributos = new HashMap<>();
    }

    /***
     * Adicona parâmetro ao método
     * @param tipo tipo do parâmetro
     * @param nome nome do parâmetro
     */
    public void addAtributo(String tipo, String nome){
        //adiciona nos maps de variáveis, atributos e na lista
        //para garantir a verificação da ordem numa chamada de método
        variaveis.put(nome, tipo);
        atributos.put(nome, tipo);
        ordemAtributos.add(tipo);
    }


    /***
     * Método que adiciona atributos do tipo vetor
     * @param tipo tipo do vetor
     * @param nome nome da variável
     */
    public void addAtributoVetor(String tipo, String nome){
        super.addVetor(tipo, nome);
        atributos.put(nome, tipo);
        ordemAtributos.add(tipo);
    }

    /***
     * Método que retorna um iterador para verificação de ordem dos parâmetro quando um método
     * chamado
     * @return iterator com a ordem e tipo dos parâmetros
     */
    public Iterator<String> getParametros(){
        return ordemAtributos.iterator();
    }

    /***
     * Verifica se um parâmetro existe.
     * @param nome nome do parâmetro
     * @return se ele existe ou não
     */
    public boolean atributoExiste(String nome){
        return atributos.get(nome) != null;
    }
}
