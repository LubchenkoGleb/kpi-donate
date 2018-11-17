package kpi.donate.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Donate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String image;

    private String token;

    private double amount;

    private boolean used;

    @NotNull
    @Column(name = "project_id")
    private Long projectId;

    public Donate(String token, double amount, boolean used, @NotNull Long projectId) {
        this.token = token;
        this.amount = amount;
        this.used = used;
        this.projectId = projectId;
    }
}
