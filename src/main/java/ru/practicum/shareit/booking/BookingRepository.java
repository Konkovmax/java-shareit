package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer>{

//    @Query(" select i from bookings i " +
//            "where i.item_id = ?1")
    List<Booking> getBookingsByItem_IdOrderByStart(int itemId);
}
