package tech.reliab.course.minkoLab.bank.builders;

import tech.reliab.course.minkoLab.bank.entity.Bank;

import java.util.Random;

public class BankBuilder {
    private String bankName;

    private static final int RATING_BOUND = 101;
    private static final int TOTAL_MONEY_BOUND = 1000001;
    private static final int MAX_RATE = 20;
    private static final double DIVIDER = 10.0;

    private Random random = new Random();

    private int generateId() {
        return random.nextInt(Integer.MAX_VALUE);
    }

    private int generateRating() {
        return random.nextInt(RATING_BOUND);
    }

    private double generateTotalMoney() {
        return random.nextInt(TOTAL_MONEY_BOUND);
    }


    private double calculateInterestRate(int rating) {
        return MAX_RATE - (rating / DIVIDER);
    }


    public BankBuilder setBankName(String bankName) {
        this.bankName = bankName;
        return this;
    }

    public Bank createBank() {
        var id = generateId();
        var rating = generateRating();
        var totalMoney = generateTotalMoney();
        var calculateInterestRate = calculateInterestRate(rating);

        return new Bank(id, bankName, rating, totalMoney, calculateInterestRate);
    }
}