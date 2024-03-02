package springdatajpa.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import springdatajpa.study.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

	List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

	// @Query(name = "Member.findByUsername") 생략해도 NamedQuery로 동작함
	List<Member> findByUsername(@Param("username") String username);
}
