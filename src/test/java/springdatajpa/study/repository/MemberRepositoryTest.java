package springdatajpa.study.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import springdatajpa.study.entity.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

	@Autowired
	MemberRepository memberRepository;

	@DisplayName("멤버 생성 및 저장 후 조회하여 같은 데이터가 있는지 확인.")
	@Test
	void testSaveMember() {

		System.out.println("memberRepository = " + memberRepository.getClass());

		// given
		Member member = new Member("memberA");

		// when
		Member savedMember = memberRepository.save(member);

		// then
		Member findMember = memberRepository.findById(savedMember.getId()).get();
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
		memberRepository.save(member1);
		memberRepository.save(member2);

		// when
		// 단건 조회
		Member findMember1 = memberRepository.findById(member1.getId()).get();
		Member findMember2 = memberRepository.findById(member2.getId()).get();

		// 리스트 조회
		List<Member> all = memberRepository.findAll();

		// 카운트 조회
		long count = memberRepository.count();

		// 삭제
		memberRepository.delete(member1);
		memberRepository.delete(member2);

		// 삭제 후 카운트 조회
		long deletedCount = memberRepository.count();

		// then
		assertThat(findMember1).isEqualTo(member1);
		assertThat(findMember2).isEqualTo(member2);

		assertThat(all.size()).isEqualTo(2);

		assertThat(count).isEqualTo(2);

		assertThat(deletedCount).isEqualTo(0);
	}

	@DisplayName("username이 일치하고 나이가 더 많은 회원 목록 조회")
	@Test
	void findByUsernameAndAgeGreaterThen() {

		// given
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("AAA", 20);
		memberRepository.save(m1);
		memberRepository.save(m2);

		// when
		List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

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
		memberRepository.save(m1);
		memberRepository.save(m2);

		// when
		List<Member> result = memberRepository.findByUsername("AAA");
		Member findMember = result.get(0);

		// then
		assertThat(findMember).isEqualTo(m1);
	}
}