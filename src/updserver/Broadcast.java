/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package updserver;

import java.awt.Frame;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static jdk.nashorn.internal.objects.ArrayBufferView.buffer;

/**
 *
 * @author fabio.iantas
 */
public class Broadcast {
    DatagramPacket toSend;
    String ip;
    Integer port;
    String username;
    String sendMessage = null;
   
    public void SendBroadcast(String lista,DatagramSocket aSocket,DataClient client,Object[][] otable,FrameServer frame){
        for(int i = 0; i < otable.length; i++){
            try { 
                byte[] buffer = new byte[1000];
                buffer = lista.getBytes();
                InetAddress aHost = InetAddress.getByName(otable[i][0].toString());
                toSend = new DatagramPacket(buffer, buffer.length, aHost, Integer.parseInt(otable[i][1].toString()));
                aSocket.send(toSend); 
                if(lista.equals("5#")){
                    frame.RemoveRow(otable[0][2].toString(), otable[i][0].toString(), Integer.parseInt(otable[i][1].toString()));
                }
            }catch(SocketException ex) {
                System.out.println("ERRO Send Broadcast1: "+ex.getMessage());
            }catch(UnknownHostException e){
                System.out.println("ERRO Send Broadcast2: "+e.getMessage());
            }catch (IOException e){
                System.out.println("ERRO Send Broadcast3: "+e.getMessage());
            }
        }
    }
}   
