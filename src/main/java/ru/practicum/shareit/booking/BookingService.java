package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingIncomeDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface BookingService {

   BookingDto create(BookingIncomeDto booking, int userId);

//    List<ItemDto> getAll(int userId);
//
//    List<ItemDto> search(String query);
//
//    ItemDto update(int id, ItemDto item, int userId);
//
//     ItemDto getItem(int itemId);

}
