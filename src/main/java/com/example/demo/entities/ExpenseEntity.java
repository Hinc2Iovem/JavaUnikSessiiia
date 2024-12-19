package com.example.demo.entities;

import com.example.demo.enums.ExpenseFrequencyTypes;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "expense")
public class ExpenseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    @Enumerated(EnumType.STRING)
    ExpenseFrequencyTypes type;

    @Column(name = "spent_amount", nullable = false)
    BigDecimal spentAmount;

    @Column(name = "spent_at", nullable = false)
    Instant spentAt;

    @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ExpensePrevDataEntity> prevExpenses = new HashSet<>();


    public ExpenseEntity() {
    }

    public ExpenseEntity(Long id, String name, ExpenseFrequencyTypes type, BigDecimal spentAmount, Instant spentAt, Set<ExpensePrevDataEntity> prevExpenses) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.spentAmount = spentAmount;
        this.spentAt = spentAt;
        this.prevExpenses = prevExpenses;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ExpenseFrequencyTypes getType() {
        return type;
    }

    public void setType(ExpenseFrequencyTypes type) {
        this.type = type;
    }

    public BigDecimal getSpentAmount() {
        return spentAmount;
    }

    public void setSpentAmount(BigDecimal spentAmount) {
        this.spentAmount = spentAmount;
    }

    public Instant getSpentAt() {
        return spentAt;
    }

    public void setSpentAt(Instant spentAt) {
        this.spentAt = spentAt;
    }

    public Set<ExpensePrevDataEntity> getPrevExpenses() {
        return prevExpenses;
    }

    public void setPrevExpenses(Set<ExpensePrevDataEntity> prevExpenses) {
        this.prevExpenses = prevExpenses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExpenseEntity that = (ExpenseEntity) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ExpenseEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", spentAmount=" + spentAmount +
                ", spentAt=" + spentAt +
                '}';
    }
}