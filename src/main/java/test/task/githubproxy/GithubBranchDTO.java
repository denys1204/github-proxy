package test.task.githubproxy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
record GithubBranchDTO(String name, GithubCommitDTO commit) {}
