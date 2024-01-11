package com.vini;

import com.sun.net.httpserver.HttpServer;
import com.vini.server.socket.SocketServer;
import com.vini.server.web.WebServer;
import com.vini.server.web.routes.Routes;
import java.io.IOException;

public class App {
  public static void main(String[] args) throws IOException {
    App.startWebServer();
    App.startSocketServer();
  }

  private static void startWebServer() throws IOException {
    HttpServer webServer = WebServer.createServer();
    Routes.registerRoutes(webServer);
    webServer.setExecutor(null);

    webServer.start();

    System.out.println("Server is running");
  }

  private static void startSocketServer() throws IOException {
    SocketServer socketServer = new SocketServer();
    socketServer.run();
    System.out.println("Socket is running");
  }
}
