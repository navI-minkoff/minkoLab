package tech.reliab.course.minkoLab.bank.service.impl;

import tech.reliab.course.minkoLab.bank.entity.Bank;
import tech.reliab.course.minkoLab.bank.entity.CreditAccount;
import tech.reliab.course.minkoLab.bank.entity.PaymentAccount;
import tech.reliab.course.minkoLab.bank.entity.User;
import tech.reliab.course.minkoLab.bank.service.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;

public class UserServiceImpl implements UserService {

    private static final int MONTHLY_INCOME_BOUND = 10001;
    private static final double DIVIDER = 1000.0;
    private static final int FACTOR = 100;
    private static int usersCount = 0;

    private final List<User> users = new ArrayList<>();

    public User createUser(String fullName, LocalDate birthDate, String job) {
        User user = new User(fullName, birthDate, job);
        user.setId(usersCount++);
        user.setMonthlyIncome(generateMonthlyIncome());
        user.setCreditRating(generateCreditRating(user.getMonthlyIncome()));
        users.add(user);
        return user;
    }

    private int generateMonthlyIncome() {
        return new Random().nextInt(MONTHLY_INCOME_BOUND);
    }

    private int generateCreditRating(double monthlyIncome) {
        return (int) Math.ceil(monthlyIncome / DIVIDER) * FACTOR;
    }


    public Optional<User> getUserById(int id) {
        return users.stream()
                .filter(user -> user.getId() == id)
                .findFirst();
    }

    @Override
    public List<User> getUsersByBank(Bank bank) {
        return users.stream()
                .filter(user ->
                        user.getBanks()
                                .stream()
                                .anyMatch((Bank userBank) -> userBank.getId() == bank.getId())).toList();
    }


    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    public void updateUser(int id, String name) {
        User user = getUserIfExists(id);
        user.setFullName(name);
    }

    public void deleteUser(int id) {
        users.remove(getUserIfExists(id));
    }

    public User getUserIfExists(int id) {
        return getUserById(id).orElseThrow(() -> new NoSuchElementException("User was not found"));
    }


    public void addCreditAccount(CreditAccount creditAccount, User user) {
        List<CreditAccount> creditAccounts = user.getCreditAccounts();
        creditAccounts.add(creditAccount);
        user.setCreditAccounts(creditAccounts);
    }

    public void addPaymentAccount(PaymentAccount paymentAccount, User user) {
        List<PaymentAccount> paymentAccounts = user.getPaymentAccounts();
        paymentAccounts.add(paymentAccount);
        user.setPaymentAccounts(paymentAccounts);
    }


    public void addBank(Bank bank, User user) {
        List<Bank> banks = user.getBanks();
        banks.add(bank);
        user.setBanks(banks);
    }


    public void deleteCreditAccount(CreditAccount creditAccount, User user) {
        List<CreditAccount> creditAccounts = user.getCreditAccounts();
        creditAccounts.remove(creditAccount);
        user.setCreditAccounts(creditAccounts);
    }


    public void deletePaymentAccount(PaymentAccount paymentAccount, User user) {
        List<PaymentAccount> paymentAccounts = user.getPaymentAccounts();
        paymentAccounts.remove(paymentAccount);
        user.setPaymentAccounts(paymentAccounts);
    }

    public void deleteBank(Bank bank) {
        for (User curUser : users) {
            List<Bank> banks = curUser.getBanks();
            banks.remove(bank);
            curUser.setBanks(banks);
        }
    }
}