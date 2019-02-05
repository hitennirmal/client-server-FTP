import java.io.*;
import java.net.*;

//import org.graalvm.compiler.hotspot.phases.OnStackReplacementPhase_OptionDescriptors;
public class Testclient
{
  public static void main(String[] args) throws Exception
  {
     
                               // reading from keyboard (keyRead object)
     BufferedReader keyRead = new BufferedReader(new InputStreamReader(System.in));
                              // sending to client (pwrite object)
     System.out.println("Enter Port number:");
     int port=Integer.parseInt(keyRead.readLine());
     Socket sock = new Socket(args[0],port);
     OutputStream ostream = sock.getOutputStream(); 
     PrintWriter pwrite = new PrintWriter(ostream, true);

                              // receiving from server ( receiveRead  object)
     InputStream istream = sock.getInputStream();
     BufferedReader receiveRead = new BufferedReader(new InputStreamReader(istream));
     
     

     System.out.println("Connected to the server at port :"+ port);

     String sendMessage, receiveMessage, c, word="", rest; 
     sendMessage="" ;            
     while(true)
     {
        sendMessage = keyRead.readLine(); // keyboard reading
        System.out.println("Message Sending to server...."+ sendMessage);
            int index = sendMessage.indexOf(' ');
            if (index > -1) // Check if there is more than one word.
            { 
            word=sendMessage.substring(0, index);// Extract first word.
            rest= sendMessage.substring(index+1);// rest word
            } else 
            {
            word=sendMessage; // Text is the first word itself.
            rest="";
            }

              // sending to server
        if(word.equals("pwd") || word.equals("ls") || word.equals("cd") || word.equals("cd..") || word.equals("delete") || word.equals("mkdir"))
        {   pwrite.println(sendMessage); 
            if((receiveMessage = receiveRead.readLine()) != null) //receive from server
            {
                System.out.println(receiveMessage); // displaying at DOS prompt
            }  
            pwrite.flush();
        }
        else if (word.equals("get"))
        {
            pwrite.println(sendMessage); 
            int bytesRead;  
            int current = 0;  
            //receving data from server
            DataInputStream clientData = new DataInputStream(istream);
            String fileName = clientData.readUTF();
            String currentdir_temp=System.getProperty("user.dir");  
            String mkdir_temp=currentdir_temp+"/"+fileName;
            File statText = new File(mkdir_temp);    
            OutputStream output = new FileOutputStream(statText);     
            long size = clientData.readLong();     
            byte[] buffer = new byte[1024];     
            while (size > 0 && (bytesRead = clientData.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1)     
            {     
                output.write(buffer, 0, bytesRead);     
                size -= bytesRead;     
            }  
               
            // Closing the FileOutputStream handle
            //in.close();
            //clientData.close();
            output.flush();
            output.close();  
            System.out.println("File Transfered");
            pwrite.println("Transfer Complete"); 
            receiveMessage = receiveRead.readLine();
            if(receiveMessage!=null)
            {
                System.out.print("");
            }            
            pwrite.flush();
        }
        else if (word.equals("put"))
        {
            pwrite.println(sendMessage); 
            String currentdir_temp=System.getProperty("user.dir");  
            File myFile = new File(currentdir_temp+"/"+rest);  
            
            byte[] mybytearray = new byte[(int) myFile.length()];  
            try{
            if(myFile.exists())
                {
                    FileInputStream fis = new FileInputStream(myFile);  
                    BufferedInputStream bis = new BufferedInputStream(fis);  
                    //bis.read(mybytearray, 0, mybytearray.length);  
                    DataInputStream dis = new DataInputStream(bis);     
                    dis.readFully(mybytearray, 0, mybytearray.length);  
                    
                    
                    //Sending file name and file size to the server  
                    DataOutputStream dos = new DataOutputStream(ostream);     
                    dos.writeUTF(myFile.getName());     
                    dos.writeLong(mybytearray.length);     
                    dos.write(mybytearray, 0, mybytearray.length);     
                    dos.flush();  
                    
                    //Sending file data to the server  
                    ostream.write(mybytearray, 0, mybytearray.length); 

                    
                    ostream.flush();  
                    dos.flush();
                    bis.close();
                    fis.close();
                    dis.close();
                    pwrite.flush();
                    //os.close();
                    //dos.close(); 
                }
            else
            {
                System.out.println("File not Found");
            }
            }
            catch(Exception e)
            {
                System.out.println(e);
            }
            if((receiveMessage = receiveRead.readLine()) != null) //receive from server
            {   
                System.out.println(receiveMessage); // displaying at DOS prompt
            }  
            pwrite.println("Hi thanks");
            pwrite.flush();
            ostream.flush();
        }
        else if(word.equals("quit"))
        {
            pwrite.println(sendMessage); 
            break;
        }
        else
        {
            System.out.println("Wrong Command"); 
        }
         
                
      }   
      sock.close();            
    }                    
}        