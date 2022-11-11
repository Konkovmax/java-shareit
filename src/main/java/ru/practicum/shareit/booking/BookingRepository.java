package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> getBookingsByItem_IdOrderByStart(int itemId);

    // не получилось сделать запрос для Status_ApprovedIs говорит no property found for type и мои ухищрения не помогли
    // и в интернетах что-то не нашёл, там все через Query фигачат
    @Query(" select b from Booking b " +
            "where b.item.id = ?1 and b.booker.id = ?2 and b.status = 'APPROVED' and b.end < ?3")
    List<Booking> getBookingsByItem_IdAndBooker_IdAndStatus_ApprovedIs(int itemId, int bookerId, LocalDateTime now);

    List<Booking> getBookingByBooker_Id(int userId);

    @Query(" select b from Booking b " +
            "where b.item.owner.id = ?1")
    List<Booking> getBookingByOwner_Id(int userId);
}
