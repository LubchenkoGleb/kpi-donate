package kpi.donate.rest;

import kpi.donate.model.dto.ProjectDto;
import kpi.donate.model.entity.Donate;
import kpi.donate.model.entity.Project;
import kpi.donate.repository.DonateRepo;
import kpi.donate.repository.ProjectRepo;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/project")
public class ProjectController {

    private final ProjectRepo projectRepo;

    private final DonateRepo donateRepo;

    public ProjectController(ProjectRepo projectRepo, DonateRepo donateRepo) {
        this.projectRepo = projectRepo;
        this.donateRepo = donateRepo;
    }

    @PostMapping
    private ResponseEntity<Project> create(@Valid @RequestBody Project project) {
        return ResponseEntity.ok(projectRepo.save(project));
    }

    @GetMapping
    private ResponseEntity<Iterable<Project>> getAll() {
        Iterable<Project> projects = projectRepo.findAll(new Sort(Sort.Direction.DESC, "open"));
        return ResponseEntity.ok(projects);
    }

    @GetMapping(value = "/{id}")
    private ResponseEntity<ProjectDto> getById(@PathVariable Long id) {

        Project project = projectRepo.findById(id).orElseThrow(() ->
                new RuntimeException("Project with id '" + id + "' not found"));

        List<Donate> donates = donateRepo.findAllByProjectIdAndUsed(project.getId(), true);

        ProjectDto response = new ProjectDto(project, donates);

        return ResponseEntity.ok(response);
    }

}
