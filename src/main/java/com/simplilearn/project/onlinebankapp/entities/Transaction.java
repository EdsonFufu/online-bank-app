package com.simplilearn.project.onlinebankapp.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.TemporalType.TIMESTAMP;

@Entity
@Table(name = "TRANSACTION")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction implements Serializable {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "SOURCE_ACCOUNT")
    private String sourceAccount;

    @Column(name = "DESTINATION_ACCOUNT")
    private String destAccount;

    @Column(name = "AMOUNT")
    private String amount;

    @Column(name = "DR_CR")
    private String drcr; // D Debit C Credit

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "NARRATION")
    private String narration;

    @Column(name = "BALANCE")
    private String balance;

    @CreationTimestamp
    @Temporal(TIMESTAMP)
    @Column(name = "CREATED_DATE")
    protected Date createdDate;

    @UpdateTimestamp
    @Temporal(TIMESTAMP)
    @Column(name = "LAST_MODIFIED_DATE")
    protected Date lastModifiedDate;

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId",referencedColumnName = "id")
    private User user;


    @SneakyThrows
    @Override
    public String toString() {
        return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(this);
    }
}
