package Server;

public class RequestHeaders {
    private String Method, Destination = "";
    public RequestHeaders(){}
    public void setDestination(String destination){
        if(this.Destination.isEmpty() && !destination.contains("favicon.icon")){
            this.Destination = destination;
        }
    }
    public void setMethod(String method){
        this.Method = method;
    }

    public String getMethod(){
        return this.Method;
    }
    public String getDestination(){
        return this.Destination;
    }

}
