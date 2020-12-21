package dk.lundogbendsen.springprimer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class SpringPrimerApplication {
    public static void main(String[] args) {
        final ConfigurableApplicationContext context = SpringApplication.run(SpringPrimerApplication.class, args);
        final TextUpperCaser bean = context.getBean(TextUpperCaser.class);
        System.out.println(bean.toUpperCase("Hello World"));

        final ServiceA serviceA = context.getBean(ServiceA.class);
        serviceA.callB();
    }
}
