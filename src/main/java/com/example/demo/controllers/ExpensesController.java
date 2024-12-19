package com.example.demo.controllers;

import com.example.demo.entities.ExpenseEntity;
import com.example.demo.enums.ExpenseFrequencyTypes;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.services.ExpensesService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping("/api/v1/expenses")
public class ExpensesController {
    @Autowired
    ExpensesService expensesService;

    public static final String SINGLE_EXPENSE = "/{expenseId}";
    public static final String EXPENSES_TODAY = "/today";

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<List<ExpenseEntity>> getExpenses(
        @RequestParam Optional<ExpenseFrequencyTypes> type
    )  throws BadRequestException {
        try {
            List<ExpenseEntity> expenses = expensesService.getExpenses(type);
            return ResponseEntity.ok().body(expenses);
        } catch (Exception e) {
            throw new BadRequestException("Something went wrong " + e.getMessage());
        }
    }

    @GetMapping(EXPENSES_TODAY)
    @Transactional(readOnly = true)
    public ResponseEntity<List<ExpenseEntity>> getExpensesForToday(
    )  throws BadRequestException {
        try {
            List<ExpenseEntity> expenses = expensesService.getExpensesForToday();
            return ResponseEntity.ok().body(expenses);
        } catch (Exception e) {
            throw new BadRequestException("Something went wrong " + e.getMessage());
        }
    }

    @GetMapping(SINGLE_EXPENSE)
    @Transactional(readOnly = true)
    public ResponseEntity<Optional<ExpenseEntity>> getExpenseById(
            @RequestParam Long expenseId
    )  throws BadRequestException {
        try {
            Optional<ExpenseEntity> expenses = expensesService.getExpenseById(expenseId);
            return ResponseEntity.ok().body(expenses);
        } catch (Exception e) {
            throw new BadRequestException("Something went wrong " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<ExpenseEntity> createExpenseEntity(
            @RequestBody ExpenseEntity expenseBody
    ) throws BadRequestException {
        try {
            System.out.print(expenseBody);
            ExpenseEntity expense = expensesService.createExpense(expenseBody);
            return ResponseEntity.ok().body(expense);
        } catch (Exception e) {
            throw new BadRequestException("Something went wrong " + e.getMessage());
        }
    }

    @PatchMapping(SINGLE_EXPENSE)
    public ResponseEntity<ExpenseEntity> updateExpense(
            @RequestBody ExpenseEntity expenseBody,
            @PathVariable(name = "expenseId") Long expenseId,
            @RequestParam(name = "savePrev", defaultValue = "false") Boolean savePrev
    ) throws BadRequestException {
        try {
            if (expenseBody.getType() != null) {
                try {
                    ExpenseFrequencyTypes.valueOf(expenseBody.getType().name());
                } catch (IllegalArgumentException e) {
                    throw new BadRequestException("Invalid type provided: " + expenseBody.getType());
                }
            }
            ExpenseEntity expense = expensesService.updateExpense(expenseBody, expenseId, savePrev);
            return ResponseEntity.ok().body(expense);
        } catch (Exception e) {
            throw new BadRequestException("Something went wrong " + e.getMessage());
        }
    }

    @DeleteMapping(SINGLE_EXPENSE)
    public ResponseEntity<String> deleteExpense(
            @PathVariable(name = "expenseId") Long expenseId
    ) {
        try {
            expensesService.deleteExpense(expenseId);
            return ResponseEntity.ok("Expense with id: " + expenseId + " was deleted");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong " + e.getMessage());
        }
    }
}
