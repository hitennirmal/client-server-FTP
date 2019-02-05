import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.NoSuchFileException;
import java.nio.file.DirectoryNotEmptyException;
import java.util.concurrent.TimeUnit;
public class Testserver
{
  public static void main(String[] args) throws Exception
  {   
      BufferedReader keyRead = new BufferedReader(new InputStreamReader(System.in));
      System.out.println("Enter Port number:");
      int port=Integer.parseInt(keyRead.readLine());
      ServerSocket sersock = new ServerSocket(port);
      while(true)
      {
          
          System.out.println("Server  ready at port :"+port);
          Socket sock = sersock.accept( );       
          System.out.println("Server connected to the Client")  ;                 
                                  // reading from keyboard (keyRead object)
          
                              // sending to client (pwrite object)
          OutputStream ostream = sock.getOutputStream(); 
          PrintWriter pwrite = new PrintWriter(ostream, true);
             // receiving from server ( receiveRead  object)
          InputStream istream = sock.getInputStream();
          BufferedReader receiveRead = new BufferedReader(new InputStreamReader(istream));


          String receiveMessage, sendMessage,rest,word,currentdir_temp, mkdir_temp;        
          String currentdir=System.getProperty("user.dir"); 
          boolean input = true;
        while(input)
        {
            receiveMessage = receiveRead.readLine();
            while (receiveMessage!=null)
            {
            //System.out.println("Command form Client "+receiveMessage);
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
                System.out.println("The current working directory is:" + currentdir);
                pwrite.println("The current working directory is:" + currentdir);             
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
                    pwrite.println("Directory Changed to :" +currentdir_temp );
                    currentdir=currentdir_temp;
                    System.out.println("Directory Changed to :" +currentdir_temp);
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
                pwrite.println("Directory Changed:" + currentdir);             
                pwrite.flush();
                System.out.println("Directory changed :" + currentdir);

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
                } 
                }
                else 
                {
                    pwrite.println("Failed to create directory!");
                }
             
                mkdir_temp=null;
                pwrite.flush();
            }   
            else if (receiveMessage !=null && word.equals("delete"))
            {   mkdir_temp=currentdir+"/"+rest;
                File file = new File(mkdir_temp);
                //System.out.println("Need Code for delete");
                try
                { 
                    if(file.exists())
                    {
                        Files.deleteIfExists(Paths.get(mkdir_temp)); 
                        pwrite.println("Deletion successful."); 
                        System.out.println("Deleted at"+ mkdir_temp);
                    }
                    else
                    {
                        pwrite.println("No such file found"); 
                    }
                    
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

                       
                pwrite.flush();
                mkdir_temp="null";
            }   
            else if (receiveMessage !=null && word.equals("get"))
            {    
                mkdir_temp=currentdir+"/"+rest;
                File myFile = new File(mkdir_temp);  

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
                mkdir_temp=null;
            }   
            else if (receiveMessage !=null && word.equals("put"))
            {   int bytesRead;  
                int current = 0;  
                //receving data from server
                DataInputStream clientData = new DataInputStream(istream);
                String fileName = clientData.readUTF();
                mkdir_temp=currentdir+"/"+fileName;
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
                mkdir_temp=null;
            }
            else if (receiveMessage !=null && word.equals("quit"))
            {
                System.out.println("Client-Server Connection: Closing");
                istream.close();
                ostream.close();
                sock.close();
                input = false;
                System.out.println("Coonection closed!");
                //TimeUnit.SECONDS.sleep(10);
                break;

            }

            /*else
            {
                System.out.println("Wrong command received  "+receiveMessage);
                pwrite.println("Server is saying it is worng command" +receiveMessage);             
                pwrite.flush();
            }*/
            receiveMessage=null;
          }    
        } 
      }
  } 
  
}                    