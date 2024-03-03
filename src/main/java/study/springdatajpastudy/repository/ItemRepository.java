package study.springdatajpastudy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.springdatajpastudy.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
