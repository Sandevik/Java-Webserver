import Server.Server;

import java.io.*;

public class HttpServer {
    public static void main(String[] args) throws IOException {

        Server server = new Server(8087);
        server.start();
        
       
    }

}
