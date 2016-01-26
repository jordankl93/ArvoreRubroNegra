/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arvorerubronegra;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

/**
 *
 * @author Jordan-PC
 * @param <T>
 */
public class ArvoreRubroNegra<T extends Comparable<T>> implements java.io.Serializable {

    public static final int PRETO = 0;
    public static final int VERMELHO = 1;

    private NoRubroNegra<T> nil = new NoRubroNegra<>(); //Representa as folhas
    private NoRubroNegra<T> root = nil; // Raiz inicial

    private void rotacaoEsquerda(NoRubroNegra<T> x) {
        NoRubroNegra<T> y = x.direito;
        x.direito = y.esquerdo;
        if (y.esquerdo != nil) {
            y.esquerdo.pai = x;
        }
        y.pai = x.pai;
        if (x.pai == nil) {
            root = y;
        } else {
            if (x == x.pai.esquerdo) {
                x.pai.esquerdo = y;
            } else {
                x.pai.direito = y;
            }
        }
        y.esquerdo = x;
        x.pai = y;
    }

    private void rotacaoDireita(NoRubroNegra<T> x) {
        NoRubroNegra<T> y = x.esquerdo;
        x.esquerdo = y.direito;
        if (y.direito != nil) {
            y.direito.pai = x;
        }
        y.pai = x.pai;
        if (x.pai == nil) {
            root = y;
        } else {
            if (x == x.pai.direito) {
                x.pai.direito = y;
            } else {
                x.pai.esquerdo = y;
            }
        }
        y.direito = x;
        x.pai = y;
    }

    private void insert(NoRubroNegra<T> z) {

        NoRubroNegra<T> y = nil;
        NoRubroNegra<T> x = root;

        while (x != nil) {
            y = x;
            if (z.chave.compareTo(x.chave) < 0) {
                x = x.esquerdo;
            } else {
                x = x.direito;
            }
        }
        z.pai = y;
        if (y == nil) {
            root = z;
        } else if (z.chave.compareTo(y.chave) < 0) {
            y.esquerdo = z;
        } else {
            y.direito = z;
        }

        z.esquerdo = nil;
        z.direito = nil;
        z.color = VERMELHO;
        insertFixUp(z);
    }
    
    //Função para inserir acertando caso fuja as regras de uma árvore rubro-negra
    private void insertFixUp(NoRubroNegra<T> z) {
        while (z != root && z.pai.color == VERMELHO) {
            if (z.pai == z.pai.pai.esquerdo) {
                NoRubroNegra<T> y = z.pai.pai.direito;
                if (y.color == VERMELHO) {
                    z.pai.color = PRETO;
                    y.color = PRETO;
                    z.pai.pai.color = VERMELHO;
                    z = z.pai.pai;
                } else {
                    if (z == z.pai.direito) {
                        z = z.pai;
                        rotacaoEsquerda(z);
                    }
                    z.pai.color = PRETO;
                    z.pai.pai.color = VERMELHO;
                    rotacaoDireita(z.pai.pai);
                }
            } else {
                if (z.pai == z.pai.pai.direito) {
                    NoRubroNegra<T> y = z.pai.pai.esquerdo;
                    if (y.color == VERMELHO) {
                        z.pai.color = PRETO;
                        y.color = PRETO;
                        z.pai.pai.color = VERMELHO;
                        z = z.pai.pai;
                    } else {
                        if (z == z.pai.esquerdo) {
                            z = z.pai;
                            rotacaoDireita(z);
                        }
                        z.pai.color = PRETO;
                        z.pai.pai.color = VERMELHO;
                        rotacaoEsquerda(z.pai.pai);
                    }
                }
            }
        }
        root.color = PRETO;
    }

    public void insert(T chave) {
        NoRubroNegra<T> novo = new NoRubroNegra<T>(chave);
        insert(novo);
    }
    
    //Função que busca o elemento - EM HD
    public String search(T chave) {
        int cont = 0;
        try {            
            T conteudo;
            NoRubroNegra<T> aux = new NoRubroNegra<>();
            File file = new File("arvore.txt");
            FileInputStream stream = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(stream);
            
            aux = (NoRubroNegra<T>)in.readObject(); //Primeiro obj sempre será a raiz        
            while (aux != nil) {
                if (chave.compareTo(aux.chave) < 0) {
                    cont++;
                    conteudo = aux.esquerdo.chave; //Pega o conteudo do filho esquerdo
                    aux = (NoRubroNegra<T>)in.readObject();
                    while(aux != null && aux.chave != conteudo){
                        aux = (NoRubroNegra<T>)in.readObject();      
                    }
                } else if (chave.compareTo(aux.chave) > 0) {
                    cont++;
                    conteudo = aux.direito.chave; //Pega o conteudo do filho direito
                    aux = (NoRubroNegra<T>)in.readObject();
                    while(aux != null && aux.chave != conteudo){
                        aux = (NoRubroNegra<T>)in.readObject();      
                    }
                } else if (chave.compareTo(aux.chave) == 0) {
                    in.close();
                    stream.close();
                    return String.format("%s presente %d", chave, cont); // Retorno caso encontre
                }
            }            
            in.close();
            stream.close();
        }
        catch(IOException e){}
        catch(ClassNotFoundException e){}   

        return String.format("%s ausente %d", chave, cont); //Caso não encontre nada
    }

    private NoRubroNegra<T> sucessor(NoRubroNegra<T> z) {
        NoRubroNegra<T> aux = z.direito;
        while (aux.esquerdo != nil) {
            aux = aux.esquerdo;
        }
        return aux;
    }

    //Função responsável por escrever no arquivo e de forma ordenada
    public void inOrder(NoRubroNegra<T> root, ObjectOutput out) throws IOException {
        NoRubroNegra<T> aux = root;
        if (aux == nil) {
            return;
        }
        out.writeObject(aux);
        inOrder(root.esquerdo, out);
        inOrder(root.direito, out);
    }

    //Cria um arquivo para guardar os elementos de entrada
    public void arvoreArquivo() {
        try {            
            File file = new File("arvore.txt");
            FileOutputStream fileStream = new FileOutputStream(file);
            ObjectOutput out = new ObjectOutputStream(fileStream);
            inOrder(root, out);
            out.close();
            fileStream.close();
        } catch (IOException e) {
        }
    }
}
