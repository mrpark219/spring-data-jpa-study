package study.springdatajpastudy.repository;

import study.springdatajpastudy.entity.Member;

import java.util.List;

public interface MemberRepositoryCustom {

	List<Member> findMemberCustom();
}
