package test.task.githubproxy;

import java.util.List;

record Repository(String repositoryName, String ownerLogin, List<Branch> branches) {
    Repository {
        branches = List.copyOf(branches);
    }
}
