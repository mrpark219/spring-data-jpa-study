package springdatajpa.study.repository;

import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
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
	List<Member> findByNames(@Param("names") List<String> names);

	// 컬렉션
	List<Member> findListByUsername(String username);

	// 단건
	Member findMemberByUsername(String username);

	// 단건 Optional
	Optional<Member> findOptionalByUsername(String username);

	Page<Member> findPageByAge(int age, Pageable pageable);

	Slice<Member> findSliceByAge(int age, Pageable pageable);

	@Query(
		value = "Select m from Member m left join m.team t",
		countQuery = "select count(m) from Member m")
	Page<Member> findSelfPageByAge(int age, Pageable pageable);

	@Modifying
	@Query("update Member m set m.age = m.age + 1 where m.age >= :age")
	int bulkAgePlus(@Param("age") int age);

	@Query("select m from Member m left join fetch m.team")
	List<Member> findMemberFetchJoin();

	@Override
	@EntityGraph(attributePaths = {"team"})
	List<Member> findAll();

	@EntityGraph(attributePaths = {"team"})
	@Query("select m from Member m")
	List<Member> findMemberEntityGraph();

	// @EntityGraph(attributePaths = {"team"})
	@EntityGraph("Member.all")
	List<Member> findEntityGraphByUsername(@Param("username") String username);

	@QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
	Member findReadOnlyByUsername(String username);
}
