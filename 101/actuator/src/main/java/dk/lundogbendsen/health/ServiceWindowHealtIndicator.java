package dk.lundogbendsen.health;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Component
public class ServiceWindowHealtIndicator implements HealthIndicator {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.forLanguageTag("DK"));
    @Value("${app.health.serviceWindowStart}")
    private String serviceWindowStart;
    @Value("${app.health.serviceWindowEnd}")
    private String serviceWindowEnd;

    @SneakyThrows
    @Override
    public Health health() {
        Date start = formatter.parse(serviceWindowStart);
        Date end = formatter.parse(serviceWindowEnd);
        final Date now = new Date();
        boolean inMaintenance = now.after(start) && now.before(end);
        if (!inMaintenance) {
            return Health.up().build();
        }
        return Health.down()
                .withDetail("Reason", "In Maintenance period")
                .withDetail("Start", serviceWindowStart)
                .withDetail("End", serviceWindowEnd)
                .build();
    }
}
