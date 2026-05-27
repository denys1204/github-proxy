package test.task.githubproxy;

class GithubUserNotFoundException extends RuntimeException {
    GithubUserNotFoundException(String message) {
        super(message);
    }
}
