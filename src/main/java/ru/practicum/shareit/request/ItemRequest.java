package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User requestor;
    private LocalDate created;
}
