// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************

  /**
   * The interface type variable.  It allows the implementation of
   * the display method in the client.
   */
  ChatIF clientUI;


  //Constructors ****************************************************

  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
   String UserId = "";
  public ChatClient(String UserId, String host, int port, ChatIF clientUI)
    throws IOException
  {
    super(host, port); //Call the superclass constructor

    if(UserId.equals("")){
      System.out.println("ERROR - No login ID specified.  Connection aborted.");
      System.exit(0);
    }
    this.clientUI = clientUI;
    this.UserId = UserId;
    openConnection();
    sendToServer("#login " + UserId);
}



  //Instance methods ************************************************

  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg)
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI
   *
   * @param message The message from the UI.
   */
  public void handleMessageFromClientUI(String message)
  {
    try
    {
     //sendToServer(message);
    //System.out.println(message);
    boolean isClientCommand = false;
      String[] arrOfStr =new String[2];
      if (message.indexOf("#") == 0){
        isClientCommand = true;
        String str = message;
        String command = "";
        if(str.indexOf(" ") >-1){
          arrOfStr = str.split(" ", 2);
          clientUI.display
          (arrOfStr[0]);
          clientUI.display
           (arrOfStr[1]);
           command = arrOfStr[0];
        } else {
          command = str;
        }


       switch(command){
         case "#quit":
            sendToServer(UserId + " has disconected");
            closeConnection();
            System.exit(0);
            break;
          case "#logoff":
          sendToServer(UserId + " has disconected");
            closeConnection();
            clientUI.display("Connection closed.");
            break;
          case "#login":
          if (arrOfStr[1] == "" || arrOfStr[1] == null){
            if (isConnected()){
              clientUI.display
                  ("Active Connection, #login not allowed");
            } else {
                 openConnection();
              }
          } else {
            sendToServer("#login " + arrOfStr[1]);
          }

             break;
          case "#gethost":
              if (isConnected()){
                clientUI.display
                  (getHost());
              } else {
                clientUI.display
                  ("inactive Connection, #gethost not allowed");
              }
              break;
          case "#getport":
               if (isConnected()){
                 clientUI.display
                   ( Integer.toString(getPort()));
               } else {
                 clientUI.display
                   ("inactive Connection, #getport not allowed");
               }
              break;
           case "#sethost":
                if (isConnected()){
                   clientUI.display
                   ("Active Connection, #sethost not allowed");
                 } else {
                     setHost(arrOfStr[1]);
                     clientUI.display("Host set to: "+ arrOfStr[1] );
                 }
                 break;
            case "#setport":
                 if (isConnected()){
                clientUI.display
                     ("Active Connection, #setport not allowed");
                 } else {
                 setPort(Integer.parseInt(arrOfStr[1]));
                 clientUI.display("Port set to: "+ arrOfStr[1] );

                 }
                 break;
       default:
       }
      // sendToServer(message);
     }
    // boolean connected = isConnected();
    //  System.out.println(isConnected());
    if(!isClientCommand){
      //System.out.print(message);
      sendToServer(message);
    }
  }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }

  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
}
//End of ChatClient class
