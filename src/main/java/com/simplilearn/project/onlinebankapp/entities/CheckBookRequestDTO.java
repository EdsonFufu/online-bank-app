package com.simplilearn.project.onlinebankapp.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CheckBookRequestDTO {

    private Long id;

    private String customerName;

    private boolean approved;

    private boolean collected;

    private String createdDate;

    private String collectedDate;
}