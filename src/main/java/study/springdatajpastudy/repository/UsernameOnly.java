package study.springdatajpastudy.repository;

public interface UsernameOnly {

	//@Value("#{target.username + ' ' + target.age}")
	String getUsername();
}
