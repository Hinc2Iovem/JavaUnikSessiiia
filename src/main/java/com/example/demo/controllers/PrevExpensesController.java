package com.example.demo.controllers;

import com.example.demo.entities.ExpensePrevDataEntity;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.services.PrevExpensesService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping("/api/v1/prevExpenses")
public class PrevExpensesController {
    @Autowired
    PrevExpensesService prevExpensesService;

    public static final String ALL_BY_EXPENSE_ID = "/expenses/{expenseId}";
    public static final String SINGLE_EXPENSE = "/{prevExpenseId}";

    @GetMapping(ALL_BY_EXPENSE_ID)
    @Transactional(readOnly = true)
    public ResponseEntity<List<ExpensePrevDataEntity>> getExpenses(
        @RequestParam Long expenseId
    )  throws BadRequestException {
        try {
            List<ExpensePrevDataEntity> expenses = prevExpensesService.getExpenses(expenseId);
            return ResponseEntity.ok().body(expenses);
        } catch (Exception e) {
            throw new BadRequestException("Something went wrong " + e.getMessage());
        }
    }

    @DeleteMapping(SINGLE_EXPENSE)
    public ResponseEntity<String> deletePrevExpense(
            @PathVariable(name = "prevExpenseId") Long prevExpenseId
    ) {
        try {
            prevExpensesService.deletePrevExpense(prevExpenseId);
            return ResponseEntity.ok("Expense with id: " + prevExpenseId + " was deleted");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Something went wrong " + e.getMessage());
        }
    }
}
