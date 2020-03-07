package com.podo.podolist.controller;

import com.podo.podolist.exception.NoContentException;
import com.podo.podolist.model.Item;
import com.podo.podolist.model.PriorityType;
import com.podo.podolist.model.dto.FutureItemResponse;
import com.podo.podolist.model.dto.MainItemResponse;
import com.podo.podolist.model.dto.PastItemResponse;
import com.podo.podolist.model.dto.PresentItemResponse;
import com.podo.podolist.repository.ItemRepository;
import com.podo.podolist.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static com.podo.podolist.util.DateTimeUtils.*;
import static java.time.ZonedDateTime.now;

@Slf4j
@RestController
@CrossOrigin(
        origins = {"*"},
        allowCredentials = "true"
)
@RequiredArgsConstructor
public class ItemController implements Authenticated {

    private final ItemService itemService;
    private final ItemRepository itemRepository;

    @GetMapping(value = {
            "/v1/items",
            "/items"
    })
    public MainItemResponse getMainItem(@ApiIgnore @ModelAttribute("userId") Integer userId,
                                        @RequestParam(name = "date", required = false, defaultValue = "") String dateString) {
        ZonedDateTime date = parseDate(dateString);
        ZonedDateTime now = now(getZonedIdOfSeoul());
        ZonedDateTime startOfToday = getStartTimeOfDay(now);
        ZonedDateTime endOfToday = getEndTimeOfDay(now);
        log.debug("endOfToday: " + endOfToday);

        if (date.isBefore(startOfToday)) {
            log.debug("isBefore");
            return PastItemResponse.of(itemService, date, userId);
        }
        if (date.isAfter(endOfToday)) {
            log.debug("isAfter");
            return FutureItemResponse.of(itemService, date, userId);
        }
        log.debug("isToday");
        return PresentItemResponse.of(itemService, date, userId);
    }

    @GetMapping(value = {
            "/v1/items/{itemId:\\d+}",
            "/items/{itemId:\\d+}"
    })
    public Item.Response getItem(@PathVariable int itemId,
                                 @ApiIgnore @ModelAttribute("userId") Integer userId) {
        return itemService.getItem(itemId, userId)
                .map(Item.Response::from)
                .orElseThrow(NoContentException::new);
    }

    @GetMapping(value = {
            "/v1/items/priority",
            "/items/priority"
    })
    public Map<Long, PriorityType> getImportantItems(@ApiIgnore @ModelAttribute("userId") Integer userId,
                                                     @RequestParam Long from,
                                                     @RequestParam Long to) {
        // +09:00
        ZonedDateTime startAtFrom = getZonedDateTimeFromEpochSecond(from);
        ZonedDateTime endAtTo = getZonedDateTimeFromEpochSecond(to);
        return itemService.getImportantItems(userId, startAtFrom, endAtTo).stream()
                .collect(Collectors.toMap(
                        item -> getStartTimeOfDay(item.getStartedAt()).toInstant().toEpochMilli(),
                        Item::getPriority,
                        (a, b) -> a.id < b.id ? a : b,
                        TreeMap::new
                ));
    }

    @PostMapping(value = {
            "/v1/items",
            "/items"
    })
    @ResponseStatus(HttpStatus.CREATED)
    public Item.Response createItem(@RequestBody Item.Request itemRequest,
                                    @ApiIgnore @ModelAttribute("userId") Integer userId) {
        return Item.Response.from(itemService.createItem(itemRequest.toItem(), userId));
    }

    @PutMapping(value = {
            "/v1/items/{itemId:\\d+}",
            "/items/{itemId:\\d+}"
    })
    public Item.Response updateItem(@PathVariable int itemId,
                                    @RequestBody Item.Request itemRequest,
                                    @ApiIgnore @ModelAttribute("userId") Integer userId) {
        return Item.Response.from(itemService.updateItem(itemId, itemRequest.toItem(), userId));
    }

    @DeleteMapping(value = {
            "/v1/items/{itemId:\\d+}",
            "/items/{itemId:\\d+}"
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(@PathVariable int itemId,
                           @ApiIgnore @ModelAttribute("userId") Integer userId) {
        itemService.deleteItem(itemId, userId);
    }

}
