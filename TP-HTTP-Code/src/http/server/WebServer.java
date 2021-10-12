///A Simple Web Server (WebServer.java)

package http.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Example program from Chapter 1 Programming Spiders, Bots and Aggregators in
 * Java Copyright 2001 by Jeff Heaton
 *
 * WebServer is a very simple web-server. Any request is responded with a very
 * simple web-page.
 *
 * @author Jeff Heaton
 * @version 1.0
 */

public class WebServer {


  protected void start() {
    ServerSocket s;

    System.out.println("Webserver starting up on port 80");
    System.out.println("(press ctrl-c to exit)");
    try {
      // create the main server socket
      s = new ServerSocket(3000);
    } catch (Exception e) {
      System.out.println("Error: " + e);
      return;
    }

    System.out.println("Waiting for connection");
    for (;;) {
      try {
        // wait for a connection
        Socket remote = s.accept();
        // remote is now the connected socket
        System.out.println("Connection, sending data.");
        BufferedReader in = new BufferedReader(new InputStreamReader(
                remote.getInputStream()));
        PrintWriter out = new PrintWriter(remote.getOutputStream());

        // read the data sent. We basically ignore it,
        // stop reading once a blank line is hit. This
        // blank line signals the end of the client HTTP
        // headers.
        String str = ".";
        String firstLineHeader = ".";
        int compteur = 0 ;

        while (!str.equals("")){
          str = in.readLine();
          //show the header over time
          out.println(str);
          //isolate the first line to get the type after
          if (compteur == 0)
            firstLineHeader = str ;

          compteur++;

        }
        out.println("premiere ligne du header");
        out.println(firstLineHeader);

        // get the type of the http request
        String[] parsefirstLineHeader = firstLineHeader.split(" ");
        String typeRequest = parsefirstLineHeader[0];

        out.println("type de la requete");
        out.println(typeRequest);

        String resourceName = parsefirstLineHeader[1].substring(1, parsefirstLineHeader[1].length());
        out.println("resource name + " + resourceName);
        // processing according to the type of request
                        /*if(typeRequest.equals("GET")) {
                         // httpGET(out, resourceName);

                        } else if(typeRequest.equals("PUT")) {
                          httpPUT(in, out, resourceName);
                        } else if(typeRequest.equals("POST")) {
                          httpPOST(in, out, resourceName);
                        } else if(typeRequest.equals("HEAD")) {
                          httpHEAD(in, out, resourceName);
                        } else if(typeRequest.equals("DELETE")) {
                          httpDELETE(out, resourceName);
                        } else {
                          // Si la requ�te ne correspond � rien de connu
                          out.write(makeHeader("501 Not Implemented").getBytes());
                          out.flush();
                        }
                       */





        // Send the response




        // Send the headers
        out.println("HTTP/1.0 200 OK");
        out.println("Content-Type: text/html");
        out.println("Server: Bot");
        // this blank line signals the end of the headers
        out.println("");
        // Send the HTML page
        out.println("<H1>Welcome to the Ultra Mini-WebServer</H2>");

        Path string = Path.of("TP-HTTP-Code/src/http/server/fichiertexte.html");
        String content = Files.readString(string, StandardCharsets.US_ASCII);

        out.println(content);
        out.flush();
        remote.close();

      } catch (Exception e) {
        System.out.println("Error: " + e);
      }
    }
  }


  /**
   * Start the application.
   *
   * @param args
   *            Command line parameters are not used.
   */
  public static void main(String args[]) {
    WebServer ws = new WebServer();
    ws.start();
  }


  protected void httpGET(BufferedOutputStream out, String filename) {
    System.out.println("GET " + filename);
    try {
      // V�rification de l'existence de la ressource demand�e
      File resource = new File(filename);
      if(resource.exists() && resource.isFile()) {
        // Envoi du Header signalant un succ�s
        out.write(makeHeader("200 OK", filename, resource.length()).getBytes());
      }

      // Ouverture d'un flux de lecture binaire sur le fichier demand�
      BufferedInputStream fileIn = new BufferedInputStream(new FileInputStream(resource));
      // Envoi du corps : le fichier (page HTML, image, vid�o...)
      byte[] buffer = new byte[256];
      int nbRead;
      while((nbRead = fileIn.read(buffer)) != -1) {
        out.write(buffer, 0, nbRead);
      }
      // Fermeture du flux de lecture
      fileIn.close();

      //Envoi des donn�es
      out.flush();
    } catch (IOException e) {
      e.printStackTrace();
      // En cas d'erreur on essaie d'avertir le client
      try {
        // out.write(makeHeader("500 Internal Server Error").getBytes());
        out.flush();
      } catch (Exception e2) {};
    }
  }


  protected String makeHeader(String status, String filename, long length) {
    String header = "HTTP/1.0 " + status + "\r\n";
    if(filename.endsWith(".html") || filename.endsWith(".htm"))
      header += "Content-Type: text/html\r\n";
    else if(filename.endsWith(".mp4"))
      header += "Content-Type: video/mp4\r\n";
    else if(filename.endsWith(".png"))
      header += "Content-Type: image/png\r\n";
    else if(filename.endsWith(".jpeg") || filename.endsWith(".jpeg"))
      header += "Content-Type: image/jpg\r\n";
    else if(filename.endsWith(".mp3"))
      header += "Content-Type: audio/mp3\r\n";
    else if(filename.endsWith(".avi"))
      header += "Content-Type: video/x-msvideo\r\n";
    else if(filename.endsWith(".css"))
      header += "Content-Type: text/css\r\n";
    else if(filename.endsWith(".pdf"))
      header += "Content-Type: application/pdf\r\n";
    else if(filename.endsWith(".odt"))
      header += "Content-Type: application/vnd.oasis.opendocument.text\r\n";
    header += "Content-Length: " + length + "\r\n";
    header += "Server: Bot\r\n";
    header += "\r\n";
    System.out.println("ANSWER HEADER :");
    System.out.println(header);
    return header;
  }
}
