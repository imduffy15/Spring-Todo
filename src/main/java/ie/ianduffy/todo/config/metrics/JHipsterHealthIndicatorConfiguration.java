package ie.ianduffy.todo.config.metrics;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class JHipsterHealthIndicatorConfiguration implements InitializingBean {

    @Inject
    private DataSource dataSource;

    private final DatabaseHealthCheckIndicator databaseHealthCheckIndicator = new DatabaseHealthCheckIndicator();

    @Bean
    public HealthIndicator<Map<String, HealthCheckIndicator.Result>> healthIndicator() {
        return new HealthIndicator<Map<String, HealthCheckIndicator.Result>>() {
            @Override
            public Map<String, HealthCheckIndicator.Result> health() {
                Map<String, HealthCheckIndicator.Result> healths = new LinkedHashMap<>();

                healths.putAll(databaseHealthCheckIndicator.health());

                return healths;
            }
        };
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        databaseHealthCheckIndicator.setDataSource(dataSource);
    }
}
