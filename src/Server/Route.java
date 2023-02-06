package Server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Route {
    private String endpoint;
    private String html;

    public Route(String file){
        if (("/"+file.substring(0, file.indexOf("."))).strip().contains("index")){
            this.endpoint = "/";
        }else{
            this.endpoint = "/"+file.substring(0, file.indexOf(".")).strip();
        }
        this.html = readFile("src/Pages/"+file);
    }
    public String getEndpoint(){
        return this.endpoint;
    }
    public String getHtml(){
        return this.html;
    }
    public static String readFile(String fileSrc){
        StringBuilder contentBuilder = new StringBuilder();
        try{
            BufferedReader in = new BufferedReader(new FileReader(fileSrc));
            String str;
            
            while ((str = in.readLine()) != null){
                contentBuilder.append(str);
            }
            in.close();
        }catch (IOException e){
            System.err.println("Could not find file " + e.getMessage());
        }
        return contentBuilder.toString();
    }
}
