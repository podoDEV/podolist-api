package com.podo.podolist.entity;

import com.podo.podolist.model.PriorityType;
import com.podo.podolist.util.DateTimeUtils;
import lombok.*;

import javax.persistence.*;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Optional;

@Entity
@Table(name = "Item")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ItemEntity {
    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int itemId;
    @Column
    private String title;
    @Column(name = "is_completed")
    private Boolean isCompleted;
    @Column(name = "completed_at")
    private ZonedDateTime completedAt;
    @Column(name = "started_at")
    private ZonedDateTime startedAt;
    @Column(name = "ended_at")
    private ZonedDateTime endedAt;
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;
    @Column(name = "created_at")
    private ZonedDateTime createdAt;
    @Column(name = "due_at")
    private ZonedDateTime dueAt;
    @Column(name = "priority_id")
    private Integer priorityId;
    @Column(name = "user_id")
    private Integer userId;

    @PrePersist
    protected void onCreate() {
        ZonedDateTime now = ZonedDateTime.now(DateTimeUtils.getZonedIdOfSeoul());

        updatedAt = createdAt = now;

        // set default value
        if (isCompleted == null) {
            isCompleted = false;
        }
        if (dueAt == null) {
            dueAt = ZonedDateTime.of(
                    now.toLocalDate(),
                    LocalTime.of(23, 59, 59, 999999999),
                    DateTimeUtils.getZonedIdOfSeoul()
            );
        }
        if (priorityId == null) {
            priorityId = PriorityType.NONE.id;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        ZonedDateTime now = ZonedDateTime.now(DateTimeUtils.getZonedIdOfSeoul());
        updatedAt = now;
        if (isCompleted) {
            completedAt = now;
        } else {
            completedAt = null;
        }
    }

    public PriorityType getPriority() {
        return Optional.ofNullable(PriorityType.valueOf(priorityId))
                .orElse(PriorityType.NONE);
    }

    public void setPriority(PriorityType priority) {
        if (Objects.isNull(priority)) {
            throw new NullPointerException("priority should not be null");
        }
        priorityId = priority.id;
    }
}
