package tech.reliab.course.minkoLab.bank.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.reliab.course.minkoLab.bank.entity.Bank;
import tech.reliab.course.minkoLab.bank.repository.BankRepository;
import tech.reliab.course.minkoLab.bank.service.BankService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {

    private static final int RATING_BOUND = 101;
    private static final int TOTAL_MONEY_BOUND = 1000001;
    private static final int MAX_RATE = 20;
    private static final double DIVIDER = 10.0;

    private final BankRepository bankRepository;

    public Bank createBank(String bankName) {
        Bank bank = new Bank(bankName);
        bank.setRating(generateRating());
        bank.setTotalMoney(generateTotalMoney());
        bank.setInterestRate(calculateInterestRate(bank.getRating()));
        return bankRepository.save(bank);
    }

    private int generateRating() {
        return new Random().nextInt(RATING_BOUND);
    }

    private double generateTotalMoney() {
        return new Random().nextInt(TOTAL_MONEY_BOUND);
    }

    private double calculateInterestRate(int rating) {
        return MAX_RATE - (rating / DIVIDER);
    }

    public Bank getBankDtoById(int id) {
        return getBankById(id);
    }

    public Bank getBankById(int id) {
        return bankRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Bank was not found"));
    }

    public List<Bank> getAllBanks() {
        return bankRepository.findAll();
    }

    public Bank updateBank(int id, String name) {
        Bank bank = getBankById(id);
        bank.setName(name);
        return bankRepository.save(bank);
    }

    public void deleteBank(int id) {
        bankRepository.deleteById(id);
    }
}