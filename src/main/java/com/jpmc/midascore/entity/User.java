package com.jpmc.midascore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private float balance;

//    @OneToMany(mappedBy = "sender")
//    @JsonIgnore
//    private List<TransactionRecord> sentTransactions;
//
//    @OneToMany(mappedBy = "recipient")
//    @JsonIgnore
//    private List<TransactionRecord> receivedTransactions;

    protected User() {
    }

    public User(String name, float balance) {
        this.username = name;
        this.balance = balance;
    }

    @Override
    public String toString() {
        return String.format("User[id=%d, name='%s', balance='%f'", id, username, balance);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

//    public List<TransactionRecord> getSentTransactions() {
//        return sentTransactions;
//    }
//
//    public void setSentTransactions(List<TransactionRecord> sentTransactions) {
//        this.sentTransactions = sentTransactions;
//    }
//
//    public List<TransactionRecord> getReceivedTransactions() {
//        return receivedTransactions;
//    }
//
//    public void setReceivedTransactions(List<TransactionRecord> receivedTransactions) {
//        this.receivedTransactions = receivedTransactions;
//    }
}
