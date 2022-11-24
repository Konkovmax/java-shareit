package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Integer> {

//    @Query(" select i from Item i " +
//            "where (upper(i.name) like upper(concat('%', ?1, '%')) " +
//            " or upper(i.description) like upper(concat('%', ?1, '%')))" +
//            "and i.available is true ")
//    List<Item> search(String query);

    List<ItemRequest> getItemRequestByRequester_Id(int requesterId);

    List<ItemRequest> findItemRequestByRequester_IdNot(int requesterId);
}
