package kpi.donate.repository;

import kpi.donate.model.entity.Donate;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface DonateRepo extends PagingAndSortingRepository<Donate, Long> {

    Optional<Donate> findByTokenAndUsed(String token, Boolean used);

    List<Donate> findAllByProjectIdAndUsed(Long projectId, Boolean used);
}
