package com.podo.podolist.model.dto;

import com.podo.podolist.model.Item;
import com.podo.podolist.service.ItemService;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@ToString
public class FutureItemResponse implements MainItemResponse {
    private List<Item.Response> delayedItems;
    private List<Item.Response> items;

    public static FutureItemResponse of(ItemService itemService, ZonedDateTime date, Integer userId) {
        Assert.notNull(itemService, "itemService should not be null");
        Assert.notNull(itemService, "date should not be null");
        Assert.notNull(itemService, "userId should not be null");

        return FutureItemResponse.builder()
                .delayedItems(Collections.emptyList())
                .items(itemService.getUndelayedItems(date, userId).stream()
                        .map(Item.Response::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
