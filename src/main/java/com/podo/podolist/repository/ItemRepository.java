package com.podo.podolist.repository;

import com.podo.podolist.entity.ItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<ItemEntity, Integer> {
    Optional<ItemEntity> findByItemIdAndUserId(Integer itemId, Integer userId);
    Page<ItemEntity> findByUserId(Integer userId, Pageable pageable);
    List<ItemEntity> findAll(Specification<ItemEntity> spec);
    Page<ItemEntity> findAll(Specification<ItemEntity> spec, Pageable pageable);
}
