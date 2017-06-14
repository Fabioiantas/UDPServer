/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package updserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;

/**
 *
 * @author fabio.iantas
 */
public class ServerConnect extends Thread {
    Integer port;
    Integer portToSend = null;
    DatagramSocket aSocket;
    DatagramPacket request;
    DatagramPacket reply;
    JTextArea textArea;
    FrameServer frame;
    
    public ServerConnect(Integer port,FrameServer frame){
        this.port  = port;
        this.frame = frame;
        try {
            aSocket = new DatagramSocket(port);
            frame.SetDebug("Server Started, Port: "+port);
        } catch (SocketException ex) {
            System.out.println("ERRO: "+ex.getMessage());
        }
    }
    public void run(){
        byte[] buffer = new byte[1000];
        byte[] out = new byte[1000];
        boolean bool;
        String clientmsg;
        Integer option;
        String user = null;
        String lista;
        String ip;
        InetAddress aHost = null;
        int cont = 0;
        String ipToSend;
        String erro = null;
        //InetAddress aHost = InetAddress.getByName(otable[i][0].toString());
        try {   
            while(true){
                request = new DatagramPacket(buffer,buffer.length);
                aSocket.receive(request);
                clientmsg = new String(buffer, 0, request.getLength());
                System.out.println("Client message:"+clientmsg);
                option = Integer.parseInt(clientmsg.substring(0,clientmsg.indexOf("#")).toString());
                System.out.println("2");
                if(option != 3){
                    user   = clientmsg.substring(clientmsg.lastIndexOf("#")+1,clientmsg.length()).trim();
                }
                  //InetAddress aHost = InetAddress.getByName(otable[i][0].toString());
                switch(option){
                    case 1:
                        String[] arrayMsg = clientmsg.trim().substring(2).split("#");
                        frame.AddRow(request.getAddress().toString(), request.getPort(),user);
                        out = null;
                        DataClient clientl = new DataClient();
                        clientl.setIp(request.getAddress().toString());
                        clientl.setPort(request.getPort());
                        clientl.setUsername(user);
                        clientl.setMsg(clientmsg);
                        Broadcast l = new Broadcast();
                        l.SendBroadcast(frame.GetUser(),aSocket,clientl,frame.Gettable());
                        break;
                    case 3:
                        String[] sendTo = clientmsg.trim().substring(2).split("#");
                        for (int i = 0; i < sendTo.length; i++) {
                            cont++;
                        }
                        if(cont < 3){
                            out = null;
                            aHost = null;
                            ipToSend = null;
                            erro = "INVALID PROTOCOL";
                            break;
                        }else{
                            if ("999.999.999.999".equals(sendTo[0]) && "99999".equals(sendTo[1])){
                                aHost = null;
                                out = null;
                                portToSend = null;
                                Broadcast sender =  new Broadcast();
                                DataClient datac = new DataClient();
                                datac.setIp(request.getAddress().toString());
                                datac.setPort(request.getPort());
                                sender.SendBroadcast("4#"+request.getAddress().toString().substring(1)+"#"+request.getPort()+"#"+sendTo[2], aSocket, datac, frame.Gettable());
                                break;
                            }
                            else{
                                aHost = InetAddress.getByName(sendTo[0].toString());
                                portToSend = Integer.parseInt(sendTo[1].toString());
                                Integer portReceive = request.getPort();
                                String message = sendTo[2];
                                ipToSend = request.getAddress().toString().substring(1);
                                String messageToSend = "4#"+request.getAddress().toString().substring(1)+"#"+portReceive.toString()+"#"+message;
                                out = messageToSend.getBytes();
                                System.out.println("sendTo:"+messageToSend);
                                break;
                            }
                        }
                    case 5:
                        frame.RemoveRow(user, request.getAddress().toString(), request.getPort());
                        DataClient client = new DataClient();
                        ip = request.getAddress().toString().substring(1,request.getAddress().toString().length());
                        client.setIp(ip);
                        client.setPort(request.getPort());
                        client.setMsg(clientmsg);
                        Broadcast lout = new Broadcast();
                        lout.SendBroadcast(frame.GetUser(),aSocket,client,frame.Gettable());
                        out = null;
                        break;
                    default:
                        out = null;
                        System.out.println("protocolo inválido: =>"+clientmsg);
                        break;
                }
                if(out != null && aHost != null && portToSend != null){
                    reply = new DatagramPacket(out, out.length, aHost, portToSend);
                    aSocket.send(reply);
                    System.out.println("enviado para:"+aHost.toString()+"<>"+portToSend);
                }else if(erro != null){
                    System.out.println(erro);
                }else
                    System.out.println("Not Send");
            }
        }catch(SocketException e){
            System.out.println("ERRO: "+e.getMessage());
        }catch (IOException ex) {
            System.out.println("ERRO: "+ex.getMessage());
        }
    }
}
