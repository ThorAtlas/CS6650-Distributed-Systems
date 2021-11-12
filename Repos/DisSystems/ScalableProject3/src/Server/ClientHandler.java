package Server;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable{
  private Socket clientSocket;
  private BufferedReader in;
  private PrintWriter out;

  @Override
  public void run() {

  }
}
