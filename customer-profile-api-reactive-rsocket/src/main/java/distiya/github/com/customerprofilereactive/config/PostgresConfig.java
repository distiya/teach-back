package distiya.github.com.customerprofilereactive.config;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@EnableR2dbcRepositories
public class PostgresConfig extends AbstractR2dbcConfiguration {

    @Value("${app.data.postgres.host}")
    private String host;
    @Value("${app.data.postgres.port}")
    private Integer port;
    @Value("${app.data.postgres.user}")
    private String user;
    @Value("${app.data.postgres.password}")
    private String password;
    @Value("${app.data.postgres.database}")
    private String database;

    @Override
    public ConnectionFactory connectionFactory() {
        return new PostgresqlConnectionFactory(
                PostgresqlConnectionConfiguration.builder()
                        .host(host)
                        .port(port)
                        .username(user)
                        .password(password)
                        .database(database)
                        .build());
    }

    @Bean
    DatabaseClient databaseClient(ConnectionFactory connectionFactory){
        return DatabaseClient.create(connectionFactory);
    }
}