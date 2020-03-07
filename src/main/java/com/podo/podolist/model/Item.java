package com.podo.podolist.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.podo.podolist.entity.ItemEntity;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.springframework.util.Assert;

import java.time.ZonedDateTime;
import java.util.Optional;

@Builder
@Getter
@ToString
public class Item {
    private int itemId;
    private String title;
    private Boolean isCompleted;
    private ZonedDateTime completedAt;
    private ZonedDateTime startedAt;
    private ZonedDateTime endedAt;
    private ZonedDateTime updatedAt;
    private ZonedDateTime createdAt;
    private ZonedDateTime dueAt;
    private PriorityType priority;

    public static Item from(ItemEntity entity) {
        Assert.notNull(entity, "itemEntity should not be null");

        return Item.builder()
                .itemId(entity.getItemId())
                .title(entity.getTitle())
                .isCompleted(entity.getIsCompleted())
                .completedAt(entity.getCompletedAt())
                .startedAt(entity.getStartedAt())
                .endedAt(entity.getEndedAt())
                .updatedAt(entity.getUpdatedAt())
                .createdAt(entity.getCreatedAt())
                .dueAt(entity.getDueAt())
                .priority(entity.getPriority())
                .build();
    }

    public ItemEntity toItemEntity(int userId) {
        return ItemEntity.builder()
                .title(title)
                .isCompleted(isCompleted)
                .completedAt(completedAt)
                .startedAt(startedAt)
                .endedAt(endedAt)
                .updatedAt(updatedAt)
                .createdAt(createdAt)
                .dueAt(dueAt)
                .priorityId(Optional.ofNullable(priority)
                                    .map(o -> o.id)
                                    .orElse(null))
                .userId(userId)
                .build();
    }

    @ApiModel(value = "ItemRequest")
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        private String title;
        private Boolean isCompleted;
        @JsonFormat(shape = JsonFormat.Shape.NUMBER)
        private ZonedDateTime completedAt;
        @JsonFormat(shape = JsonFormat.Shape.NUMBER)
        private ZonedDateTime startedAt;
        @JsonFormat(shape = JsonFormat.Shape.NUMBER)
        private ZonedDateTime endedAt;
        @JsonFormat(shape = JsonFormat.Shape.NUMBER)
        private ZonedDateTime createdAt;
        @JsonFormat(shape = JsonFormat.Shape.NUMBER)
        private ZonedDateTime dueAt;
        @JsonFormat(shape = JsonFormat.Shape.NUMBER)
        private ZonedDateTime updatedAt;
        private PriorityType priority;

        public Item toItem() {
            return Item.builder()
                    .title(title)
                    .isCompleted(isCompleted)
                    .completedAt(completedAt)
                    .startedAt(startedAt)
                    .endedAt(endedAt)
                    .updatedAt(updatedAt)
                    .createdAt(createdAt)
                    .dueAt(dueAt)
                    .priority(priority)
                    .build();
        }
    }

    @ApiModel(value = "ItemResponse")
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Response {
        private int id;
        private String title;
        private Boolean isCompleted;
        @JsonFormat(shape = JsonFormat.Shape.NUMBER)
        private ZonedDateTime completedAt;
        @JsonFormat(shape = JsonFormat.Shape.NUMBER)
        private ZonedDateTime startedAt;
        @JsonFormat(shape = JsonFormat.Shape.NUMBER)
        private ZonedDateTime endedAt;
        @JsonFormat(shape = JsonFormat.Shape.NUMBER)
        private ZonedDateTime updatedAt;
        @JsonFormat(shape = JsonFormat.Shape.NUMBER)
        private ZonedDateTime createdAt;
        @JsonFormat(shape = JsonFormat.Shape.NUMBER)
        private ZonedDateTime dueAt;
        private PriorityType priority;

        public static Item.Response from(Item item) {
            Assert.notNull(item, "item should not be null");

            return Response.builder()
                    .id(item.getItemId())
                    .title(item.getTitle())
                    .isCompleted(item.getIsCompleted())
                    .completedAt(item.getCompletedAt())
                    .startedAt(item.getStartedAt())
                    .endedAt(item.getEndedAt())
                    .updatedAt(item.getUpdatedAt())
                    .createdAt(item.getCreatedAt())
                    .dueAt(item.getDueAt())
                    .priority(item.getPriority())
                    .build();
        }
    }
}
