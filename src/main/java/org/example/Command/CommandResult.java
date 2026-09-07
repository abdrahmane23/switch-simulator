package org.example.Command;

public record CommandResult(boolean result, String message) {
    public CommandResult(boolean result){
        this(result,"");
    }
}
