/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package updserver;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author fabio.iantas
 */
public class FrameServer extends javax.swing.JFrame {

    ArrayList listareport = new ArrayList();
    ArrayList reportLista = new ArrayList();
    
    public FrameServer() {
        initComponents();
        TextStatus.setBackground(Color.red);
        TextStatus.setText("Stoped");
    }

    public JTable getTableList() {
        return TableList;
    }
    public void AddList(String linha){
        listareport.add(linha);
    }
    public void SetDebug(String msg){
        TextDebug.setText(TextDebug.getText()+"\n"+msg);
    }
    public void CleanTable(){
        DefaultTableModel model = (DefaultTableModel)TableList.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
        }
        TableList.setModel(model);
    }
    public void AddRow(String ip, Integer port,String user,String passw){
        String ipa = ip.substring(1, ip.length());
        ((DefaultTableModel)TableList.getModel()).addRow(new String[]{ipa,port.toString(),user});  
    }
    
    public void ReportAll(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Date hora = Calendar.getInstance().getTime(); // Ou qualquer outra forma que tem
        String dataFormatada = sdf.format(hora);
        for (int i = 0; i < TableReport.getRowCount(); i++) {
            if (null == TableReport.getValueAt(i, 3))   
                TableReport.setValueAt(dataFormatada, i, 3);
        }
    }
    public void UpdateRowReport(String usuario){
        int controle = 0;
        for (int i = 0; i < TableReport.getRowCount(); i++) {
            if(TableReport.getValueAt(i, 3) != null || !usuario.equals(TableReport.getValueAt(i, 0)) ){
            } else {
                controle++;
                int contador = Integer.parseInt(TableReport.getValueAt(i, 4).toString()) + 1;
                ((DefaultTableModel)TableReport.getModel()).setValueAt(contador, i, 4);
            }
        }
    }
    public void UpdateRowReportLogoff(String usuario){
         SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
         Date hora = Calendar.getInstance().getTime(); // Ou qualquer outra forma que tem
         String dataFormatada = sdf.format(hora);
        for (int i = 0; i < TableReport.getRowCount(); i++) {
            if(usuario.equals(TableReport.getValueAt(i, 0)) && TableReport.getValueAt(i, 3) == null){
                ((DefaultTableModel)TableReport.getModel()).setValueAt(dataFormatada, i, 3);
            }
        }
    }
    public void AddRowReport(String user, String data,String h_inicio,String h_final,String cont){
        ((DefaultTableModel)TableReport.getModel()).addRow(new String[]{user,data,h_inicio,h_final,cont});
    }
    
    public void RemoveRow(String user,String ip, Integer port){
        int row;
        String portstring = port.toString();
        DefaultTableModel dtm = (DefaultTableModel)TableList.getModel();
        for (int i = 0; i < TableList.getRowCount(); i++) {
            if(ip.equals(TableList.getValueAt(i, 0).toString())&& port == Integer.parseInt((String) TableList.getValueAt(i, 1))){
                dtm.removeRow(i);
                TableList.setModel(dtm);
                break;
            }
        }
    }
    
    public boolean UserValidate(String user, String pwd){
        boolean validate = false;
        int cont = 0;
        for (int i = 0; i < TableLogin.getRowCount(); i++) {
            if(user.equals(TableLogin.getValueAt(i, 0).toString()) && pwd.equals(TableLogin.getValueAt(i, 1).toString())){
                cont++;
            }
        }
        if(cont == 1)
            validate = true;
        else 
            validate = false;
        return validate;
    }
    public boolean UserOnline(String ip, Integer port){
        int contador = 0;
        for (int i = 0; i < TableList.getRowCount(); i++) {
            if(ip.substring(1).equals(TableList.getValueAt(i, 0).toString()) && 
               port.toString().equals(TableList.getValueAt(i, 1).toString())){
               contador++;
            }
        }
        if (contador > 0)
            return true;
        else
            return false;
    }
    public String ReturnUser(String user){
        int contador = 0;
        String usuario = null;
        for (int i = 0; i < TableList.getRowCount(); i++) {
            if(user.equals(TableList.getValueAt(i, 2))){
                contador++;
                usuario =  TableList.getValueAt(i, 2).toString();
            }
        }
        if (contador == 0)
            return usuario;
        else 
            return "";
    }
    public String ReturnUser(String ip, Integer port){
        int contador   = 0;
        String porta   = port.toString();
        String usuario = null;
        String ip0 = ip.substring(1);
        for (int i = 0; i < TableList.getRowCount(); i++) {
            if(ip0.equals(TableList.getValueAt(i, 0)) && porta.equals(TableList.getValueAt(i, 1))){
                contador++;
                usuario =  TableList.getValueAt(i, 2).toString();
            }
        }
        return usuario;
    }
    public String ReturnUser2(String ip, Integer port){
        int contador   = 0;
        String porta   = port.toString();
        String usuario = "";
        String ip0 = ip.substring(1);
        for (int i = 0; i < TableList.getRowCount(); i++) {
            if(ip0.equals(TableList.getValueAt(i, 0)) && porta.equals(TableList.getValueAt(i, 1))){
                contador++;
                usuario =  TableList.getValueAt(i, 2).toString();
            }
        }
        return usuario;
    }
    public String GetUser(){
        Object[][] otable = new Object[TableList.getRowCount()][TableList.getColumnCount()];
        DefaultTableModel model = (DefaultTableModel)TableList.getModel();
        String concat = "";
        for(int i = 0; i < TableList.getRowCount(); i++){
            for(int j = 0; j < TableList.getColumnCount(); j++){
                    otable[i][j]= TableList.getValueAt(i,j);
                        concat = concat+otable[i][j].toString().trim()+"#";
            }
        }
        this.SetDebug(concat);
        return "2#"+concat;
    }
    
    public Object[][] Gettable(){
        Object[][] otable = new Object[TableList.getRowCount()][TableList.getColumnCount()];
        for(int i = 0; i < TableList.getRowCount(); i++){
            for(int j = 0; j < TableList.getColumnCount(); j++){
                otable[i][j]= TableList.getValueAt(i,j);
            }
        }
        return otable;
    }
    public Boolean Login(String usuario, String senha){
        int x = 0; 
        for (int i = 0; i < TableLogin.getRowCount(); i++) {
            if(usuario.equals(TableLogin.getValueAt(i, 0).toString()) && senha.equals(TableLogin.getValueAt(i, 1).toString()))
                x++;
        }
        if (x > 0)
            return true;
        else
            return false;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        TextPort = new javax.swing.JTextField();
        BtStart = new javax.swing.JButton();
        BtStop = new javax.swing.JButton();
        TextStatus = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        TableLogin = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        TextUserName = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        TextPassword = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        TextDebug = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        TableList = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        TableReport = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 638, Short.MAX_VALUE)
        );

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel2.setText("SERVER");

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Servidor", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        jLabel1.setText("Port:");

        TextPort.setText("12345");

        BtStart.setText("Start");
        BtStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtStartActionPerformed(evt);
            }
        });

        BtStop.setText("Stop");

        TextStatus.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        TextStatus.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        TextStatus.setBorder(null);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(TextStatus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextPort, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(BtStart)
                .addGap(18, 18, 18)
                .addComponent(BtStop)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(BtStop)
                    .addComponent(BtStart)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(TextStatus, javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(TextPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))))
                .addGap(0, 6, Short.MAX_VALUE))
        );

        jLabel3.setText("Debug:");

        jButton2.setText("Desconectar todos");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        TableLogin.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "User", "Senha"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane3.setViewportView(TableLogin);
        if (TableLogin.getColumnModel().getColumnCount() > 0) {
            TableLogin.getColumnModel().getColumn(0).setResizable(false);
            TableLogin.getColumnModel().getColumn(1).setResizable(false);
        }

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setText("Relatório");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cadastrar Usuário", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        jLabel4.setText("User");

        jLabel5.setText("Password");

        jButton1.setText("Cadastrar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 3, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TextUserName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TextPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)))
        );

        TextDebug.setColumns(20);
        TextDebug.setRows(5);
        jScrollPane1.setViewportView(TextDebug);

        TableList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "IP", "Port", "User"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(TableList);
        if (TableList.getColumnModel().getColumnCount() > 0) {
            TableList.getColumnModel().getColumn(0).setResizable(false);
            TableList.getColumnModel().getColumn(1).setResizable(false);
            TableList.getColumnModel().getColumn(2).setResizable(false);
        }

        TableReport.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nome", "Data", "Hora Início", "Hora Final", "E-mails Resp."
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TableReport.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableReportMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(TableReport);
        if (TableReport.getColumnModel().getColumnCount() > 0) {
            TableReport.getColumnModel().getColumn(0).setResizable(false);
            TableReport.getColumnModel().getColumn(1).setResizable(false);
            TableReport.getColumnModel().getColumn(2).setResizable(false);
            TableReport.getColumnModel().getColumn(3).setResizable(false);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 418, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(4, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(4, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jButton2))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtStartActionPerformed
        ServerConnect s = new ServerConnect(Integer.parseInt(TextPort.getText()),this);
        TextStatus.setBackground(Color.green);
        TextStatus.setText("Started");
        s.start();
    }//GEN-LAST:event_BtStartActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        DefaultTableModel modelo = (DefaultTableModel) TableLogin.getModel();
        int x = 0;
        if("".equals(TextUserName.getText()) || "".equals(TextPassword.getText())){
            JOptionPane.showMessageDialog(null, "Informe Usuário e Senha");
        }
        else{
            for (int i = 0; i < TableLogin.getRowCount(); i++) {
                if (TextUserName.getText().equals(TableLogin.getValueAt(i, 0).toString()))
                    x++;
            }
            if (x == 0){
                ((DefaultTableModel)TableLogin.getModel()).addRow(new String[]{TextUserName.getText(),TextPassword.getText()});
                TextUserName.setText("");
                TextPassword.setText("");
            }    
            else
                JOptionPane.showMessageDialog(null, "Usuário já cadastrado.");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        DatagramSocket aSocket;
        try {
            aSocket = new DatagramSocket();
            Broadcast l = new Broadcast();
            l.SendBroadcast("5#", aSocket, null, Gettable(), this);
            ReportAll();
            TxtFile txt = new TxtFile(listareport,null);
            txt.Gravar();
            String lista="";
          
            for (int i = 0; i < TableReport.getRowCount(); i++) {
                lista = "";
                for (int j = 0; j < TableReport.getColumnCount(); j++) {
                    lista = lista+TableReport.getValueAt(i, j).toString()+";";
                }
                lista = lista+"\n";
                reportLista.add(lista);
            }
            TxtFile txt2 = new TxtFile(reportLista,null);
            txt2.Gravar();
        } catch (SocketException ex) {
            Logger.getLogger(FrameServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void TableReportMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableReportMouseClicked
        TxtFile txt = new TxtFile(listareport,TableReport.getValueAt(TableReport.getSelectedRow(), 0).toString());
        txt.Gravar();
    }//GEN-LAST:event_TableReportMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrameServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrameServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrameServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrameServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrameServer().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtStart;
    private javax.swing.JButton BtStop;
    private javax.swing.JTable TableList;
    private javax.swing.JTable TableLogin;
    private javax.swing.JTable TableReport;
    private javax.swing.JTextArea TextDebug;
    private javax.swing.JTextField TextPassword;
    private javax.swing.JTextField TextPort;
    private javax.swing.JTextField TextStatus;
    private javax.swing.JTextField TextUserName;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    // End of variables declaration//GEN-END:variables
}
