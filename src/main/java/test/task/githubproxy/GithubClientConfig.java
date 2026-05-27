package test.task.githubproxy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
class GithubClientConfig {
    @Bean
    GithubClient githubClient() {
        var restClient = RestClient.builder()
                .defaultHeader("Accept", "application/vnd.github.v3+json")
                .defaultStatusHandler(status -> status.value() == 404, (_, _) -> {
                    throw new GithubUserNotFoundException("GitHub user not found");
                })
                .build();

        var adapter = RestClientAdapter.create(restClient);
        var factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(GithubClient.class);
    }
}
