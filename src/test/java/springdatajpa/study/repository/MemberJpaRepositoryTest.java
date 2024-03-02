package springdatajpa.study.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import springdatajpa.study.entity.Member;

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
}