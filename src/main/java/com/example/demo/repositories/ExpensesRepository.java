package com.example.demo.repositories;
import com.example.demo.entities.ExpenseEntity;
import com.example.demo.enums.ExpenseFrequencyTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.stream.Stream;
public interface ExpensesRepository extends JpaRepository<ExpenseEntity, Long> {
    @Query("SELECT e FROM ExpenseEntity e")
    Stream<ExpenseEntity> streamAll();

    @Query("SELECT e FROM ExpenseEntity e WHERE e.type = :type")
    Stream<ExpenseEntity> streamAllByType(ExpenseFrequencyTypes type);
}
