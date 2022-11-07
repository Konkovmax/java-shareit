package ru.practicum.shareit.request;

import lombok.Data;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User requestor;
    private LocalDate created;
}
