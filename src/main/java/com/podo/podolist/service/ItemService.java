package com.podo.podolist.service;

import com.podo.podolist.model.Item;
import org.springframework.data.domain.Pageable;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface ItemService {
    List<Item> getItems(Pageable pageable, int userId);
    List<Item> getDelayedItems(ZonedDateTime zonedDateTime, int userId);
    List<Item> getUndelayedItems(ZonedDateTime zonedDateTime, int userId);
    Optional<Item> getItem(int itemId, int userId);
    List<Item> getImportantItems(int userId, ZonedDateTime startAtFrom, ZonedDateTime endAtTo);
    List<Item> getCompletedItems(int userId, ZonedDateTime from, ZonedDateTime to);
    Item createItem(Item item, int userId);
    Item updateItem(int itemId, Item item, int userId);
    void deleteItem(int itemId, int userId);
}
