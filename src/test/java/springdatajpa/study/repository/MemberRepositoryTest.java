package springdatajpa.study.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import springdatajpa.study.dto.MemberDto;
import springdatajpa.study.entity.Member;
import springdatajpa.study.entity.Team;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

	@Autowired
	MemberRepository memberRepository;

	@Autowired
	TeamRepository teamRepository;

	@PersistenceContext
	EntityManager em;

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

	@DisplayName("username과 나이가 일치하는 멤버 목록 조회")
	@Test
	void findMember() {

		// given
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("BBB", 20);
		memberRepository.save(m1);
		memberRepository.save(m2);

		// when
		List<Member> result = memberRepository.findMember("AAA", 10);

		// then
		assertThat(result.get(0)).isEqualTo(m1);
	}

	@DisplayName("모든 멤버의 이름을 List<String>으로 조회")
	@Test
	void findUsernameList() {

		// given
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("BBB", 20);
		memberRepository.save(m1);
		memberRepository.save(m2);

		// when
		List<String> usernameList = memberRepository.findUsernameList();

		// then
		assertThat(usernameList).contains(m1.getUsername(), m2.getUsername());
	}

	@DisplayName("모든 멤버의 아이디, 유저네임, 팀 이름 조회")
	@Test
	void findMemberDto() {

		// given
		Team team = new Team("teamA");
		teamRepository.save(team);

		Member m1 = new Member("AAA", 10);
		m1.setTeam(team);
		memberRepository.save(m1);

		// when
		List<MemberDto> memberDto = memberRepository.findMemberDto();

		// then
		assertThat(memberDto.get(0).getId()).isEqualTo(m1.getId());
		assertThat(memberDto.get(0).getUsername()).isEqualTo(m1.getUsername());
		assertThat(memberDto.get(0).getTeamName()).isEqualTo(team.getName());
	}

	@DisplayName("멤버 이름으로 IN절로 검색하여 목록 조회")
	@Test
	void findByNames() {

		// given
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("BBB", 20);
		memberRepository.save(m1);
		memberRepository.save(m2);

		// when
		List<Member> usernameList = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));

		// then
		assertThat(usernameList).contains(m1, m2);
	}

	@DisplayName("멤버 이름으로 IN절로 검색하여 목록 조회")
	@Test
	void findListByUsername() {

		// given
		Member m1 = new Member("AAA", 10);
		Member m2 = new Member("BBB", 20);
		memberRepository.save(m1);
		memberRepository.save(m2);

		// when
		List<Member> memberList = memberRepository.findListByUsername("AAA");

		Member findMember = memberRepository.findMemberByUsername("AAA");

		Optional<Member> optionalMember = memberRepository.findOptionalByUsername("AAA");

		// then
		assertThat(memberList.get(0)).isEqualTo(m1);
		assertThat(findMember).isEqualTo(m1);
		assertThat(optionalMember.get()).isEqualTo(m1);
	}

	@DisplayName("전체 멤버 조회 페이징")
	@Test
	void findPageByAge() {

		// given
		memberRepository.save(new Member("member1", 10));
		memberRepository.save(new Member("member2", 10));
		memberRepository.save(new Member("member3", 10));
		memberRepository.save(new Member("member4", 10));
		memberRepository.save(new Member("member5", 10));

		int age = 10;
		PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

		// when
		Page<Member> page = memberRepository.findPageByAge(age, pageRequest);
		//Page<MemberDto> map = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));

		// then
		assertThat(page.getContent().size()).isEqualTo(3);
		assertThat(page.getTotalElements()).isEqualTo(5);
		assertThat(page.getNumber()).isEqualTo(0);
		assertThat(page.getTotalPages()).isEqualTo(2);
		assertThat(page.isFirst()).isTrue();
		assertThat(page.hasNext()).isTrue();
	}

	@DisplayName("전체 멤버 조회 페이징")
	@Test
	void findSliceByAge() {

		// given
		memberRepository.save(new Member("member1", 10));
		memberRepository.save(new Member("member2", 10));
		memberRepository.save(new Member("member3", 10));
		memberRepository.save(new Member("member4", 10));
		memberRepository.save(new Member("member5", 10));

		int age = 10;
		PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

		// when
		Slice<Member> page = memberRepository.findSliceByAge(age, pageRequest);

		// then
		assertThat(page.getContent().size()).isEqualTo(3);
		assertThat(page.getNumber()).isEqualTo(0);
		assertThat(page.isFirst()).isTrue();
		assertThat(page.hasNext()).isTrue();
	}

	@DisplayName("전체 멤버 조회 페이징")
	@Test
	void findSelfPageByAge() {

		// given
		memberRepository.save(new Member("member1", 10));
		memberRepository.save(new Member("member2", 10));
		memberRepository.save(new Member("member3", 10));
		memberRepository.save(new Member("member4", 10));
		memberRepository.save(new Member("member5", 10));

		int age = 10;
		PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

		// when
		Page<Member> page = memberRepository.findSelfPageByAge(age, pageRequest);

		// then
		assertThat(page.getContent().size()).isEqualTo(3);
		assertThat(page.getTotalElements()).isEqualTo(5);
		assertThat(page.getNumber()).isEqualTo(0);
		assertThat(page.getTotalPages()).isEqualTo(2);
		assertThat(page.isFirst()).isTrue();
		assertThat(page.hasNext()).isTrue();
	}

	@DisplayName("파라미터로 주어진 나이 이상인 모든 멤버의 나이를 +1 한다.")
	@Test
	void bulkAgePlus() {

		// given
		memberRepository.save(new Member("member1", 10));
		memberRepository.save(new Member("member2", 19));
		memberRepository.save(new Member("member3", 20));
		memberRepository.save(new Member("member4", 21));
		memberRepository.save(new Member("member5", 40));

		// when
		int resultCount = memberRepository.bulkAgePlus(20);

		// 벌크 연산 뒤에는 영속성 컨텍스트 초기화 필수! = @Modifying(clearAutomatically = true)
		//em.flush();
		//em.clear();

		// then
		assertThat(resultCount).isEqualTo(3);
	}
	
	@DisplayName("지연로딩 확인")
	@Test
	void findMemberLazy() {
	    
	    // given
	    // member1 -> teamA
	    // member2 -> teamB

		Team teamA = new Team("teamA");
		Team teamB = new Team("teamB");
		teamRepository.save(teamA);
		teamRepository.save(teamB);

		Member member1 = new Member("member1", 10, teamA);
		Member member2 = new Member("member2", 10, teamB);
		memberRepository.save(member1);
		memberRepository.save(member2);

		em.flush();
		em.clear();

	    // when
		List<Member> members = memberRepository.findEntityGraphByUsername("member1");

		// then 지연 로딩 발생 확인
		for(Member member : members) {
			System.out.println("member = " + member.getUsername());
			// 지연 로딩 발생
			System.out.println("member.teamClass = " + member.getTeam().getClass());
			System.out.println("team = " + member.getTeam().getName());
		}
	}

	@DisplayName("JPA Hint")
	@Test
	void queryHint() {

	    // given
		Member member1 = new Member("member1", 10);
		memberRepository.save(member1);
		em.flush();
		em.clear();

	    // when
		Member findMember = memberRepository.findReadOnlyByUsername("member1");
		findMember.setUsername("member2");

		em.flush();
	}

	@DisplayName("JPA Lock")
	@Test
	void lock() {

	    // given
		Member member1 = new Member("member1", 10);
		memberRepository.save(member1);
		em.flush();
		em.clear();

	    // when
		List<Member> result = memberRepository.findLockByUsername("member1");
	}

	@DisplayName("사용자 정의 리포지토리")
	@Test
	void callCustom() {

		// given
	    List<Member> result = memberRepository.findMemberCustom();
	}
}