package test.task.githubproxy;

class GithubUserNotFoundException extends RuntimeException {
    GithubUserNotFoundException() {
        super("User not found");
    }
}
