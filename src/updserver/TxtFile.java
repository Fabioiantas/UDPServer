/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package updserver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;

/**
 *
 * @author fabio.iantas
 */
public class TxtFile {
    String user;
    FileWriter arquivo;
    ArrayList lista = new ArrayList();
    String caminho;
    
    public TxtFile(ArrayList lista,String user){
        this.lista = lista;
        this.caminho = caminho;
        this.user = user;
    }
    public void Gravar(){
        JFileChooser file = new JFileChooser();
        file.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int t = file.showSaveDialog(null);
        if (t == 1) {
            System.out.println("teste");
        } else {
            File arquivo = file.getSelectedFile();
            caminho = (arquivo.getPath());
        }
        System.out.println("caminho "+caminho);
        try {  
            arquivo = new FileWriter(new File(caminho));  
            for (int i = 0; i < lista.size(); i++) {
                System.out.println("lista :"+lista.get(i));
                if(user == null)
                    arquivo.write((String) lista.get(i)+"\n");
                else{
                    System.out.println("listt "+lista.get(i).toString().substring(0, lista.get(i).toString().indexOf(";")));
                    if(user.equals(lista.get(i).toString().substring(0, lista.get(i).toString().indexOf(";"))))
                        arquivo.write((String) lista.get(i)+"\n");
                }
                    
            }
              
            arquivo.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }
}
/*
//Declaração
    	List lista = new ArrayList();
    	String[] listaStr;
    	//Adicionando
    	lista.add("campo 1");
    	lista.add("campo 2");
    	lista.add("campo 3");
    	//Tamanho do ArrayList. Retorno: 3
    	System.out.println("--- Imprimindo Tamanho do ArrayList ---");
    	System.out.println(lista.size());
    	//Imprimindo todo o ArrayList
    	System.out.println("--- Imprimindo ArrayList ---");
    	for (int i = 0; i < lista.size(); i++) {
    		System.out.println(lista.get(i));
    	}
    	//Convertendo de ArrayList para Array(Matriz)
    	listaStr = (String[]) lista.toArray (new String[0]);
    	//Imprimindo toda a matriz
    	System.out.println("--- Imprimindo Array ---");
    	for (int i = 0; i < listaStr.length; i++) {
    		System.out.println(listaStr[i]);

*/