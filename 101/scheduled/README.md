# Logging

## Exercises

### Exercise 1: Set loglevel to DEBUG for all dk packages

### Exercise 2: Use Logback configuration
Define a `resources/logback-spring.xml` file with the following content:

```
<?xml version="1.0" encoding="UTF-8"?>
<configuration>

    <appender name="Console"
        class="ch.qos.logback.core.ConsoleAppender">
        <layout class="ch.qos.logback.classic.PatternLayout">
            <Pattern>
                %black(%d{ISO8601}) %highlight(%-5level) [%blue(%t)] %yellow(%C{1.}): %msg%n%throwable
            </Pattern>
        </layout>
    </appender>
    
    <!-- LOG everything at INFO level -->
    <root level="info">
        <appender-ref ref="Console" />
    </root>

    <springProfile name="!production">
        <!-- configuration to be enabled when the "production" profile is not active -->
        <logger name="dk" level="trace" additivity="false">
            <appender-ref ref="Console" />
        </logger>
    </springProfile>

</configuration>
```