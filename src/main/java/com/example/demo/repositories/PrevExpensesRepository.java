package com.example.demo.repositories;
import com.example.demo.entities.ExpenseEntity;
import com.example.demo.entities.ExpensePrevDataEntity;
import com.example.demo.enums.ExpenseFrequencyTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.stream.Stream;

public interface PrevExpensesRepository extends JpaRepository<ExpensePrevDataEntity, Long> {
    @Query("SELECT e FROM ExpensePrevDataEntity e WHERE e.expense.id = :espenseId")
    Stream<ExpensePrevDataEntity> streamAllByExpenseId(long expenseId);

}
