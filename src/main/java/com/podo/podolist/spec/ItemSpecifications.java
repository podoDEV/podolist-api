package com.podo.podolist.spec;

import com.podo.podolist.entity.ItemEntity;
import com.podo.podolist.model.Item;
import com.podo.podolist.model.PriorityType;
import com.podo.podolist.util.DateTimeUtils;
import org.springframework.data.jpa.domain.Specification;

import java.time.ZonedDateTime;

public class ItemSpecifications {
    private ItemSpecifications() {
    }

    public static Specification<ItemEntity> userIdEqualTo(final Integer userId) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("userId"), userId);
    }

    public static Specification<ItemEntity> dueAtFrom(final ZonedDateTime from) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("dueAt"), from);
    }

    public static Specification<ItemEntity> dueAtTo(final ZonedDateTime to) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.lessThan(root.get("dueAt"), to);
    }

    public static Specification<ItemEntity> completedAtFrom(final ZonedDateTime from) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("completedAt"), from);
    }

    public static Specification<ItemEntity> completedAtTo(final ZonedDateTime to) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.lessThan(root.get("completedAt"), to);
    }

    public static Specification<ItemEntity> startAtFrom(final ZonedDateTime from) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("startedAt"), from);
    }

    public static Specification<ItemEntity> endAtTo(final ZonedDateTime to) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.lessThan(root.get("endedAt"), to);
    }

    public static Specification<ItemEntity> isDelayedEqual(final Boolean isDelayed) {
        ZonedDateTime now = ZonedDateTime.now(DateTimeUtils.getZonedIdOfSeoul());
        ZonedDateTime startTimeOfToday = DateTimeUtils.getStartTimeOfDay(now);
        if (isDelayed) {
            return (root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.lessThan(root.get("dueAt"), startTimeOfToday);
        }
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("dueAt"), startTimeOfToday);
    }

    public static Specification<ItemEntity> isCompletedEqual(final Boolean isCompleted) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isCompleted"), isCompleted);
    }

    public static Specification<ItemEntity> priorityEqualTo(final PriorityType priorityType) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("priorityId"), priorityType.id);
    }
}
