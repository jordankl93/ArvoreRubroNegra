/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arvorerubronegra;

/**
 *
 * @author Jordan-PC
 * @param <T>
 */
public class NoRubroNegra <T extends Comparable<T>> implements java.io.Serializable{
    public static final int PRETO = 0;
    public static final int VERMELHO = 1;
    
    public T chave;
    public NoRubroNegra<T> esquerdo, direito, pai;
    public int color;
 
    public NoRubroNegra() {
        esquerdo = null;
        direito = null;
        pai = null;
        color = PRETO;
    }
 
    public NoRubroNegra(T chave) {
        this();
        this.chave = chave;
    }
}
