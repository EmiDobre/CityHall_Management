package org.example;

public class CerereException extends Exception{
    String message;
    public CerereException(String message){
        this.message = message;
    };
}
