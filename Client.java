import java.io.*;
import java.net.*;
public class Client
{
  public static void main(String[] args) throws Exception
  {
     Socket sock = new Socket("127.0.0.1", 25000);
                               // reading from keyboard (keyRead object)
     BufferedReader keyRead = new BufferedReader(new InputStreamReader(System.in));
                              // sending to client (pwrite object)
     OutputStream ostream = sock.getOutputStream(); 
     PrintWriter pwrite = new PrintWriter(ostream, true);

                              // receiving from server ( receiveRead  object)
     InputStream istream = sock.getInputStream();
     BufferedReader receiveRead = new BufferedReader(new InputStreamReader(istream));

     System.out.println("Connected");

     String sendMessage, receiveMessage, c, word="", rest; 
     sendMessage="" ;            
     while(true)
     {
        sendMessage = keyRead.readLine(); // keyboard reading
            int index = sendMessage.indexOf(' ');
            if (index > -1) // Check if there is more than one word.
            { 
            word=sendMessage.substring(0, index);// Extract first word.
            rest= sendMessage.substring(index);// rest word
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
            if((receiveMessage = receiveRead.readLine()) != null) //receive from server
            {
                System.out.println(receiveMessage); // displaying at DOS prompt
            }  
            pwrite.flush();
        }
        else if (word.equals("put"))
        {
            pwrite.println(sendMessage); 
            File myFile = new File(rest);  
            if(myFile.exists())
            {
            byte[] mybytearray = new byte[(int) myFile.length()];  
            try{
            FileInputStream fis = new FileInputStream(myFile);  
            BufferedInputStream bis = new BufferedInputStream(fis);  
            //bis.read(mybytearray, 0, mybytearray.length);  
            
            DataInputStream dis = new DataInputStream(bis);     
            dis.readFully(mybytearray, 0, mybytearray.length);  
            
            OutputStream os = sock.getOutputStream();  
            
            //Sending file name and file size to the server  
            DataOutputStream dos = new DataOutputStream(os);     
            dos.writeUTF(myFile.getName());     
            dos.writeLong(mybytearray.length);     
            dos.write(mybytearray, 0, mybytearray.length);     
            dos.flush();  
            
            //Sending file data to the server  
            os.write(mybytearray, 0, mybytearray.length);  
            os.flush();  
            os.close();
            dos.close(); 
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
            pwrite.flush();
        }
        else if(word.equals("quit"))
        {
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