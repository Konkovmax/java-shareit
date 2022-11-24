package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> getBookingsByItem_IdOrderByStart(int itemId);

    @Query(" select b from Booking b " +
            "where b.item.id = ?1 and b.booker.id = ?2 and b.status = 'APPROVED' and b.end < ?3")
    List<Booking> getBookingsByItem_IdAndBooker_IdAndStatus_ApprovedIs(int itemId, int bookerId, LocalDateTime now);

    Page<Booking> getBookingByBooker_Id(int userId, Pageable pageable);

    @Query(" select b from Booking b " +
            "where b.item.owner.id = ?1")
    Page<Booking> getBookingByOwner_Id(int userId, Pageable pageable);
}
