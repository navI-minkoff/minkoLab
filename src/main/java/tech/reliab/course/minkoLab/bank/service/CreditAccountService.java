package tech.reliab.course.minkoLab.bank.service;


import tech.reliab.course.minkoLab.bank.entity.Bank;
import tech.reliab.course.minkoLab.bank.entity.CreditAccount;
import tech.reliab.course.minkoLab.bank.entity.Employee;
import tech.reliab.course.minkoLab.bank.entity.PaymentAccount;
import tech.reliab.course.minkoLab.bank.entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CreditAccountService {

    CreditAccount createCreditAccount(User user, Bank bank, LocalDate startDate, int loanTermMonths,
                                      double loanAmount, double interestRate, Employee employee,
                                      PaymentAccount paymentAccount);

    Optional<CreditAccount> getCreditAccountById(int id);

    List<CreditAccount> getCreditAccountByUserId(int userId);

    List<CreditAccount> getAllCreditAccounts();

    void updateCreditAccount(int id, Bank bank);

    void deleteCreditAccount(int accountId, int userId);
}