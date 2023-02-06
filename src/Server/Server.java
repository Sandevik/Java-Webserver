package Server;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server{
    int port = 0;
    String address = "localhost";
    InetAddress addr;
    ServerSocket serverSocket;
    ArrayList<Route> routes = new ArrayList<>();

    public Server(String address, int port){
        this.port = port;
        this.address = address;
    }
    public Server(int port){
        this.port = port; 
    }
    public Server(){}

    public void start() throws IOException{
        addr = InetAddress.getByName(address);
        serverSocket = new ServerSocket(port, 50, addr);
        System.out.println("loading files...");
        loadFiles();
        System.out.println("Server Started");
        
        while (true){
            RequestHeaders requestHeaders = new RequestHeaders();   
            Socket clientSocket = serverSocket.accept();
            System.err.println("Client Connected");
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            ArrayList<String> responseList = new ArrayList<String>();
            String respLine; 
            while ((respLine = in.readLine()) != null){
                for (String string : respLine.split(" ")) {
                    responseList.add(string);
                }
                if (respLine.isEmpty()){
                    break;
                }
            }
            requestHeaders.setMethod(responseList.get(0).strip());
            requestHeaders.setDestination(responseList.get(1).strip());    
            System.out.println("Request to: "+requestHeaders.getDestination());

            OutputStream clientOut = clientSocket.getOutputStream();
            clientOut.write("HTTP/1.1 200 OK\r\n".getBytes());
            clientOut.write("\r\n".getBytes());
            clientOut.write(determineResponse(routes, requestHeaders).getBytes());
            clientOut.write("\r\n\r\n".getBytes());
            clientOut.flush();

            System.err.println("Client Connection Closed");
            in.close();
            clientOut.close();
        }
    }

    public String getServerLocation(){
        return serverSocket.getInetAddress().toString();
    }

    public int getServerPort(){
        return serverSocket.getLocalPort();
    }

    private void loadFiles(){
        for (File page : new File("src/Pages").listFiles()) {
            routes.add(new Route(page.getName().trim()));
        }
    }
    
    private String determineResponse(ArrayList<Route> routes, RequestHeaders requestHeaders){
        String html = "";
        Boolean routeExists = false;
        for (Route route : routes) {
            if (route.getEndpoint().equals(requestHeaders.getDestination())){
                html = route.getHtml();
                routeExists = true;
                break;
            }
        }
        if(routeExists == false){
            // Read Error File contents
           html = Route.readFile("./src/Pages/error.html");
        }
        return html;   
    }
    
}





