package study.springdatajpastudy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.springdatajpastudy.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
