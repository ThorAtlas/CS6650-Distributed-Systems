import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.Scanner;

public class server {

  //function to reverse a string
  public static String reverseString(String string){
    StringBuilder builder = new StringBuilder();
    builder.append(string);
    builder.reverse();
    return builder.toString();
  }

  //fucntion to inverse letters
  public static String inverseLetters(String string){
    StringBuilder newstring = new StringBuilder();
    for(int i = 0; i< string.length(); i++){
      Character chr = string.charAt(i);
      if(Character.isLowerCase(chr)){
        newstring.append(Character.toUpperCase(chr));
      } else if (Character.isUpperCase(chr)){
        newstring.append(Character.toLowerCase(chr));
      }else {
        newstring.append(chr);
      }
    }
    return newstring.toString();
  }


  public static void main(String[] args){
    try {

      Scanner scanner = new Scanner(System.in);
      System.out.println("Enter the socket value you wish the port to be: ");
      Integer IPPort = Integer.valueOf(scanner.nextLine());

      //creation of the port/socket
      ServerSocket ss = new ServerSocket(IPPort);
      //server passively waits for a connection
      Socket s = ss.accept();

      //lets serverside node a client has connected
      System.out.println("client connected");

      //gets the input stream
      InputStreamReader in = new InputStreamReader(s.getInputStream());
      BufferedReader bf = new BufferedReader(in);

      //gets what the client printed
      String str = bf.readLine();

      //warning that the client word length is greater than 80
      if(str.length() > 80){
        System.out.println("client word length is greater than 80");
      }

      System.out.println("client : " + str);

      String inversedString = inverseLetters(str);

      String flippedString = reverseString(inversedString);

      //sneds the flipped string back to the cleint and flushes the printwriter
      PrintWriter pr = new PrintWriter (s.getOutputStream());
      pr.println("Flipped string is: " + flippedString);
      pr.flush();


    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
