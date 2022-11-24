package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDateDto;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ItemDto {
    private int id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    @NotNull
    private Boolean available;
    private User owner;
    private int requestId;
//    private ItemRequest request;
    private BookingDateDto lastBooking;
    private BookingDateDto nextBooking;
    private List<CommentDto> comments;

}
