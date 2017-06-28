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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import jdk.nashorn.internal.objects.NativeString;

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
    String cliente[][] = new String[500][5];
    
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
        int cont1 = 0;
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
                String[] msg = clientmsg.trim().split("#");
                switch(option){
                    case 1:
                        String[] arrayMsg = clientmsg.trim().split("#");
                        for (int i = 0; i < arrayMsg.length; i++) {
                            cont1++;
                        }
                        System.out.println("cont > "+cont1);
                        if(cont1 != 3){
                            cont1 = 0;
                            System.out.println("Protocolo Inválido / Client msg: "+clientmsg);
                            out = null;
                            break;        
                        }
                        cont1 = 0;
                        if (!frame.UserValidate(arrayMsg[1], arrayMsg[2])){
                            out = "e1#".getBytes();
                            String ip1 = request.getAddress().toString().substring(1);
                            aHost = InetAddress.getByName(ip1);
                            portToSend = request.getPort();
                            break;
                        }
                        else{
                            if (frame.ReturnUser(user) == null){
                                frame.AddRow(request.getAddress().toString(), request.getPort(),arrayMsg[1],arrayMsg[2]);
                                Date data = new Date();
                                String dataStr = java.text.DateFormat.getDateInstance(DateFormat.MEDIUM).format(data);
                                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                                Date hora = Calendar.getInstance().getTime(); // Ou qualquer outra forma que tem
                                String dataFormatada = sdf.format(hora);
                                frame.AddRowReport(arrayMsg[1], dataStr, dataFormatada, null, "0");
                                out = null;
                            }else{
                                out = "e2#".getBytes();
                                String ip1 = request.getAddress().toString().substring(1);
                                aHost = InetAddress.getByName(ip1);
                                portToSend = request.getPort();
                                break;
                            }    
                        }
                        DataClient clientl = new DataClient();
                        clientl.setIp(request.getAddress().toString());
                        clientl.setPort(request.getPort());
                        clientl.setUsername(user);
                        clientl.setMsg(clientmsg);
                        Broadcast l = new Broadcast();
                        l.SendBroadcast(frame.GetUser(),aSocket,clientl,frame.Gettable(),frame);
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
                                sender.SendBroadcast("4#"+request.getAddress().toString().substring(1)+"#"+request.getPort()+"#"+sendTo[2], aSocket, datac, frame.Gettable(),frame);
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
                        frame.UpdateRowReportLogoff(frame.ReturnUser(request.getAddress().toString(), request.getPort()));
                        frame.RemoveRow(user, request.getAddress().toString().substring(1), request.getPort());
                        DataClient client = new DataClient();
                        ip = request.getAddress().toString().substring(1,request.getAddress().toString().length());
                        client.setIp(ip);
                        client.setPort(request.getPort());
                        client.setMsg(clientmsg);
                        Broadcast lout = new Broadcast();
                        lout.SendBroadcast(frame.GetUser(),aSocket,client,frame.Gettable(),frame);
                        out = "5#".getBytes();
                        aHost = InetAddress.getByName(request.getAddress().toString().substring(1));
                        portToSend = request.getPort();
                        break;
                    case 6:
                        System.out.println("userport6 "+request.getAddress().toString()+" = "+request.getPort());
                        String[] emailmsg = clientmsg.trim().split("#");
                        frame.UpdateRowReport( frame.ReturnUser(request.getAddress().toString(), request.getPort()));
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
