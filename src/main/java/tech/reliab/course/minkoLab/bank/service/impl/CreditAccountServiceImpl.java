package tech.reliab.course.minkoLab.bank.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.reliab.course.minkoLab.bank.entity.Bank;
import tech.reliab.course.minkoLab.bank.entity.CreditAccount;
import tech.reliab.course.minkoLab.bank.model.CreditAccountRequest;
import tech.reliab.course.minkoLab.bank.repository.CreditAccountRepository;
import tech.reliab.course.minkoLab.bank.service.*;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CreditAccountServiceImpl implements CreditAccountService {

    private final CreditAccountRepository creditAccountRepository;
    private final BankService bankService;
    private final UserService userService;
    private final EmployeeService employeeService;
    private final PaymentAccountService paymentAccountService;


    public CreditAccount createCreditAccount(CreditAccountRequest creditAccountRequest) {
        CreditAccount creditAccount = new CreditAccount(userService.getUserById(creditAccountRequest.getUserId()),
                bankService.getBankById(creditAccountRequest.getBankId()), creditAccountRequest.getStartDate(),
                creditAccountRequest.getLoanTermMonths(), creditAccountRequest.getInterestRate(),
                employeeService.getEmployeeById(creditAccountRequest.getEmployeeId()),
                paymentAccountService.getPaymentAccountById(creditAccountRequest.getPaymentAccountId()));
        creditAccount.setEndDate(calculateEndDate(creditAccountRequest.getStartDate(), creditAccountRequest.getLoanTermMonths()));
        creditAccount.setLoanAmount(calculateLoanAmount(creditAccountRequest.getLoanAmount(), creditAccountRequest.getBankId()));
        creditAccount.setMonthlyPayment(calculateMonthlyPayment(creditAccountRequest.getInterestRate(),
                creditAccountRequest.getLoanAmount(), creditAccountRequest.getLoanTermMonths()));
        creditAccount.setInterestRate(calculateInterestRate(creditAccountRequest.getInterestRate(), creditAccountRequest.getBankId()));
        return creditAccountRepository.save(creditAccount);
    }


    private LocalDate calculateEndDate(LocalDate startDate, int loanTermMonths) {
        return startDate.plusMonths(loanTermMonths);
    }


    private double calculateMonthlyPayment(double interestRate, double loanAmount, int loanTermMonths) {
        double monthlyRate = interestRate / 12 / 100;
        return loanAmount * (monthlyRate / (1 - Math.pow(1 + monthlyRate, -loanTermMonths)));
    }


    private double calculateLoanAmount(double loanAmount, int bankId) {
        Bank bank = bankService.getBankById(bankId);
        if (loanAmount > bank.getTotalMoney()) {
            loanAmount = bank.getTotalMoney();
        }
        return loanAmount;
    }


    private double calculateInterestRate(double interestRate, int bankId) {
        Bank bank = bankService.getBankById(bankId);
        if (interestRate > bank.getInterestRate()) {
            System.out.println("Заданная процентная ставка превышает процентную ставку банка. Ставка будет скорректирована.");
            interestRate = bank.getInterestRate();
        }
        return interestRate;
    }


    public CreditAccount getCreditAccountById(int id) {
        return creditAccountRepository.findById(id).orElseThrow(() -> new NoSuchElementException("CreditAccount was not found"));
    }


    public CreditAccount getCreditAccountDtoById(int id) {
        return getCreditAccountById(id);
    }


    public List<CreditAccount> getAllCreditAccounts() {
        return creditAccountRepository.findAll();
    }


    public CreditAccount updateCreditAccount(int id, int bankId) {
        CreditAccount creditAccount = getCreditAccountById(id);
        creditAccount.setBank(bankService.getBankById(bankId));
        return creditAccountRepository.save(creditAccount);
    }


    public void deleteCreditAccount(int id) {
        creditAccountRepository.deleteById(id);
    }

    public void deleteAllCreditAccounts() {
        creditAccountRepository.deleteAll();
    }
}