package springdatajpa.study.repository;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import springdatajpa.study.entity.Member;

public class MemberSpec {

	public static Specification<Member> teamName(final String teamName) {
		return (Specification<Member>) (root, query, builder) -> {

			if(StringUtils.isEmpty(teamName)) {
				return null;
			}

			// 회원과 조인
			Join<Object, Object> t = root.join("team", JoinType.INNER);
			return builder.equal(t.get("name"), teamName);
		};
	}

	public static Specification<Member> username(final String username) {
		return (root, query, builder) ->
			builder.equal(root.get("username"), username);
	}
}
