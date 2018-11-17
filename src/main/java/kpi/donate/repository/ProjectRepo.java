package kpi.donate.repository;

import kpi.donate.model.entity.Project;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepo extends PagingAndSortingRepository<Project, Long> {
}
