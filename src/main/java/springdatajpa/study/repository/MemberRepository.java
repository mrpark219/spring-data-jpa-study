package springdatajpa.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import springdatajpa.study.dto.MemberDto;
import springdatajpa.study.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

	List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

	// @Query(name = "Member.findByUsername") 생략해도 NamedQuery로 동작함
	List<Member> findByUsername(@Param("username") String username);

	@Query("select m from Member m where m.username = :username and m.age = :age")
	List<Member> findMember(@Param("username") String username, @Param("age") int age);

	@Query("select m.username from Member m")
	List<String> findUsernameList();

	@Query("select new springdatajpa.study.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
	List<MemberDto> findMemberDto();

	@Query("select m from Member m where m.username in :names")
	List<Member> findByNames(@Param("names")List<String> names);

	// 컬렉션
	List<Member> findListByUsername(String username);

	// 단건
	Member findMemberByUsername(String username);

	// 단건 Optional
	Optional<Member> findOptionalByUsername(String username);
}
