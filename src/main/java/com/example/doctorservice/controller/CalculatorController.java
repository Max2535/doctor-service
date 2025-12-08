package com.example.doctorservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.doctorservice.controller.response.CalculationResult;
import com.example.doctorservice.service.CalculatorService;

@RestController
@RequestMapping("/api/calculator")
public class CalculatorController {

    private final CalculatorService calculatorService;

    public CalculatorController(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    @GetMapping("/add")
    public int add(@RequestParam int a, @RequestParam int b) {
        return a + b;
    }

    @GetMapping("/subtract/{a}/{b}")
    public int subtract(@PathVariable int a, @PathVariable int b) {
        return a - b;
    }

    @GetMapping("/multiply")
    public int multiply(@RequestParam int a, @RequestParam int b) {
        return a * b;
    }

    @GetMapping("/divide/{a}/{b}")
    public double divide(@PathVariable int a, @PathVariable int b) {
        if (b == 0) {
            throw new IllegalArgumentException("Division by zero is not allowed.");
        }
        return (double) a / b;
    }

    @GetMapping("/calculate")
    public CalculationResult calculate(@RequestParam int operand1,
            @RequestParam int operand2,
            @RequestParam String operation) {
        // return CalculationResult object
        double result;
        switch (operation.toLowerCase()) {
            case "add":
                result = calculatorService.add(operand1, operand2);
                break;
            case "subtract":
                result = calculatorService.subtract(operand1, operand2);
                break;
            case "multiply":
                result = calculatorService.multiply(operand1, operand2);
                break;
            case "divide":
                if (operand2 == 0) {
                    throw new IllegalArgumentException("Division by zero is not allowed.");
                }
                result = calculatorService.divide(operand1, operand2);
                break;
            default:
                throw new IllegalArgumentException("Invalid operation: " + operation);
        }
        return new CalculationResult(operand1, operand2, operation, result);
    }
}
