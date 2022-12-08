package ru.practicum.shareit.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Integer> {

    List<ItemRequest> getItemRequestByRequester_Id(int requesterId);

    Page<ItemRequest> findItemRequestByRequester_IdNot(int requesterId, Pageable pageable);
}
