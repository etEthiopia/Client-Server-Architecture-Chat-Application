package improvedchatapplication;
import java.io.*;
import java.net.*; 
import java.util.*; 
import javax.swing.*; 
import java.awt.*; 
import java.awt.event.*;

public class client implements ActionListener{
    JTextArea incoming;    
    JTextField outgoing,username;   
     JButton userButton;
    BufferedReader reader;    
    PrintWriter writer;   
    Socket sock;
    JFrame frame;
    public static void main(String[] args) {       
       client client = new client();
           client.go();    }
    public void go() {
        frame = new JFrame();
        JPanel mainPanel = new JPanel();               
        incoming = new JTextArea(15,50);  
        username = new JTextField(20);
        incoming.setLineWrap(true);      
        incoming.setWrapStyleWord(true);      
        incoming.setEditable(false);         
        JScrollPane qScroller = new JScrollPane(incoming);   
        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);    
        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); 
        outgoing = new JTextField(20);   
        JButton sendButton = new JButton("Send");
       userButton = new JButton("add user");
        userButton.addActionListener(this);
        sendButton.addActionListener(new SendButtonListener());  
        mainPanel.add(qScroller);     
        mainPanel.add(outgoing);  
        mainPanel.add(sendButton);
        mainPanel.add(username);
        mainPanel.add(userButton);

        
        setUpNetworking();
        Thread readerThread = new Thread(new IncomingReader());    
        readerThread.start();              
        frame.getContentPane().add(BorderLayout.CENTER, mainPanel); 
        frame.setSize(800,1200);      
        frame.setVisible(true); 
}
     private void setUpNetworking() {  
        try {           
            sock = new Socket("127.0.0.1", 5000);
            
                 
        InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());   
        reader = new BufferedReader(streamReader);    
        writer = new PrintWriter(sock.getOutputStream());     
        System.out.println("networking estabished");}
      catch(IOException ex) {  
    ex.printStackTrace();
    
}     
}  

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==userButton){
           frame.setTitle(username.getText());
       }
    }
 
   public class SendButtonListener implements ActionListener { 
       public void actionPerformed(ActionEvent ev) {
           
           try {            
               writer.println(outgoing.getText());
               writer.flush();
       }
        catch(Exception ex) {
            ex.printStackTrace();
        }       
                
       outgoing.setText("");      
       outgoing.requestFocus();   
   }    
}   
  public class IncomingReader implements Runnable {
      public void run() {        
          String message;
          try {
             while ((message = reader.readLine()) != null) {     
                 
                 System.out.println("Read " + message);
                incoming.append(username.getText() + ":"+ message + "\n");            
             }         
          } 
          catch(Exception ex) {
              ex.printStackTrace();
          }
      }      
  }   
}     


