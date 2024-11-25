package tech.reliab.course.minkoLab.bank.service.impl;

import tech.reliab.course.minkoLab.bank.entity.Bank;
import tech.reliab.course.minkoLab.bank.service.BankService;
import tech.reliab.course.minkoLab.bank.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Predicate;

public class BankServiceImpl implements BankService {

    private final UserService userService;

    private List<Bank> banks = new ArrayList<>();

    public BankServiceImpl(UserService userService) {
        this.userService = userService;
    }

    public void registerBank(Bank bank) {
        if (getBankById(bank.getId()).isPresent()) return;
        banks.add(bank);
    }

    public Optional<Bank> getBankById(int id) {
        Predicate<Bank> filterById = bank -> bank.getId() == id;
        return banks.stream()
                .filter(filterById)
                .findFirst();
    }

    public List<Bank> getAllBanks() {
        return new ArrayList<>(banks);
    }

    public void updateBank(int id, String name) {
        Bank bank = getBankIfExists(id);
        bank.setName(name);
    }

    public void deleteBank(int id) {
        Bank bank = getBankIfExists(id);
        banks.remove(bank);
        userService.deleteBank(bank);
    }

    public Bank getBankIfExists(int id) throws NoSuchElementException {
        return getBankById(id).orElseThrow(() -> new NoSuchElementException("Bank was not found"));
    }

    public int addOffice(int id) {
        var bank = getBankIfExists(id);
        bank.setOfficeCount(bank.getOfficeCount() + 1);
        return bank.getOfficeCount();
    }

    public int addAtm(int id) {
        var bank = getBankIfExists(id);
        bank.setAtmCount(bank.getAtmCount() + 1);
        return bank.getAtmCount();
    }


    public int addEmployee(int id) {
        var bank = getBankIfExists(id);
        bank.setEmployeeCount(bank.getEmployeeCount() + 1);
        return bank.getEmployeeCount();
    }

    public int addClient(int id) {
        var bank = getBankIfExists(id);
        bank.setClientCount(bank.getClientCount() + 1);
        return bank.getClientCount();
    }


    public int removeOffice(int id) {
        var bank = getBankIfExists(id);
        bank.setOfficeCount(bank.getOfficeCount() - 1);
        return bank.getOfficeCount();
    }

    public int removeAtm(int id) {
        var bank = getBankIfExists(id);
        bank.setAtmCount(bank.getAtmCount() - 1);
        return bank.getAtmCount();
    }


    public int removeEmployee(int id) {
        var bank = getBankIfExists(id);
        bank.setEmployeeCount(bank.getEmployeeCount() - 1);
        return bank.getEmployeeCount();
    }

    public int removeClient(int id) {
        var bank = getBankIfExists(id);
        bank.setClientCount(bank.getClientCount() - 1);
        return bank.getClientCount();
    }
}