package it.berkhel.email;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Message {

    public static Message parse(String jsonString) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonString, Message.class);
    }

    private String email;
    private String message;

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }


}
