package com.simplilearn.project.onlinebankapp.entities;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.TemporalType.TIMESTAMP;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "settings")
public class Settings implements Serializable {
    private static final long serialVersionUID = -1938301799177314446L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String companyName;

    private String accountNumber;

    private String address;

    private String mobile;

    private String withdrawFee;

    @CreationTimestamp
    @Temporal(TIMESTAMP)
    @Column(name = "CREATED_DATE")
    protected Date createdDate;

    @UpdateTimestamp
    @Temporal(TIMESTAMP)
    @Column(name = "LAST_MODIFIED_DATE")
    protected Date lastModifiedDate;
}