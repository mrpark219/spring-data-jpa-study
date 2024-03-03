package study.springdatajpastudy.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.springdatajpastudy.repository.MemberRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberTest {

	@PersistenceContext
	EntityManager em;

	@Autowired
	MemberRepository memberRepository;

	@DisplayName("멤버, 팀 연관관계 변경 확인.")
	@Test
	void changeTeam() {

		// given
		Team teamA = new Team("teamA");
		Team teamB = new Team("teamB");
		em.persist(teamA);
		em.persist(teamB);

		Member member1 = new Member("member1", 10, teamA);
		Member member2 = new Member("member2", 20, teamA);
		Member member3 = new Member("member3", 30, teamB);
		Member member4 = new Member("member4", 40, teamB);

		em.persist(member1);
		em.persist(member2);
		em.persist(member3);
		em.persist(member4);

		em.flush();
		em.clear();

		// when
		Member findMember1 = em.find(Member.class, member1.getId());
		Member findMember2 = em.find(Member.class, member2.getId());
		Member findMember3 = em.find(Member.class, member3.getId());
		Member findMember4 = em.find(Member.class, member4.getId());

		Team findTeamA = em.find(Team.class, teamA.getId());
		Team findTeamB = em.find(Team.class, teamB.getId());

		// then
		assertThat(findMember1.getTeam()).isEqualTo(findTeamA);
		assertThat(findMember2.getTeam()).isEqualTo(findTeamA);
		assertThat(findMember3.getTeam()).isEqualTo(findTeamB);
		assertThat(findMember4.getTeam()).isEqualTo(findTeamB);

		assertThat(findTeamA.getMembers()).contains(findMember1, findMember2);
		assertThat(findTeamB.getMembers()).contains(findMember3, findMember4);
	}

	@DisplayName("JPA Auditing")
	@Test
	void jpaEventBaseEntity() throws InterruptedException {

	    // given
		Member member = new Member("member1");
		// @PrePersist
		memberRepository.save(member);

		Thread.sleep(100);
		member.setUsername("member2");

		// @PreUpdate
		em.flush();
		em.clear();

	    // when
		Member findMember = memberRepository.findById(member.getId()).get();

		// then
		System.out.println(findMember.getCreatedDate());
		System.out.println(findMember.getLastModifiedDate());
		System.out.println(findMember.getCreatedBy());
		System.out.println(findMember.getLastModifiedBy());
	}
}