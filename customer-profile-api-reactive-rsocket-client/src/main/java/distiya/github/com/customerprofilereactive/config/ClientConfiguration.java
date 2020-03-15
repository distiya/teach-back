package distiya.github.com.customerprofilereactive.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketRequester;

@Configuration
public class ClientConfiguration {

    @Value("${app.rsocket.server.host}")
    private String rsocketServerHost;
    @Value("${app.rsocket.server.port}")
    private Integer rsocketServerPort;

    @Bean
    public RSocketRequester rSocketRequester(RSocketRequester.Builder b) {
        return b.connectTcp(rsocketServerHost, rsocketServerPort).block();
    }

}
