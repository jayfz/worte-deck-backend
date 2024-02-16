package co.harborbytes.wortedeck.apiresponse;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Fail {
    private final String status = "fail";
    private Instant timestamp;
    private List<String> errors;
    private String debug;

    public Fail(){
        this.timestamp = Instant.now();
        this.errors =  new ArrayList<>();
    }

    public void addMessage(String message){
        this.errors.add(message);
    }
}
