package study.springdatajpastudy.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.springdatajpastudy.entity.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberJpaRepositoryTest {

	@Autowired
	MemberJpaRepository memberJpaRepository;

	@DisplayName("멤버 생성 및 저장 후 조회하여 같은 데이터가 있는지 확인.")
	@Test
	void testSaveMember() {

		// given
		Member member = new Member("memberA");

		// when
		Member savedMember = memberJpaRepository.save(member);

		// then
		Member findMember = memberJpaRepository.find(savedMember.getId());
		assertThat(findMember.getId()).isEqualTo(member.getId());
		assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
		assertThat(findMember).isEqualTo(member);
	}

	@DisplayName("멤버 CRUD 확인.")
	@Test
	void basicCRUD() {

		// given
		Member member1 = new Member("member1");
		Member member2 = new Member("member2");
		memberJpaRepository.save(member1);
		memberJpaRepository.save(member2);

		// when
		// 단건 조회
		Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
		Member findMember2 = memberJpaRepository.findById(member2.getId()).get();

		// 리스트 조회
		List<Member> all = memberJpaRepository.findAll();

		// 카운트 조회
		long count = memberJpaRepository.count();

		// 삭제
		memberJpaRepository.delete(member1);
		memberJpaRepository.delete(member2);

		// 삭제 후 카운트 조회
		long deletedCount = memberJpaRepository.count();

		// then
		assertThat(findMember1).isEqualTo(member1);
		assertThat(findMember2).isEqualTo(member2);

		assertThat(all.size()).isEqualTo(2);

		assertThat(count).isEqualTo(2);

		assertThat(deletedCount).isEqualTo(0);
	}

	@DisplayName("username이 일치하고 나이가 더 많은 멤버 목록 조회")
	@Test
	void findByUsernameAndAgeGreaterThen() {

		// given
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("AAA", 20);
		memberJpaRepository.save(m1);
		memberJpaRepository.save(m2);

		// when
		List<Member> result = memberJpaRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

		// then
		assertThat(result.get(0).getUsername()).isEqualTo("AAA");
		assertThat(result.get(0).getAge()).isEqualTo(20);
		assertThat(result.size()).isEqualTo(1);
	}

	@DisplayName("usernmae이 일치하는 멤버 목록 조회(NamedQuery)")
	@Test
	void findByUsername() {

		// given
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("BBB", 20);
		memberJpaRepository.save(m1);
		memberJpaRepository.save(m2);

		// when
		List<Member> result = memberJpaRepository.findByUsername("AAA");
		Member findMember = result.get(0);

		// then
		assertThat(findMember).isEqualTo(m1);
	}

	@DisplayName("전체 멤버 조회 페이징")
	@Test
	void paging() {

		// given
		memberJpaRepository.save(new Member("member1", 10));
		memberJpaRepository.save(new Member("member2", 10));
		memberJpaRepository.save(new Member("member3", 10));
		memberJpaRepository.save(new Member("member4", 10));
		memberJpaRepository.save(new Member("member5", 10));

		int age = 10;
		int offset = 0;
		int limit = 3;

		// when
		List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
		long totalCount = memberJpaRepository.totalCount(age);

		// then
		assertThat(members.size()).isEqualTo(3);
		assertThat(totalCount).isEqualTo(5);
	}

	@DisplayName("파라미터로 주어진 나이 이상인 모든 멤버의 나이를 +1 한다.")
	@Test
	void bulkAgePlus() {

	    // given
		memberJpaRepository.save(new Member("member1", 10));
		memberJpaRepository.save(new Member("member2", 19));
		memberJpaRepository.save(new Member("member3", 20));
		memberJpaRepository.save(new Member("member4", 21));
		memberJpaRepository.save(new Member("member5", 40));

	    // when
		int resultCount = memberJpaRepository.bulkAgePlus(20);

	    // then
		assertThat(resultCount).isEqualTo(3);
	}
}