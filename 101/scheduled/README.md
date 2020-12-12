# Scheduled

## Exercises

Create a Naked Springboot application.

Add the `@EnableScheduling`.

### Exercise 1: Schedule a job to run every other minute
Make a service with a method that prints current time _every other ODD minute monday to friday_.

#### Solution
`@Scheduled(cron = "0 1/2 * * * MON-FRI")`
