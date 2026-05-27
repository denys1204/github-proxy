package test.task.githubproxy;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

@HttpExchange(url = "https://api.github.com")
interface GithubClient {
    @GetExchange("/users/{username}/repos")
    List<GithubRepositoryDTO> getRepositories(@PathVariable String username);

    @GetExchange("/repos/{owner}/{repo}/branches")
    List<GithubBranchDTO> getBranches(@PathVariable String owner, @PathVariable String repo);
}
