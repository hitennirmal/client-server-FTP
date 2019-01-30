import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.NoSuchFileException;
import java.nio.file.DirectoryNotEmptyException;

public class Server
{
  public static void main(String[] args) throws Exception
  {
      ServerSocket sersock = new ServerSocket(25000);
      System.out.println("Server  ready");
      Socket sock = sersock.accept( );       
      System.out.println("Connected")  ;                 
                              // reading from keyboard (keyRead object)
      BufferedReader keyRead = new BufferedReader(new InputStreamReader(System.in));
	                      // sending to client (pwrite object)
      OutputStream ostream = sock.getOutputStream(); 
      PrintWriter pwrite = new PrintWriter(ostream, true);

                              // receiving from server ( receiveRead  object)
      InputStream istream = sock.getInputStream();
      BufferedReader receiveRead = new BufferedReader(new InputStreamReader(istream));
       
      String receiveMessage, sendMessage,rest,word,currentdir_temp, mkdir_temp;        
      String currentdir=System.getProperty("user.dir");     
      while(true)
    {
        receiveMessage = receiveRead.readLine();
        while (receiveMessage!=null)
        {
        int index = receiveMessage.indexOf(' ');
            if (index > -1) // Check if there is more than one word.
            { 
            word=receiveMessage.substring(0, index);// Extract first word.
            rest= receiveMessage.substring(index+1);// rest word
            } else 
            {
            word=receiveMessage; // Text is the first word itself.
            rest="";
            }
        if(receiveMessage != null && word.equals("pwd"))  
        {
            //System.out.println("Need Code for PWD");
            pwrite.println(currentdir);             
            pwrite.flush();       
        }         
        else if(receiveMessage != null && word.equals("ls"))
        {
            //System.out.println("Need Code for ls");
            File dir = new File(currentdir);
            String[] files = dir.list();
            if (files.length == 0)
             {
                pwrite.println("The directory is empty");
                
            } else 
            {
                String str = String.join("    ", files);
                pwrite.println(str);
                }
            pwrite.flush();           
        }
        else if (receiveMessage !=null && word.equals("cd"))
        {
            //System.out.println("Need Code for cd");
            currentdir_temp=currentdir+ "/"+rest;
            if (Files.isDirectory(Paths.get(currentdir_temp))) 
            {
                pwrite.println("Directory Changed" );
                currentdir=currentdir_temp;
                System.out.println(currentdir);
            }
            else
            {
                pwrite.println("Directory does not exists");
            }
                         
            pwrite.flush();
        }
        
        else if (receiveMessage !=null && word.equals("cd.."))
        {
           // System.out.println("Need Code for cd..");
           currentdir=currentdir.substring(0,currentdir.lastIndexOf('/'));
            pwrite.println("Directory Changed");             
            pwrite.flush();
            System.out.println(currentdir);

        }
        else if (receiveMessage !=null && word.equals("mkdir"))
        {
            //System.out.println("Need Code for mkdir");
            mkdir_temp=currentdir+"/"+rest;
            File file = new File(mkdir_temp);
            if (!file.exists()) 
            {
            if (file.mkdir()) 
            {
                pwrite.println("Directory is created!");
                System.out.println("Created at "+ mkdir_temp);
            } else 
            {
                pwrite.println("Failed to create directory!");
            }
             }
            mkdir_temp=null;
            pwrite.flush();
        }   
        else if (receiveMessage !=null && word.equals("delete"))
        {   mkdir_temp=currentdir+"/"+rest;
            //System.out.println("Need Code for delete");
            try
            { 
                Files.deleteIfExists(Paths.get(mkdir_temp)); 
            } 
            catch(NoSuchFileException e) 
            { 
                pwrite.println("No such file/directory exists"); 
            } 
            catch(DirectoryNotEmptyException e) 
            { 
                pwrite.println("Directory is not empty."); 
            } 
            catch(IOException e) 
            { 
                pwrite.println("Invalid permissions."); 
            } 
              
            pwrite.println("Deletion successful.");         
            pwrite.flush();
            System.out.println("Deleted at"+ mkdir_temp);
        }   
        else if (receiveMessage !=null && word.equals("get"))
        {
            System.out.println("Need Code for get");
            pwrite.println("This will be reply from get");             
            pwrite.flush();
        }   
        else if (receiveMessage !=null && word.equals("put"))
        {
            System.out.println("Need Code for put");
            pwrite.println("This will be reply from put");             
            pwrite.flush();
        }   
        receiveMessage=null;
      }         
    }      
    }                    
}                        