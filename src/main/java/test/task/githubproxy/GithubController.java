package test.task.githubproxy;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/github")
class GithubController {
    private final GithubService githubService;

    GithubController(GithubService githubService) {
        this.githubService = githubService;
    }

    @GetMapping("/{username}")
    List<Repository> getRepositories(@PathVariable String username) {
        return githubService.getRepositories(username);
    }

    @ExceptionHandler(GithubUserNotFoundException.class)
    ResponseEntity<ErrorResponse> handleGithubUserNotFoundException(GithubUserNotFoundException e) {
        return ResponseEntity.status(404).body(new ErrorResponse(404, e.getMessage()));
    }
}
