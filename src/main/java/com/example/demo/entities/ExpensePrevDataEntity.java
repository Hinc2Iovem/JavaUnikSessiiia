package com.example.demo.entities;

import com.example.demo.enums.ExpenseFrequencyTypes;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "expense_prev_data")
public class ExpensePrevDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "prev_name", nullable = false)
    String prevName;

    @Enumerated(EnumType.STRING)
    @Column(name = "prev_type", nullable = false)
    ExpenseFrequencyTypes prevType;

    @Column(name = "prev_spent_amount", nullable = false)
    BigDecimal prevSpentAmount;

    @Column(name = "prev_spent_at", nullable = false)
    Instant prevSpentAt;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "expense_id")
    private ExpenseEntity expense;

    public ExpensePrevDataEntity() {
    }

    public ExpensePrevDataEntity(Long id, String prevName, ExpenseFrequencyTypes prevType, BigDecimal prevSpentAmount, Instant prevSpentAt, ExpenseEntity expense) {
        this.id = id;
        this.prevName = prevName;
        this.prevType = prevType;
        this.prevSpentAmount = prevSpentAmount;
        this.prevSpentAt = prevSpentAt;
        this.expense = expense;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrevName() {
        return prevName;
    }

    public void setPrevName(String prevName) {
        this.prevName = prevName;
    }

    public ExpenseFrequencyTypes getPrevType() {
        return prevType;
    }

    public void setPrevType(ExpenseFrequencyTypes prevType) {
        this.prevType = prevType;
    }

    public BigDecimal getPrevSpentAmount() {
        return prevSpentAmount;
    }

    public void setPrevSpentAmount(BigDecimal prevSpentAmount) {
        this.prevSpentAmount = prevSpentAmount;
    }

    public Instant getPrevSpentAt() {
        return prevSpentAt;
    }

    public void setPrevSpentAt(Instant prevSpentAt) {
        this.prevSpentAt = prevSpentAt;
    }

    public ExpenseEntity getExpense() {
        return expense;
    }

    public void setExpense(ExpenseEntity expense) {
        this.expense = expense;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExpensePrevDataEntity that = (ExpensePrevDataEntity) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ExpensePrevDataEntity{" +
                "id=" + id +
                ", prevName='" + prevName + '\'' +
                ", prevType=" + prevType +
                ", prevSpentAmount=" + prevSpentAmount +
                ", prevSpentAt=" + prevSpentAt +
                '}';
    }
}