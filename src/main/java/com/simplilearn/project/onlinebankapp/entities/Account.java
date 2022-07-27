package com.simplilearn.project.onlinebankapp.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "ACCOUNT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account implements Serializable {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "ACCOUNT_NAME")
    private String accountName;

    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;

    @Builder.Default
    @Column(name = "CURRENCY")
    private String currency = "TZS";

    @Builder.Default
    @Column(name = "ACCOUNT_BALANCE")
    private double balance = 0.00;

    @Builder.Default
    @Column(name = "ACCOUNT_TYPE")
    private String accountType = "savings";  // savings or currency

    @Column(name = "ACCOUNT_STATUS")
    private boolean status = true;

    @CreatedDate
    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date createdDate;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    @Column(name = "LAST_MODIFIED_DATE")
    private Date lastModifiedDate;

    @JsonBackReference
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ID",referencedColumnName = "ID")
    private User user;

    public String getFormatedBalance(){
        return String.format("TZS %,.2f",balance);
    }

    @SneakyThrows
    @Override
    public String toString() {
        return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(this);
    }
}
