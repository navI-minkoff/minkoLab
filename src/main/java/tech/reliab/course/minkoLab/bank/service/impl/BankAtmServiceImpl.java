package tech.reliab.course.minkoLab.bank.service.impl;

import tech.reliab.course.minkoLab.bank.entity.Bank;
import tech.reliab.course.minkoLab.bank.entity.BankAtm;
import tech.reliab.course.minkoLab.bank.entity.BankAtmStatus;
import tech.reliab.course.minkoLab.bank.entity.BankOffice;
import tech.reliab.course.minkoLab.bank.entity.Employee;
import tech.reliab.course.minkoLab.bank.service.BankAtmService;
import tech.reliab.course.minkoLab.bank.service.BankService;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class BankAtmServiceImpl implements BankAtmService {

    private static int bankAtmsCount = 0;

    private List<BankAtm> bankAtms = new ArrayList<>();

    private final BankService bankService;

    public BankAtmServiceImpl(BankService bankService) {
        this.bankService = bankService;
    }

    public BankAtm createBankAtm(String name, String address, Bank bank, BankOffice location, Employee employee,
                                 boolean cashWithdrawal, boolean cashDeposit, double maintenanceCost) {
        BankAtm bankAtm = new BankAtm(name, address, bank, location, employee,
                cashWithdrawal, cashDeposit, maintenanceCost);
        bankAtm.setId(bankAtmsCount++);
        bankAtm.setStatus(generateStatus());
        bankAtm.setAtmMoney(generateAtmMoney(bank));
        bankService.addAtm(bank.getId());
        bankAtms.add(bankAtm);
        return bankAtm;
    }


    private BankAtmStatus generateStatus() {
        return BankAtmStatus.randomStatus();
    }

    private double generateAtmMoney(Bank bank) {
        return new Random().nextDouble(bank.getTotalMoney());
    }

    public Optional<BankAtm> getBankAtmById(int id) {
        return bankAtms.stream()
                .filter(bankAtm -> bankAtm.getId() == id)
                .findFirst();
    }

    public List<BankAtm> getAllBankAtms() {
        return new ArrayList<>(bankAtms);
    }


    public List<BankAtm> getAllBankAtmsByBank(Bank bank) {
        return bankAtms.stream()
                .filter(bankAtm -> bankAtm.getBank().getId() == bank.getId())
                .collect(Collectors.toList());
    }


    public void updateBankAtm(int id, String name) {
        BankAtm bankAtm = getBankAtmIfExists(id);
        bankAtm.setName(name);
    }

    public void deleteBankAtm(int id) {
        BankAtm bankAtm = getBankAtmIfExists(id);
        bankAtms.remove(bankAtm);
        Bank bank = bankAtm.getBank();
        bankService.removeAtm(bank.getId());
    }

    private BankAtm getBankAtmIfExists(int id) {
        return getBankAtmById(id).orElseThrow(() -> new NoSuchElementException("BankAtm was not found"));
    }
}