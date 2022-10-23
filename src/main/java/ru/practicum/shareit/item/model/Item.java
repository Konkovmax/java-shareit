package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class Item {
   private int id;

   @NotEmpty
   private String name;

   @NotEmpty
   private String description;

   @NotNull
   private Boolean available;
   private User owner;
   private ItemRequest request;
}
