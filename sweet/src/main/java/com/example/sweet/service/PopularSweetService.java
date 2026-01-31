package com.example.sweet.service;

import com.example.sweet.model.Sweet;
import com.example.sweet.repository.OrderItemRepository;
import com.example.sweet.repository.SweetRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PopularSweetService {

    private final OrderItemRepository orderItemRepository;
    private final SweetRepository sweetRepository;

    public PopularSweetService(
            OrderItemRepository orderItemRepository,
            SweetRepository sweetRepository
    ) {
        this.orderItemRepository = orderItemRepository;
        this.sweetRepository = sweetRepository;
    }

    public List<Sweet> getPopularSweets(int limit) {

        List<Long> popularIds =
                orderItemRepository.findPopularSweetIds(PageRequest.of(0, limit));

        if (popularIds.isEmpty()) {
            return List.of();
        }

        return sweetRepository.findByIdInAndActiveTrue(popularIds);
    }
}
