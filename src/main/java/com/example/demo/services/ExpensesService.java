package com.example.demo.services;

import com.example.demo.entities.ExpenseEntity;
import com.example.demo.entities.ExpensePrevDataEntity;
import com.example.demo.enums.ExpenseFrequencyTypes;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.MissingCredentialsException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.repositories.ExpensesRepository;
import com.example.demo.repositories.PrevExpensesRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExpensesService {

    @Autowired
    ExpensesRepository expensesRepository;
    @Autowired
    PrevExpensesRepository prevExpensesRepository;

    @Transactional(readOnly = true)
    public List<ExpenseEntity> getExpenses(Optional<ExpenseFrequencyTypes> type) {
        if(type.isEmpty()){
            return expensesRepository.streamAll().collect(Collectors.toList());
        }
        return expensesRepository.streamAllByType(type).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ExpenseEntity> getExpensesForToday() {
        Stream<ExpenseEntity> expenses = expensesRepository.streamAll();
        LocalDate todayDate = LocalDate.now(ZoneOffset.UTC);
        Instant startOfDay = todayDate.atStartOfDay().toInstant(ZoneOffset.UTC);

        return expenses.filter(e -> e.getSpentAt().isAfter(startOfDay)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<ExpenseEntity> getExpenseById(Long expenseId) {
        return expensesRepository.findById(expenseId);
    }

    public ExpenseEntity createExpense(ExpenseEntity expenseBody) throws MissingCredentialsException, BadRequestException {
        if(expenseBody.getName().trim().isEmpty()
                || expenseBody.getType() == null
                || expenseBody.getSpentAmount() == null
                || expenseBody.getSpentAt() == null
        ){
            throw new MissingCredentialsException("name, type, spentAt and spentAmount are required");
        }

        if(expenseBody.getSpentAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("spent amount can not be less than 0");
        }

        ExpenseEntity currentExpense = new ExpenseEntity();

        currentExpense.setSpentAmount(expenseBody.getSpentAmount());
        currentExpense.setSpentAt(expenseBody.getSpentAt());
        currentExpense.setName(expenseBody.getName());
        currentExpense.setType(expenseBody.getType());
        return expensesRepository.save(currentExpense);
    }


    public ExpenseEntity updateExpense(
            ExpenseEntity expenseBody,
            Long expenseId,
            Boolean savePrev
    ) throws NotFoundException, BadRequestException {
        ExpenseEntity existingExpense = expensesRepository.findById(expenseId)
                .orElseThrow(() -> new NotFoundException("Expense with such id doesn't exist"));

        if(savePrev){
            ExpensePrevDataEntity expensePrevDataEntity = new ExpensePrevDataEntity();
            expensePrevDataEntity.setPrevName(existingExpense.getName());
            expensePrevDataEntity.setPrevType(existingExpense.getType());
            expensePrevDataEntity.setPrevSpentAt(existingExpense.getSpentAt());
            expensePrevDataEntity.setPrevSpentAmount(existingExpense.getSpentAmount());
            expensePrevDataEntity.setExpense(existingExpense);
            prevExpensesRepository.save(expensePrevDataEntity);
        }

        if(expenseBody.getName() != null && !expenseBody.getName().trim().isEmpty()){
            existingExpense.setName(expenseBody.getName());
        }

        if(expenseBody.getSpentAmount() != null){
            if(expenseBody.getSpentAmount().compareTo(BigDecimal.ZERO) < 0) {
                throw new BadRequestException("spent amount can not be less than 0");
            }
            existingExpense.setSpentAmount(expenseBody.getSpentAmount());
        }

        if(expenseBody.getType() != null){
            existingExpense.setType(expenseBody.getType());
        }

        if(expenseBody.getSpentAt() != null){
            existingExpense.setSpentAt(expenseBody.getSpentAt());
        }

        return expensesRepository.save(existingExpense);
    }


    public void deleteExpense(Long expenseId) throws NotFoundException {
        if(expensesRepository.findById(expenseId).isEmpty()){
            throw new NotFoundException("Expense with such id wasn't found");
        }

        expensesRepository.deleteById(expenseId);
    }
}
