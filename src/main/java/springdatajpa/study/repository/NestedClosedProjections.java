package springdatajpa.study.repository;

public interface NestedClosedProjections {

	String getUsername();

	TeamInfo getTeam();

	interface TeamInfo {
		String getName();
	}
}
