package springdatajpa.study.repository;

import springdatajpa.study.entity.Member;

import java.util.List;

public interface MemberRepositoryCustom {

	List<Member> findMemberCustom();
}
