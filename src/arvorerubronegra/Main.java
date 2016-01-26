/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arvorerubronegra;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 *
 * @author Jordan-PC
 */
public class Main {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {        
        assert (args.length == 3);
        String entrada = args[0];
        String busca = args[1];
        String saida = args[2];

        try {
            ArvoreRubroNegra<String> tree = new ArvoreRubroNegra<>();
            Scanner scannerEntrada = new Scanner(new FileReader(entrada));
            Scanner scannerBusca = new Scanner(new FileReader(busca));
            
            //Inserindo os dados na árvore
            while (scannerEntrada.hasNext()) {
                String texto = scannerEntrada.nextLine();
                tree.insert(texto);
                //System.out.println(texto);
            }
            
            //Função que transfere a árvore dentro de um arquivo para realizar a busca em HD
            tree.arvoreArquivo();           

            FileWriter arq = new FileWriter(saida);
            PrintWriter gravarArq = new PrintWriter(arq);

            while (scannerBusca.hasNext()) {
                gravarArq.println(tree.search(scannerBusca.nextLine()));
            }

            gravarArq.close();
            arq.close();
            
            scannerEntrada.close();
            scannerBusca.close();
            
            File arquivo = new File("arvore.txt");
            arquivo.createNewFile();
            arquivo.delete();

            System.out.println("Tudo ocorreu bem... a principio");
        } 
        catch (Exception e) {
            System.out.println("Algo está errado => " + e.getMessage());
        }
    }

}
