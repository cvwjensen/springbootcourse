package dk.lundogbendsen.springprimer;


import org.springframework.stereotype.Component;

@Component
public class ServiceB {
    public void bMethod() {
        System.out.println("ServiceB - HelloWorld");
    }
}
