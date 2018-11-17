package kpi.donate.rest;

import kpi.donate.model.entity.Project;
import kpi.donate.repository.ProjectRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "project")
public class ProjectController {

    private final ProjectRepo projectRepo;

    public ProjectController(ProjectRepo projectRepo) {
        this.projectRepo = projectRepo;
    }

    @PostMapping
    private ResponseEntity<Project> create(@Valid @RequestBody Project project) {
        return ResponseEntity.ok(projectRepo.save(project));
    }
}
