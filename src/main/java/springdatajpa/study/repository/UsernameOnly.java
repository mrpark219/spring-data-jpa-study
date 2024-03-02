package springdatajpa.study.repository;

public interface UsernameOnly {

	//@Value("#{target.username + ' ' + target.age}")
	String getUsername();
}
