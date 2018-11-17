package kpi.donate.repository;

import kpi.donate.model.entity.Maecenas;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MaecenasRepo extends PagingAndSortingRepository<Maecenas, Long> {
}
