package model;

import java.util.*;

/***
 * Representa o escopo de um metodo
 */
public class Metodo extends Escopo {


    //========================================
    ArrayList<String> ordemAtributos;
    HashMap<String, String> atributos;
    String nome;
    String retorno;
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
        this.nome = nome;
        this.retorno = retorno;
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

    /**
     * Método compara ordem de parâmetros recebida com a de método em questão
     * @param parametros
     * @return
     */
    public boolean comparaOrdemDosParametros(List<String> parametros){
        boolean iguais = true;
        if(parametros.size() != ordemAtributos.size())
            return false;
        else{
            for(int i = 0; i < parametros.size(); i++){
                if(!parametros.get(i).equals(ordemAtributos.get(i)))
                    iguais = false;
            }
            return iguais;
        }
    }

    /**
     * Compara se dois métodos são iguais
     * Julgamos dois métodos como iguais aqui caso eles tenham o mesmo nome, retorno,
     * mesma quantidade e tipo de parâmetros.
     * @param obj metodo a ser comparado
     * @return se são iguais ou não
     */
    @Override
    public boolean equals(Object obj) {
        boolean iguais = true;
        if(obj instanceof Metodo){
            Metodo recebido = (Metodo) obj;
            if(recebido.nome.equals(this.nome)){
                if(recebido.retorno.equals(retorno)){
                    if(recebido.ordemAtributos.size() == ordemAtributos.size()){
                        for(int i = 0; i < recebido.ordemAtributos.size(); i++){
                            if(!recebido.ordemAtributos.get(i).equals(ordemAtributos.get(i)))
                                iguais = false;
                        }
                        return iguais;
                    }else
                        return false;
                }else
                    return false;
            } else
                return false;

        }
        else
            return false;
    }
}
