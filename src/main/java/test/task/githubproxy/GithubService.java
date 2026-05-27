package test.task.githubproxy;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
class GithubService {
    private final GithubClient githubClient;

    GithubService(GithubClient githubClient) {
        this.githubClient = githubClient;
    }

    List<Repository> getRepositories(String username) {
        return githubClient.getRepositories(username).stream()
                .filter(repoDTO -> !repoDTO.fork())
                .map(this::fetchBranchesAndMap)
                .toList();
    }

    private Repository fetchBranchesAndMap(GithubRepositoryDTO repoDTO) {
        var branchesDTO = githubClient.getBranches(repoDTO.owner().login(), repoDTO.name());

        var branches = branchesDTO.stream()
                .map(branchDTO -> new Branch(branchDTO.name(), branchDTO.commit().sha()))
                .toList();

        return new Repository(repoDTO.name(), repoDTO.owner().login(), branches);
    }
}
