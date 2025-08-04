package th.mfu;

import java.io.*;
import java.net.*;

public class MockWebServer implements Runnable {

    private int port;

    public MockWebServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Mock Web Server running on port " + port + "...");

            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();

                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                    String line;
                    while ((line = in.readLine()) != null && !line.isEmpty()) {
                        System.out.println("[" + port + "] Request: " + line);
                    }

                    String response = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n"
                        + "<html><body>Hello, Web! on Port " + port + "</body></html>";

                    out.print(response);
                    out.flush();

                    clientSocket.close();
                } catch (IOException e) {
                    System.err.println("[" + port + "] Error handling client: " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.err.println("[" + port + "] Server error: " + e.getMessage());
        }

        System.out.println("Mock Web Server on port " + port + " stopped.");
    }

    public static void main(String[] args) {
        Thread server1 = new Thread(new MockWebServer(8080));
        server1.start();

        Thread server2 = new Thread(new MockWebServer(8081));
        server2.start();
        
        System.out.println("Press any key to stop the server...");
        try {
            System.in.read();

            server1.stop();
            server1.interrupt();
            server2.stop();
            server2.interrupt();
            System.out.println("Mock web server stopped.");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}