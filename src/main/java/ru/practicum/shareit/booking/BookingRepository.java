package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer>{

//    @Query(" select i from Item i " +
//            "where (upper(i.name) like upper(concat('%', ?1, '%')) " +
//            " or upper(i.description) like upper(concat('%', ?1, '%')))" +
//            "and i.available is true ")
//    List<Item> search(String query);
}
