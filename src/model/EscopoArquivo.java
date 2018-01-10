package model;

import java.util.HashMap;


/**
 * Classe que representa a escopo do arquivo (maior escopo dentro do projeto)
 */
public class EscopoArquivo extends Escopo {


    //==========================================================
    HashMap<String, Classe> classes;
    HashMap<String, String> constantes;
    //==========================================================


    /**
     * Construtor padrao
     */
    public EscopoArquivo() {
        super(null);
        classes = new HashMap<>();
        constantes = new HashMap<>();
    }

    /**
     * Metodo que retorna o tipo de uma constante de acordo com o nome
     * @param nome nome para se buscado
     * @return o tipo da constante ou null, caso ela nao exista
     */
    public String tipoDeConstante(String nome){
        return constantes.get(nome);
    }

    public boolean constanteExiste(String nome){
        return tipoDeConstante(nome) != null;
    }

    /**
     * Adiciona uma constante ao escopo da classe
     * @param tipo
     * @param nome
     */
    public void addConstante(String tipo, String nome){
        constantes.put(nome, tipo);
    }

    /**
     * Retorna o objeto classe a partir de seu nome
     * @param nome nome da classe
     * @return objeto classe
     */
    public Classe pegaClasse(String nome){
        return classes.get(nome);
    }

    /**
     * Adiciona classe ao escopo
     * @param nome nome da classe
     * @param classe objeto classe
     */
    public void addClasse(String nome, Classe classe){
        classes.put(nome, classe);
    }
}
