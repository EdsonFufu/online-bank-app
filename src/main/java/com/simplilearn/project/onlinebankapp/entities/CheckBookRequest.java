package com.simplilearn.project.onlinebankapp.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
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
@ToString
@Entity
@Table(name = "CHECK_BOOK_REQUEST")
public class CheckBookRequest implements Serializable {
    private static final long serialVersionUID = 4683943811765967953L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private boolean approved;

    @Builder.Default
    private boolean collected = false;


    @Builder.Default
    @Column(name = "COLLECTED_DATE")
    private String collectedDate = "";

    @JsonManagedReference
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customerId",referencedColumnName = "id")
    private User user;

    @CreatedDate
    @Basic(optional = false)
    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdDate;

    @LastModifiedDate
    @Basic(optional = false)
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    @Column(name = "LAST_MODIFIED_DATE")
    private Date lastModifiedDate;
}