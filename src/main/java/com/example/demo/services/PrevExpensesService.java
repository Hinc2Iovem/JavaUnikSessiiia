package com.example.demo.services;

import com.example.demo.entities.ExpensePrevDataEntity;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.repositories.PrevExpensesRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PrevExpensesService {

    @Autowired
    PrevExpensesRepository prevExpensesRepository;

    @Transactional(readOnly = true)
    public List<ExpensePrevDataEntity> getExpenses(Long expenseId) {
        return prevExpensesRepository.streamAllByExpenseId(expenseId).collect(Collectors.toList());
    }

    public void deletePrevExpense(Long prevExpenseId) throws NotFoundException {
        if(prevExpensesRepository.findById(prevExpenseId).isEmpty()){
            throw new NotFoundException("PrevExpense with such id wasn't found");
        }

        prevExpensesRepository.deleteById(prevExpenseId);
    }
}
