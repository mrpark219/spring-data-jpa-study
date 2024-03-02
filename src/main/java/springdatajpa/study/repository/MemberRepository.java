package springdatajpa.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springdatajpa.study.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
