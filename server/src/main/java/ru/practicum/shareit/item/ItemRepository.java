package ru.practicum.shareit.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    @Query(" SELECT i FROM Item i " +
            "WHERE (upper(i.name) LIKE upper(concat('%', ?1, '%')) " +
            " OR upper(i.description) LIKE upper(concat('%', ?1, '%')))" +
            "AND i.available IS true ")
    Page<Item> search(String query, Pageable pageable);

    Page<Item> getItemByOwner_Id(int ownerId, Pageable pageable);

    List<Item> getItemByRequest_Id(int requestId);
}
