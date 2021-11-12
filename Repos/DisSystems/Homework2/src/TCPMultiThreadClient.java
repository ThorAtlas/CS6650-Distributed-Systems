import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class TCPMultiThreadClient {
  public static void main(String[] args) throws IOException {

    Scanner scanner = new Scanner((System.in));
    System.out.println("Enter IP and port number separated by a space: ");
    String IPPort = scanner.nextLine();
    String[] IPPortList = IPPort.split(" ");
    String IP = IPPortList[0];
    Integer port = Integer.parseInt(IPPortList[1]);

    Socket s = new Socket(IP, port);

    //Input/Output streams are bridges from byte streams to character streams
    //reads the stream coming from source
    InputStreamReader inputStreamReader = new InputStreamReader(s.getInputStream());
    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(s.getOutputStream());
    PrintWriter pr = new PrintWriter(s.getOutputStream(), true);

    //buffers reads and writes a large block of data at a time. Improves efficiency
    BufferedReader bf = new BufferedReader(inputStreamReader);
//      BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

    try {
      while(true) {

        System.out.print("Enter your text: ");
        String textInput = scanner.nextLine();

        pr.println(textInput);
        pr.flush();

        if(textInput.equals("Q")){
          break;
        }

        //reads the line buffered and prints it out
        String str = bf.readLine();
        System.out.println("Server response: " + str);
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}

