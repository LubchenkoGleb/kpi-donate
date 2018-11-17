package kpi.donate.model.dto;

import kpi.donate.model.entity.Donate;
import kpi.donate.model.entity.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {

    private Project project;

    private List<Donate> donates;
}
