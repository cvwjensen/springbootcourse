package dk.lundogbendsen.springprimer;

import org.springframework.stereotype.Service;

@Service
public class TextUpperCaser {
    public String toUpperCase(String message) {
        return message.toUpperCase();
    }
}
