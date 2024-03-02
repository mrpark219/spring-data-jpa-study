package springdatajpa.study.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import springdatajpa.study.entity.Item;

@SpringBootTest
class ItemRepositoryTest {

	@Autowired
	ItemRepository itemRepository;

	@DisplayName("")
	@Test
	void save() {
		Item item = new Item("A");
		itemRepository.save(item);
	}
}