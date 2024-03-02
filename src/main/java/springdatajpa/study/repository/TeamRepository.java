package springdatajpa.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springdatajpa.study.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
