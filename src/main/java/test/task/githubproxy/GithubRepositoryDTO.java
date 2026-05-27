package test.task.githubproxy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
record GithubRepositoryDTO(String name, GithubOwnerDTO owner, boolean fork) {}
