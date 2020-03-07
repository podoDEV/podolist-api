package com.podo.podolist.service;

import com.podo.podolist.entity.ItemEntity;
import com.podo.podolist.exception.ResourceNotFoundException;
import com.podo.podolist.model.Item;
import com.podo.podolist.repository.ItemRepository;
import com.podo.podolist.spec.ItemSpecifications;
import com.podo.podolist.util.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    @Override
    public List<Item> getItems(Pageable pageable, int userId) {
        return itemRepository.findByUserId(userId, pageable)
                .getContent()
                .stream()
                .map(Item::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> getDelayedItems(ZonedDateTime zonedDateTime, int userId) {
        Specification<ItemEntity> specs = Specification.where(ItemSpecifications.userIdEqualTo(userId))
                .and(ItemSpecifications.isDelayedEqual(true))
                .and(ItemSpecifications.isCompletedEqual(false));
        return itemRepository.findAll(specs).stream()
                .map(Item::from)
                .collect(Collectors.toList());
    }

    /**
     * 오늘 완료한 일
     * 완료하지 않았지만, 오늘 마감인 일
     */
    @Override
    public List<Item> getUndelayedItems(ZonedDateTime zonedDateTime, int userId) {
        ZonedDateTime from = DateTimeUtils.getStartTimeOfDay(zonedDateTime);
        ZonedDateTime to = DateTimeUtils.getEndTimeOfDay(zonedDateTime);

        Specification<ItemEntity> specs = Specification.where(ItemSpecifications.userIdEqualTo(userId))
                .and(ItemSpecifications.isCompletedEqual(false))
                .and(ItemSpecifications.dueAtFrom(from))
                .and(ItemSpecifications.dueAtTo(to))
                .and(ItemSpecifications.isDelayedEqual(false));
        List<ItemEntity> uncompletedItems = itemRepository.findAll(specs);

        Specification<ItemEntity> specification = Specification.where(ItemSpecifications.userIdEqualTo(userId))
                .and(ItemSpecifications.isCompletedEqual(true))
                .and(ItemSpecifications.completedAtFrom(from))
                .and(ItemSpecifications.completedAtTo(to));
        List<ItemEntity> completedItems = itemRepository.findAll(specification);

        List<ItemEntity> results = new ArrayList<>(uncompletedItems);
        results.addAll(completedItems);
        results.sort(Comparator.comparingInt(ItemEntity::getItemId));

        return results.stream()
                .map(Item::from)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Item> getItem(int itemId, int userId) {
        return itemRepository.findByItemIdAndUserId(itemId, userId)
                .map(Item::from);
    }

    @Override
    public List<Item> getImportantItems(int userId, ZonedDateTime from, ZonedDateTime to) {
        Specification<ItemEntity> specs = Specification.where(ItemSpecifications.userIdEqualTo(userId))
                .and(Specification.where(ItemSpecifications.startAtFrom(from))
                        .and(Specification.where(ItemSpecifications.endAtTo(to))));

        return itemRepository.findAll(specs).stream()
                .map(Item::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> getCompletedItems(int userId, ZonedDateTime from, ZonedDateTime to) {
        Specification<ItemEntity> specs = Specification.where(ItemSpecifications.userIdEqualTo(userId))
                .and(ItemSpecifications.completedAtFrom(from))
                .and(ItemSpecifications.completedAtTo(to));
        return itemRepository.findAll(specs).stream()
                .map(Item::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Item createItem(Item item, int userId) {
        ItemEntity itemEntity = item.toItemEntity(userId);
        itemRepository.save(itemEntity);
        return Item.from(itemRepository.save(itemEntity));
    }

    @Override
    @Transactional
    public Item updateItem(int itemId, Item item, int userId) {
        ItemEntity itemEntity = itemRepository.findByItemIdAndUserId(itemId, userId)
                .orElseThrow(ResourceNotFoundException::new);
        Optional.ofNullable(item.getTitle())
                .ifPresent(itemEntity::setTitle);
        Optional.ofNullable(item.getIsCompleted())
                .ifPresent(itemEntity::setIsCompleted);
        Optional.ofNullable(item.getStartedAt())
                .ifPresent(itemEntity::setStartedAt);
        Optional.ofNullable(item.getEndedAt())
                .ifPresent(itemEntity::setEndedAt);
        Optional.ofNullable(item.getCreatedAt())
                .ifPresent(itemEntity::setCreatedAt);
        Optional.ofNullable(item.getDueAt())
                .ifPresent(itemEntity::setDueAt);
        Optional.ofNullable(item.getPriority())
                .ifPresent(itemEntity::setPriority);
        itemRepository.save(itemEntity);
        return Item.from(itemEntity);
    }

    @Override
    @Transactional
    public void deleteItem(int itemId, int userId) {
        if (!itemRepository.findByItemIdAndUserId(itemId, userId).isPresent()) {
            return;
        }
        itemRepository.deleteById(itemId);
    }
}
