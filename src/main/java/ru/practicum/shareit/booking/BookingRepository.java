package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> getBookingsByItem_IdOrderByStart(int itemId);
}
