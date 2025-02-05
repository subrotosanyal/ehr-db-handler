package net.sanyal.ehr.model.common;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import lombok.Data;

@Data
public abstract class BaseEntity {
    @CreatedDate
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private String source;
}