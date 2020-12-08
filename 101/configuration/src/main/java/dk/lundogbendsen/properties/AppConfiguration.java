package dk.lundogbendsen.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {
    @Value("${app.name:NoName}")
    public String appName;

    @Value("${app.configIdentifier:Default}")
    public String configurationId;

    public String getConfigurationId() {
        return configurationId;
    }

    public void setConfigurationId(String configurationId) {
        this.configurationId = configurationId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AppConfiguration{");
        sb.append("appName='").append(appName).append('\'');
        sb.append(", configurationId='").append(configurationId).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
