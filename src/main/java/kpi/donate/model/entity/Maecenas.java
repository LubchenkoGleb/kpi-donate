package kpi.donate.model.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class Maecenas {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @NotNull
    private String image;

    @NotNull
    @Column(name = "project_id")
    private Long projectId;
}
