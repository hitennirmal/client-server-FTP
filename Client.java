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
        else if (word.equals("put"))
        {
            pwrite.println(sendMessage); 
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