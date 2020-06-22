// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com

import java.io.*;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer
{
  //Class variables *************************************************

  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;

  //Constructors ****************************************************

  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port)
  {
    super(port);
  }


  //Instance methods ************************************************

  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
    String s=msg.toString();

    boolean isServerCommand = false;
  //  System.out.println(s);
    String[] arrOfStr =new String[2];
  //  System.out.println(s);
    if(s.contains("SERVER MSG> #")){
      String[] ServMessage =new String[2];
      ServMessage = s.split("#", 2);
      String CleanMessage = "#"+ServMessage[1];
      String command = "";
    //  System.out.println("inside server messages-" + CleanMessage + "-");
      if(CleanMessage.indexOf(" ") >-1){
        arrOfStr = CleanMessage.split(" ", 2);
  //      System.out.println("first part" + arrOfStr[0]);
  //     System.out.println("second part" + arrOfStr[1]);

         command = arrOfStr[0];
      } else {
        command = CleanMessage;
      }
 //System.out.println("-" + command + "-");
      switch(command){
        case "#quit":
           System.exit(0);
           break;
        case "#stop":
      //  System.out.println("inside stop");
            this.sendToAllClients("WARNING - Server has stopped listening for connections.");
           stopListening();
           break;
        case "#close":
           System.out.println(client.getInfo("login") + "Has disconnected ");
           this.sendToAllClients("WARNING - The server has stopped listening for connections");
           this.sendToAllClients("SERVER SHUTTING DOWN! DISCONNECTING!");
           this.sendToAllClients("Abnormal termination of connection.");
           stopListening();
           /* try{
             close();
           }
           catch(Exception e){} */
           //System.exit(0);
           break;
        case "#start":
             try{
               if (!isListening()){
                   listen();
               }else{
                 System.out.println("server is listening, cant hear more than that");}
               }
               catch (Exception e){
                   System.out.println("server is listening, cant hear more than that");
             }
          break;
        case "#getport":
          System.out.println(Integer.toString(getPort()));
          break;
        case "#setport":
          if (isListening()){
            System.out.println("server is open, #setport unavailable");
          } else {
            setPort(Integer.parseInt(arrOfStr[1]));
          }
          break;

        default:
      }
    //  System.out.println("part 1 = "+ServMessage[0]);
    //  System.out.println("part 2 = " +ServMessage[1]);

  }
  else{
//    System.out.println("inside client message-");

    String ClientMessage= "";
    String ClientCommand= "";
    if(s.indexOf("#") == 0){
  //    System.out.println("has #  " + s);
      if(s.indexOf(" ")>-1){
    //    System.out.println("has __ " );
        String[] ClientCode =new String[2];
        ClientCode = s.split(" ", 2);
    //    System.out.println("part 1-"+ ClientCode[0]+ "-");
    //    System.out.println("part 2"+ ClientCode[1]);
        if (ClientCode[0].contains("#login")){
        //  System.out.print("-"+ client.getInfo("login")+"-");

          if (client.getInfo("login") == null){
            Object obj= ClientCode[1];
            System.out.println("A new client is attempting to connect to the server.");
            System.out.println("Message received #login "+ ClientCode[1]+" ; from null");

            client.setInfo("login", obj);
            System.out.println( ClientCode[1]+" has logged on.");
          try{
            String p= ClientCode[1]+" has logged on.";
            Object obj2=p;
            client.sendToClient(obj2);
          } catch(Exception e){}
          }else {
            try{
              String q="login not authorized, disconnecting now";
              Object ec=q;
              client.sendToClient(ec);
              client.close();
            }
            catch(Exception e){}
          }
    //      System.out.println("is login attempt");

      //    System.out.println("login saved!");
      //    System.out.println(client.getInfo("login").toString());

        }

      }
      else{
      }
    }

  }
    System.out.println("Message received: " + msg + " from " + client);
    this.sendToAllClients(client.getInfo("login").toString() +" "+ msg);
  }

  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }

  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }

  //Class methods ***************************************************

  /**
   * This method is responsible for the creation of
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555
   *          if no argument is entered.
   */
  public static void main(String[] args)
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }

    EchoServer sv = new EchoServer(port);

    try
    {
      sv.listen(); //Start listening for connections
    }
    catch (Exception ex)
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
}
//End of EchoServer class
