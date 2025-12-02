package teamficial.teamficial_be.global.config;

import org.apache.http.HttpHost;
import org.opensearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.opensearch.client.RestClient;

@Configuration
public class OpenSearchConfig {

    @Value("${opensearch.host}")
    private String host;
    @Value("${opensearch.port}")
    private Integer port;
    @Value("${opensearch.scheme}")
    private String scheme;

    @Bean
    public RestHighLevelClient openSearchClient() {
        return new RestHighLevelClient(
                RestClient.builder(new HttpHost(host, port, scheme)));
    }
}