/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 *
 * @author Anderson Queiroz
 */
public class AnalisadorSintatico {

    public BufferedReader codigoFonte;
    public String arquivo;
    public FileWriter arq;
    public PrintWriter salvarArq;

    public Stack pilha = new Stack();
    public String lexema = "";
    public String token = "";
    public String linhaAtual;

    public String[] s;
    public String maquina;
    int estado = 0;
    public boolean validacao = false;
    public boolean metodo_main = false;

    public final List tipo = new ArrayList();
    public final List boleano = new ArrayList();
    public final List operAritmeticos = new ArrayList();
    public final List operRelacionais = new ArrayList();
    public final List operLogicos = new ArrayList();


    EscopoArquivo escopoArquivo = new EscopoArquivo();

    //-----------------------------------------------------
    //VARIAVEIS AUXILIARES
    //-----------------------------------------------------
    String tipoAtual;
    String idAtual;
    Escopo escopoAtual;
    boolean heranca = false;


    public AnalisadorSintatico(String arquivo) {

        tipo.add("int");
        tipo.add("bool");
        tipo.add("string");
        tipo.add("float");

        boleano.add("true");
        boleano.add("false");
        escopoAtual = escopoArquivo;

        try {
            codigoFonte = new BufferedReader(new FileReader(arquivo));
        } catch (FileNotFoundException ex) {
            System.err.println("Arquivo não encontrado");
        }
    }

    public boolean consumir() throws IOException {
        arquivo = codigoFonte.readLine();
        if (arquivo == null) {
            return true;
        } else {
            s = arquivo.split(" ");
            linhaAtual = s[0];
            lexema = s[1];
            token = s[2];
            return false;
        }
    }

    public void analisadorSintatico() throws IOException {
        arq = new FileWriter("Analisador.txt");
        salvarArq = new PrintWriter(arq);

        while (true) {
            if (consumir()) {
                break;
            }

            switch (estado) {
                case 0:
                    if (lexema.equals("final")) {
                        constantes();
                        estado = 0;
                        break;
                    } else if (tipo.contains(lexema)) {
                        tipoAtual = lexema;
                        estado = 1;
                        break;
                    } else if (lexema.equals("class")) {
                        classe(0);
                        if (boleano.contains(lexema)) {
                            estado = 1;
                        }
                        break;
                    }
                    break;

                case 1:
                    //????
                    if (lexema.equals("main")) {
                        classeMain();
                        estado = 0;
                        break;                        
                    } else if (token.equals("Identificador")) {
                        variavel(1);
                        //adicionando uma variavel global
                        idAtual = lexema;
                        escopoArquivo.addVariavel(tipoAtual, idAtual);

                        estado = 0;
                        break;
                    }
                    break;
            }
        }
        salvarArq.printf("Analise Sintática Concluída!");
        arq.close();

    }

    public boolean constantes() throws IOException {
        if (consumir()) {
            return false;
        } else {
            if (tipo.contains(lexema)) {

                tipoAtual = lexema; //seta o tipo pra ser igual ao lexema
                if (consumir()) {
                    salvarArq.printf("Linha: %s - Tipo não decladaro.%n", linhaAtual);
                    return false;
                } else {
                    if (token.equals("Identificador")) {
                        idAtual = lexema;

                        //verifica se a variável já existe
                        if(escopoArquivo.tipoDeConstante(idAtual) == null
                                && escopoArquivo.tipoVariavel(idAtual) == null)
                            escopoArquivo.addConstante(tipoAtual, idAtual);
                        else        //se a variável existir imprime o erro
                            salvarArq.printf("Linha: %s - nome de variável  já existente.%n", linhaAtual);
                        if (consumir()) {
                            salvarArq.printf("Linha: %s - Esperado um identificador.%n", linhaAtual);
                            return false;
                        } else {
                            if (lexema.equals(boleano) || token.equals("Número") || token.equals("Cadeia_de_Caracter")) {
                                if(lexema.equals(boleano))
                                {
                                    if(!tipoAtual.equals("bool"))
                                        salvarArq.printf("Linha: %s - Tipo incompatível.%n", linhaAtual);
                                } else if(token.equals("Número")){
                                    if(lexema.contains(".")){
                                        if(!tipoAtual.equals("float"))
                                            salvarArq.printf("Linha: %s - Tipo incompatível.%n", linhaAtual);
                                    }else{
                                        if(!tipoAtual.equals("int"))
                                            salvarArq.printf("Linha: %s - Tipo incompatível.%n", linhaAtual);
                                    }
                                } else if(token.equals("Cadeia_de_Caracter")){
                                    if(!tipoAtual.equals("string"))
                                        salvarArq.printf("Linha: %s - Tipo incompatível.%n", linhaAtual);
                                }
                                return true;
                            }
                        }
                    }
                }

            }

        }
        salvarArq.printf("Linha: %s - Erro na decladaçao.%n", linhaAtual);
        return false;
    }

    public boolean variavel(int estadoAtual) throws IOException {
        int state = estadoAtual;
        while (true) {
            if (consumir()) {
                return false;
            }

            switch (state) {
                case 0:
                    if (token.equals("Identificador")) {
                        idAtual = lexema;
                        state = 1;
                        break;
                    }
                    state = 15;
                    break;

                case 1:
                    if (lexema.equals(";")) {
                        if(!escopoAtual.variavelExiste(idAtual)){
                            escopoAtual.addVariavel(tipoAtual, idAtual);

                        } else{
                            salvarArq.printf("Linha: %s - Nome de variável já existe.%n", linhaAtual);
                        }
                        return true;
                    } else if (lexema.equals(",")) {
                        if(!escopoAtual.variavelExiste(idAtual)){
                            escopoAtual.addVariavel(tipoAtual, idAtual);

                        } else{
                            salvarArq.printf("Linha: %s - Nome de variável já existe.%n", linhaAtual);
                        }
                        state = 2;
                        break;
                    } else if (lexema.equals("[")) {
                        state = 5;
                        break;
                    }
                    state = 15;
                    break;

                case 2:
                    if (token.equals("Identificador")) {
                        state = 3;
                        break;
                    }
                    state = 15;
                    break;
                case 3:
                    if (lexema.equals(";")) {
                        return true;
                    } else if (lexema.equals(",")) {
                        state = 2;
                        break;
                    }
                    state = 15;
                    break;

                case 5:
                    if (token.equals("Número")) {
                        state = 6;
                        break;
                    }
                    state = 15;
                    break;
                case 6:
                    if (lexema.equals("]")) {
                        state = 7;
                        break;
                    }
                    state = 15;
                    break;

                case 7:
                    if (lexema.equals("[")) {
                        state = 5;
                        break;
                    } else if (lexema.equals(";")) {
                        //adicionar um vetor
                        if(!escopoAtual.variavelExiste(idAtual)){
                            escopoAtual.addVetor(tipoAtual, idAtual);
                        }else
                            salvarArq.printf("Linha: %s - Nome de variável já existe.%n", linhaAtual);
                        return true;
                    }
                    state = 15;
                    break;

                case 15:
                    salvarArq.printf("Linha: %s - Erro na declaração da variavel.%n", linhaAtual);
                    return false;
            }
        }
    }

    public boolean classe(int estadoAtual) throws IOException {
        int state = estadoAtual;
        while (true) {
            if (consumir()) {
                return false;
            }
            switch (state) {
                case 0:
                    if (token.equals("Identificador")) {
                        state = 1;
                        idAtual = lexema;
                        break;
                    }
                    salvarArq.printf("Linha: %s - Falta Identificador.%n", linhaAtual);
                    return false;

                case 1:
                    if (lexema.equals(":")) {
                        state = 4;
                        heranca = true;
                        break;
                    }

                    if (lexema.equals("{")) {

                        //cria o novo escopo
                        Classe aux = new Classe(escopoAtual, tipoAtual);

                        //verifica se o nome da classe já existe
                        if(escopoArquivo.pegaClasse(idAtual) == null)
                            escopoArquivo.addClasse(idAtual, aux);
                        else
                            salvarArq.printf("Linha: %s - Nome de classe já existente %n", linhaAtual);
                        escopoAtual = aux;
                        state = 2;
                        break;
                    }

                    salvarArq.printf("Linha: %s - Erro na declaração.%n", linhaAtual);
                    return false;

                case 2:
                    if (conteudo_class()) {
                        state = 3;
                        break;
                    }
                    return false;

                case 3:
                    if (lexema.equals("}")) {
                        return true;
                    }
                    salvarArq.printf("Linha: %s - Esperava fechar a chave.%n", linhaAtual);
                    return false;

                case 4:
                    if (lexema.equals(":")) {
                        state = 5;
                        break;
                    }
                    salvarArq.printf("Linha: %s - Esperado :.", linhaAtual);
                    return false;
                case 5:
                    if (token.equals("Identificador")) {
                        state = 6;
                        tipoAtual = lexema;
                        break;
                    }
                    salvarArq.printf("Linha: %s - Esperado Identificador.%n", linhaAtual);
                    return false;
                case 6:
                    if (lexema.equals("{")) {
                        state = 2;
                        Classe aux = new Classe(escopoAtual, tipoAtual);
                        Classe aux2 = escopoArquivo.pegaClasse(tipoAtual);
                        //verifiva se a classe mãe existe
                        if(aux2 != null)
                        {
                            //verifica herança múltipla
                            if(aux2.herdaDeAlguem())
                                salvarArq.printf("Linha: %s - herança não permitida: a classe mãe já herda de alguém.%n", linhaAtual);
                        }
                        else
                            salvarArq.printf("Linha: %s - classe inexistente.%n", linhaAtual);

                        //verifica se o nome da classe já existe
                        if(escopoArquivo.pegaClasse(idAtual) == null)
                            escopoArquivo.addClasse(idAtual, aux);
                        else
                            salvarArq.printf("Linha: %s - Nome de classe já existente %n", linhaAtual);
                        escopoAtual = aux;
                        break;
                    }
                    salvarArq.printf("Linha: %s - Esperado {.%n", linhaAtual);
                    return false;

            }
        }

    }

    public boolean conteudo_class() throws IOException {
        int state = 0;
        Classe classe = (Classe) escopoAtual;
        while (true) {
            switch (state) {
                case 0:
                    if (tipo.contains(lexema)) {
                        tipoAtual = lexema;
                        state = 1;
                        break;
                    }
                    state = 15;
                    break;

                case 1:
                    if (token.equals("Identificador")) {
                        idAtual = lexema;
                        state = 2;
                        break;
                    }
                    state = 15;
                    break;

                case 2:
                    if (lexema.equals(";")) {
                        if(!escopoAtual.variavelExiste(idAtual)){
                            escopoAtual.addVariavel(tipoAtual, idAtual);

                        } else{
                            salvarArq.printf("Linha: %s - Nome de variável já existe.%n", linhaAtual);
                        }
                        if (variavel(4)) {

                            state = 0;
                            break;
                        }
                    } else if (lexema.equals(",")) {
                        if(!escopoAtual.variavelExiste(idAtual)){
                            escopoAtual.addVariavel(tipoAtual, idAtual);

                        } else{
                            salvarArq.printf("Linha: %s - Nome de variável já existe.%n", linhaAtual);
                        }
                        if (variavel(2)) {
                            state = 0;
                            break;
                        }
                    } else if (lexema.equals("[")) {
                        if (variavel(5)) {
                            state = 0;
                            break;
                        }
                    }

                    if (lexema.equals("(")) {

                        //adiciona método à classe
                        Metodo aux = new Metodo(escopoAtual, idAtual, tipoAtual);
                        classe.addMetodo(tipoAtual, idAtual);
                        escopoAtual = aux;
                        if (parametro()) {
                            state = 3;
                            break;
                        }
                    }
                    state = 15;
                    break;

                case 3:
                    if (lexema.equals(")")) {
                        state = 4;
                        break;
                    }
                    state = 15;
                    break;

                case 4:
                    if (lexema.equals("{")) {
                        if (conteudo_metodo()) {
                            state = 5;
                            break;
                        }
                        return false;
                    }
                    state = 15;
                    break;

                case 5:
                    if (lexema.equals("}")) {
                        return true;
                    }
                    state = 15;
                    break;

                case 15:
                    salvarArq.printf("Linha: %s - Erro no conteúdo da calsse.%n", linhaAtual);
                    return false;
            }
            if (consumir()) {
                return false;
            }
        }

    }

    public boolean conteudo_metodo() throws IOException {
        int state = 0;

        while (true) {
            if (consumir()) {
                return false;
            }

            switch (state) {
                case 0:
                    if (tipo.contains(lexema)) {
                        tipoAtual = lexema;
                        variavel(0);
                        state = 0;
                        break;
                    } else if (lexema.equals("print")) {
                        print();
                        state = 0;
                        break;
                    } else if (lexema.equals("for")) {
                        maquinaFor();
                        state = 0;
                        break;
                    } else if (lexema.equals("if")) {
                        maquinaIf();
                        state = 0;
                        break;
                    } else if (lexema.equals("scan")) {
                        scan();
                        state = 0;
                        break;
                    } else if (token.equals("Identificador")) {
                        state = 1;
                        break;
                    }
                    escopoAtual = escopoAtual.getEscopoPai();
                    return true;

                case 1:
                    if (lexema.equals("=")) {
                        atribuicao(5);
                        state = 0;
                        break;
                    } else if (lexema.equals(":")) {
                        chamadaMetodo(3);
                        state = 0;
                        break;
                    }
                    escopoAtual = escopoAtual.getEscopoPai();
                    return false;
            }
        }
    }

    public boolean parametro() throws IOException {
        int state = 0;
        Metodo metodo = (Metodo) escopoAtual;
        while (true) {
            if (consumir()) {
                return false;
            }

            switch (state) {
                case 0:
                    if (tipo.contains(lexema)) {
                        tipoAtual = lexema;
                        state = 1;
                        break;
                    }
                    return false;

                case 1:
                    if (token.equals("Identificador")) {
                        idAtual = lexema;
                        state = 2;
                        break;
                    }
                    return false;

                case 2:
                    if (lexema.equals("[")) {
                        state = 3;
                        break;
                    }

                    if (lexema.equals(",")) {
                        if(!metodo.atributoExiste(idAtual))
                            metodo.addAtributo(tipoAtual, idAtual);
                        else
                            salvarArq.printf("Linha: %s - Nome de parâmetro já existe.%n", linhaAtual);
                        state = 0;
                        break;
                    }
                    return true;

                case 3:
                    if (token.equals("Número")) {
                        state = 4;
                        break;
                    }
                    return false;

                case 4:
                    if (lexema.equals("]")) {
                        if(!metodo.atributoExiste(idAtual)){
                            metodo.addAtributoVetor(tipoAtual, idAtual);
                        }else {
                            salvarArq.printf("Linha: %s - Nome de parâmetro já existe.%n", linhaAtual);
                        }
                        state = 5;
                        break;
                    }
                    return false;

                case 5:
                    if (lexema.equals(",")) {
                        state = 0;
                        break;
                    }
                    return true;
            }
        }
    }

    public boolean classeMain() throws IOException {
        int state = 1;

        while (true) {
            if (consumir()) {
                return false;
            }

            switch (state) {

                case 1:
                    if (lexema.equals("(")) {
                        state = 2;
                        break;
                    }
                    state = 15;
                    break;

                case 2:
                    if (lexema.equals(")")) {
                        state = 3;
                        break;
                    }
                    state = 15;
                    break;

                case 3:
                    if (lexema.equals("{")) {
                        conteudo_metodo();
                        state = 4;
                        break;
                    }
                    state = 15;
                    break;

                case 4:
                    if (lexema.equals("}")) {
                        return true;
                    }
                    state = 15;
                    break;

                case 15:
                    salvarArq.printf("Linha: %s - Erro na escrita do main.", linhaAtual);
                    return false;
            }
        }
    }

    private boolean variavelExiste(){
        boolean existe = false;
        Escopo escopo = escopoAtual;
        while(escopo != null && !existe){
            existe = escopo.variavelExiste(idAtual);
            escopo = escopo.getEscopoPai();
        }
        return existe;


    }
    public boolean print() throws IOException {
        int state = 0;
        Stack pilhaPrint = new Stack();

        while (true) {
            if (consumir()) {
                return false;
            }

            switch (state) {
                case 0:
                    if (lexema.equals("(")) {
                        state = 1;
                        break;
                    }
                    return false;

                case 1:
                    if (lexema.equals("(")) {
                        state = 1;
                        pilhaPrint.push("(");
                        break;
                    } else if (token.equals("Cadeia_de_Caracter")) {
                        state = 2;
                        break;
                    } else if (token.equals("Identificador") || token.equals("Número")) {
                        if(token.equals("Identidicador")){
                            idAtual = lexema;
                            if(!variavelExiste())
                                salvarArq.printf("Linha: %s - Variável não existe.%n", linhaAtual);;
                        }

                        state = 3;
                        break;
                    }
                    return false;

                case 2:
                    if (lexema.equals(")")) {
                        state = 6;
                        break;
                    }
                    return false;

                case 3:
                    if (token.equals("Operador_Aritmético")) {
                        state = 4;
                        break;
                    }
                    return false;

                case 4:
                    if (expAritmetica(4)) {
                        state = 5;
                        break;
                    }
                    return false;

                case 5:
                    if (lexema.equals(")")) {
                        state = 6;
                        break;
                    }
                    return false;

                case 6:
                    if (pilhaPrint.isEmpty() && lexema.equals(")")) {
                        state = 7;
                        break;
                    } else if (lexema.equals(")")) {
                        pilhaPrint.pop();
                        state = 6;
                        break;
                    }
                    return false;

                case 7:
                    return true;
            }

        }
    }

    public boolean scan() throws IOException {
        int state = 0;

        while (true) {
            if (consumir()) {
                return false;
            }

            switch (state) {
                case 0:
                    if (lexema.equals("(")) {
                        state = 1;
                        break;
                    }
                    state = 15;
                    break;

                case 1:
                    if (token.equals("Identificador")) {
                        state = 2;
                        break;
                    }
                    state = 15;
                    break;

                case 2:
                    if (lexema.equals(",")) {
                        state = 1;
                        break;
                    } else if (lexema.equals(")")) {
                        state = 3;
                        break;
                    }
                    state = 15;
                    break;

                case 3:
                    if (lexema.equals(";")) {
                        return true;
                    }
                    state = 15;
                    break;

                case 15:
                    salvarArq.printf("Linha: %d - Erro no Scan", linhaAtual);
                    return false;
            }
        }
    }

    public boolean atribuicao(int n) throws IOException {

        int state = n;
        String linha = linhaAtual;

        while (true) {
            if (consumir()) {
                return false;
            }

            switch (state) {
                case 0:
                    if (token.equals("Identificador")) {
                        state = 1;
                        break;
                    }
                    state = 15;
                    break;

                case 1:
                    if (lexema.equals("[")) {
                        state = 2;
                        break;
                    } else if (lexema.equals("=")) {
                        state = 5;
                        break;
                    }
                    state = 15;
                    break;

                case 2:
                    if (token.equals("Número")) {
                        state = 3;
                        break;
                    }
                    state = 15;
                    break;

                case 3:
                    if (lexema.equals("]")) {
                        state = 4;
                        break;
                    }
                    state = 15;
                    break;

                case 4:
                    if (lexema.equals("=")) {
                        state = 5;
                        break;
                    } else if (lexema.equals("[")) {
                        state = 2;
                        break;
                    }
                    state = 15;
                    break;

                case 5:
                    if (token.equals("Número")) {
                        expLogica();
                        if (lexema.equals(";")) {
                            return true;
                        }
                        state = 15;
                        break;
                    } else if (token.equals("Identificador")) {
                        state = 6;
                        break;
                    }

                case 6:
                    if (token.equals("Operador_Relacional") || token.equals("Operador_Aritmética")
                            || token.equals("Operador_Lógica")) {
                        state = 8;
                        break;
                    } else if (lexema.equals(":")) {
                        state = 7;
                        break;
                    }
                    state = 15;
                    break;

                case 7:
                    chamadaMetodo(3);
                    if (lexema.equals(";")) {
                        return true;
                    }
                    state = 15;
                    break;

                case 15:
                    salvarArq.printf("Linha: %s - Erro na atribuição.", linha);
                    return false;
            }

        }

    }

    public boolean maquinaIf() throws IOException {
        int state = 0;

        while (true) {
            if (consumir()) {
                return false;
            }

            switch (state) {
                case 0:
                    if (lexema.equals("(")) {
                        state = 1;
                        break;
                    }
                    state = 15;
                    break;

                case 1:
                    if (expLogica()) {
                        state = 2;
                        break;
                    }
                    state = 15;
                    break;

                case 2:
                    if (lexema.equals(")")) {
                        state = 3;
                        break;
                    }
                    state = 15;
                    break;

                case 3:
                    if (lexema.equals("{")) {
                        state = 4;
                        break;
                    }
                    state = 15;
                    break;

                case 4:
                    if (conteudo_metodo()) {
                        state = 5;
                        break;
                    }
                    state = 15;
                    break;

                case 5:
                    if (lexema.equals("}")) {
                        state = 6;
                        break;
                    }
                    state = 15;
                    break;

                case 6:
                    if (lexema.equals("else")) {
                        state = 7;
                        break;
                    }
                    state = 15;
                    break;

                case 7:
                    if (lexema.equals("{")) {
                        state = 8;
                        break;
                    }
                    state = 15;
                    break;

                case 8:
                    if (conteudo_metodo()) {
                        state = 9;
                        break;
                    }
                    state = 15;
                    break;

                case 9:
                    if (lexema.equals("}")) {
                        return true;
                    }
                    state = 15;
                    break;

                case 15:
                    salvarArq.printf("Linha: %d - Erro na expressão aritmética.", linhaAtual);
                    return false;

            }

        }

    }

    public boolean maquinaFor() throws IOException {
        int state = 0;
        String linha = linhaAtual;

        while (true) {
            if (consumir()) {
                return false;
            }

            switch (state) {
                case 0:
                    if (lexema.equals("(")) {
                        state = 1;
                        break;
                    }
                    state = 15;
                    break;

                case 1:
                    if (atribuicao(0)) {
                        state = 2;
                        break;
                    }
                    state = 15;
                    break;

                case 2:
                    if (lexema.equals(";")) {
                        state = 3;
                        break;
                    }
                    state = 15;
                    break;

                case 3:
                    if (expLogica()) {
                        state = 4;
                        break;
                    }
                    state = 15;
                    break;

                case 4:
                    if (lexema.equals(";")) {
                        state = 5;
                        break;
                    }
                    state = 15;
                    break;

                case 5:
                    if (expAritmetica(0)) {
                        state = 6;
                        break;
                    }
                    state = 15;
                    break;

                case 6:
                    if (lexema.equals(")")) {
                        state = 7;
                        break;
                    }
                    state = 15;
                    break;

                case 7:
                    if (lexema.equals("{")) {
                        state = 8;
                        break;
                    }
                    state = 15;
                    break;

                case 8:
                    if (conteudo_metodo()) {
                        state = 9;
                        break;
                    }
                    state = 15;
                    break;

                case 9:
                    if (lexema.equals("}")) {
                        return true;
                    }
                    state = 15;
                    break;

                case 15:
                    salvarArq.printf("Linha: %s - Erro na expressão do For", linha);
                    return false;
            }
        }
    }

    public boolean expAritmetica(int n) throws IOException {
        int state = n;
        Stack pAritmetica = new Stack();

        while (true) {
            if (consumir()) {
                return false;
            }

            switch (state) {
                case 0:
                    if (lexema.equals("(")) {
                        state = 0;
                        pAritmetica.push("(");
                        break;
                    }
                    if (token.equals("Identificador")) {
                        state = 1;
                        break;
                    } else if (token.equals("Número")) {
                        state = 2;
                        break;
                    }

                    state = 15;
                    break;

                case 1:
                    if (lexema.equals("[")) {
                        state = 30;
                        break;
                    } else if (token.equals("Operador_Aritmético")) {
                        state = 4;
                        break;
                    }
                    if (lexema.equals(")")) {
                        pAritmetica.pop();
                        if (pAritmetica.isEmpty()) {
                            return true;
                        }
                    }
                    if (pAritmetica.isEmpty()) {
                        return true;
                    }

                case 30:
                    if (token.equals("Número")) {
                        state = 31;
                        break;
                    }
                    state = 15;
                    break;

                case 31:
                    if (lexema.equals("]")) {
                        state = 32;
                        break;
                    }
                    state = 15;
                    break;

                case 32:
                    if (lexema.equals("[")) {
                        state = 30;
                        break;
                    } else if (token.equals("Operador_Relacional")) {
                        state = 3;
                        break;
                    } else if (token.equals("Operador_Aritmético")) {
                        state = 4;
                        break;
                    }
                    return true;

                case 2:
                    if (token.equals("Operador_Relacional")) {
                        state = 3;
                        break;
                    } else if (token.equals("Operador_Aritmético")) {
                        state = 4;
                        break;
                    }
                    if (lexema.equals(")")) {
                        pAritmetica.pop();
                        if (pAritmetica.isEmpty()) {
                            return true;
                        }
                    }
                    if (pAritmetica.isEmpty()) {
                        return true;
                    }

                case 4:
                    if (token.equals("Identificador")) {
                        state = 1;
                        break;
                    } else if (token.equals("Número")) {
                        state = 2;
                        break;
                    }
                    if (lexema.equals("(")) {
                        pAritmetica.push("(");
                        state = 0;
                        break;
                    }

                case 15:
                    salvarArq.printf("Linha: %s - Erro na Expressão Aritmética.", linhaAtual);
                    return false;
            }

        }

    }

    public boolean expLogica() throws IOException {
        int state = 0;
        Stack pLogica = new Stack();
        String linha = linhaAtual;

        while (true) {
            if (consumir()) {
                return false;
            }

            switch (state) {
                case 0:
                    if (lexema.equals("(")) {
                        pLogica.push("(");
                        state = 0;
                        break;
                    } else if (token.equals("Identificador")) {
                        state = 1;
                        break;
                    } else if (token.equals("Número")) {
                        state = 9;
                        break;
                    } else if (boleano.contains(lexema)) {
                        state = 3;
                        break;
                    }
                    state = 15;
                    break;

                case 1:
                    if (lexema.equals("[")) {
                        state = 20;
                        break;
                    } else if (token.equals("Operador_Relacional") || token.equals("Operador_Aritmético")) {
                        state = 8;
                        break;
                    } else if (token.equals("Operador_Lógico")) {
                        state = 4;
                        break;
                    }
                    return true;

                case 4:
                    if (token.equals("Identificador")) {
                        state = 5;
                        break;
                    } else if (lexema.equals("(")) {
                        state = 0;
                        break;
                    } else if (boleano.contains(lexema)) {
                        state = 10;
                        break;
                    }
                    state = 15;
                    break;

                case 5:
                    if (lexema.equals("[")) {
                        state = 30;
                        break;
                    }
                    if (token.equals("Operador_Lógico")) {
                        state = 4;
                        break;
                    } else if (token.equals("Operador_Aritmético") || token.equals("Operador_Relacional")) {
                        state = 8;
                        break;
                    } else if (lexema.equals(")")) {
                        pLogica.pop();
                        state = 100;
                        break;
                    }

                    if (pLogica.isEmpty()) {
                        return true;
                    }
                    state = 15;
                    break;

                case 9:
                    if (token.equals("Operador_Aritmético") || token.equals("Operador_Relacional")) {
                        state = 8;
                        break;
                    } else {
                        return true;
                    }

                case 8:
                    expRelacional(3);
                    if (token.equals("Operador_Lógico")) {
                        state = 4;
                        break;
                    }
                    if (lexema.equals(")")) {
                        pLogica.pop();
                        if (pLogica.isEmpty()) {
                            return true;
                        }
                        state = 8;
                        break;
                    }
                    if (pLogica.isEmpty()) {
                        return true;
                    }

                case 10:
                    if (token.equals("Operador_Lógico")) {
                        state = 4;
                        break;
                    }
                    return true;

                case 20:
                    if (token.equals("Número")) {
                        state = 21;
                        break;
                    }
                    state = 15;
                    break;

                case 21:
                    if (lexema.equals("]")) {
                        state = 22;
                        break;
                    }
                    state = 15;
                    break;

                case 22:
                    if (lexema.equals("[")) {
                        state = 20;
                        break;
                    } else if (token.equals("Operador_Aritmético") || token.equals("Operador_Relacional")) {
                        state = 8;
                    } else if (token.equals("Operador_Lógico")) {
                        state = 4;
                    }
                    return true;

                case 3:
                    if (token.equals("Operador_Relacional")) {
                        state = 8;
                    } else if (token.equals("Operador_Lógico")) {
                        state = 4;
                    } else if (token.equals("Operador_Aritmético")) {
                        state = 9;
                    }
                    return true;

                case 30:
                    if (token.equals("Número")) {
                        state = 31;
                        break;
                    }
                    state = 15;
                    break;

                case 31:
                    if (lexema.equals("]")) {
                        state = 32;
                        break;
                    }
                    state = 15;
                    break;

                case 32:
                    if (lexema.equals("[")) {
                        state = 30;
                        break;
                    }
                    if (token.equals("Operador_Lógico")) {
                        state = 4;
                        break;
                    } else if (token.equals("Operador_Aritmético") || token.equals("Operador_Relacional")) {
                        state = 8;
                        break;
                    } else if (lexema.equals(")")) {
                        pLogica.pop();
                        state = 100;
                        break;
                    }

                    if (pLogica.isEmpty()) {
                        return true;
                    }
                    state = 15;
                    break;

                case 15:
                    salvarArq.printf("Linha: %s - Expressão lógica errada", linhaAtual);
                    if (lexema.equals(";") || lexema.equals("{") || lexema.equals("}")) {
                        return false;
                    } else {
                        state = 16;
                        break;
                    }

                case 16:
                    if (lexema.equals(";") || lexema.equals("{") || lexema.equals("}")) {
                        return false;
                    }
                    state = 16;
                    break;

                case 100:
                    if (lexema.equals(")")) {
                        pLogica.pop();
                        if (pLogica.isEmpty()) {
                            return true;
                        }
                        state = 100;
                        break;
                    } else if (pLogica.isEmpty()) {
                        return true;
                    }
            }
        }

    }

    public boolean expRelacional(int n) throws IOException {
        int state = n;
        Stack pilhaRelacional = new Stack();

        while (true) {
            if (consumir()) {
                return false;
            }

            switch (state) {
                case 0:
                    if (lexema.equals("(")) {
                        state = 0;
                        pilhaRelacional.push("(");
                        break;
                    }
                    if (token.equals("Identificador")) {
                        state = 1;
                        break;
                    } else if (token.equals("Número")) {
                        state = 2;
                        break;
                    }

                    state = 15;
                    break;

                case 1:
                    if (lexema.equals("[")) {
                        state = 30;
                        break;
                    } else if (token.equals("Operador_Relacional")) {
                        state = 3;
                        break;
                    } else if (token.equals("Operador_Aritmético")) {
                        state = 4;
                        break;
                    }
                    if (lexema.equals(")")) {
                        pilhaRelacional.pop();
                        if (pilhaRelacional.isEmpty()) {
                            return true;
                        }
                    }
                    if (pilhaRelacional.isEmpty()) {
                        return true;
                    }

                case 30:
                    if (token.equals("Número")) {
                        state = 31;
                        break;
                    }
                    state = 15;
                    break;

                case 31:
                    if (lexema.equals("]")) {
                        state = 32;
                        break;
                    }
                    state = 15;
                    break;

                case 32:
                    if (lexema.equals("[")) {
                        state = 30;
                        break;
                    } else if (token.equals("Operador_Relacional")) {
                        state = 3;
                        break;
                    } else if (token.equals("Operador_Aritmético")) {
                        state = 4;
                        break;
                    }
                    return true;

                case 2:
                    if (token.equals("Operador_Relacional")) {
                        state = 3;
                        break;
                    } else if (token.equals("Operador_Aritmético")) {
                        state = 4;
                        break;
                    }
                    if (lexema.equals(")")) {
                        pilhaRelacional.pop();
                        if (pilhaRelacional.isEmpty()) {
                            return true;
                        }
                    }
                    if (pilhaRelacional.isEmpty()) {
                        return true;
                    }

                case 3:
                    if (token.equals("(")) {
                        pilhaRelacional.push("(");
                        state = 0;
                        break;
                    } else if (token.equals("Número")) {
                        state = 2;
                        break;
                    }
                    state = 15;
                    break;

                case 4:
                    expAritmetica(4);
                    if (token.equals("Operador_Relacional")) {
                        state = 3;
                        break;
                    } else if (token.equals("Operador_Aritmético")) {
                        state = 4;
                        break;
                    } else if (lexema.equals("(")) {
                        pilhaRelacional.push("(");
                        state = 0;
                        break;
                    }
                    if (lexema.equals(")")) {
                        pilhaRelacional.pop();
                        if (pilhaRelacional.isEmpty()) {
                            return true;
                        }
                    }
                    if (pilhaRelacional.isEmpty()) {
                        return true;
                    }

                case 15:
                    salvarArq.printf("Linha: %s - Erro na Expressão Regular.", linhaAtual);
                    return false;
            }

        }
    }

    public boolean chamadaMetodo(int n) throws IOException {
        int state = n;
        String linha = linhaAtual;

        while (true) {
            if (consumir()) {
                return false;
            }

            switch (state) {
                case 0:
                    if (token.equals("Identificador")) {
                        state = 1;
                        break;
                    }
                    state = 15;
                    break;

                case 1:
                    if (lexema.equals("[")) {
                        state = 30;
                        break;
                    } else if (lexema.equals(":")) {
                        state = 2;
                        break;
                    }
                    state = 15;
                    break;

                case 2:
                    if (lexema.equals("(")) {
                        state = 3;
                        break;
                    }
                    state = 15;
                    break;

                case 3:
                    if (token.equals("Identificador")) {
                        state = 4;
                        break;
                    }
                    state = 15;
                    break;

                case 4:
                    if (lexema.equals(",")) {
                        state = 3;
                        break;
                    } else if (lexema.equals(")")) {
                        return true;
                    }
                    state = 15;
                    break;

                case 15:
                    salvarArq.printf("Linha: %s - Erro na Chamada do Método.%n", linha);

                case 30:
                    if (token.equals("Número")) {
                        state = 31;
                        break;
                    }
                    state = 15;
                    break;

                case 31:
                    if (lexema.equals("]")) {
                        state = 32;
                        break;
                    }
                    state = 15;
                    break;

                case 32:
                    if (lexema.equals("[")) {
                        state = 31;
                        break;
                    }
            }
        }
    }

}