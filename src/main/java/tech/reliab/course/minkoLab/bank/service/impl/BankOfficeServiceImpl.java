package tech.reliab.course.minkoLab.bank.service.impl;

import tech.reliab.course.minkoLab.bank.entity.Bank;
import tech.reliab.course.minkoLab.bank.entity.BankOffice;
import tech.reliab.course.minkoLab.bank.entity.BankOfficeStatus;
import tech.reliab.course.minkoLab.bank.service.BankOfficeService;
import tech.reliab.course.minkoLab.bank.service.BankService;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class BankOfficeServiceImpl implements BankOfficeService {

    private static int bankOfficesCount = 0;

    private List<BankOffice> bankOffices = new ArrayList<>();

    private final BankService bankService;

    public BankOfficeServiceImpl(BankService bankService) {
        this.bankService = bankService;
    }

    public BankOffice createBankOffice(String name, String address, boolean canPlaceAtm,
                                       boolean canIssueLoan, boolean cashWithdrawal, boolean cashDeposit,
                                       double rentCost, Bank bank) {
        BankOffice bankOffice = new BankOffice(name, address, canPlaceAtm, canIssueLoan,
                cashWithdrawal, cashDeposit, rentCost, bank);
        bankOffice.setId(bankOfficesCount++);
        bankOffice.setStatus(generateStatus());
        bankOffice.setOfficeMoney(generateOfficeMoney(bank));
        bankOffices.add(bankOffice);
        bankService.addOffice(bank.getId());
        return bankOffice;
    }


    private BankOfficeStatus generateStatus() {
        return BankOfficeStatus.randomStatus();
    }


    public Optional<BankOffice> getBankOfficeById(int id) {
        return bankOffices.stream()
                .filter(bankOffice -> bankOffice.getId() == id)
                .findFirst();
    }

    @Override
    public List<BankOffice> getAllBankOfficesByBank(Bank bank) {
        return bankOffices.stream()
                .filter(bankOffice -> bankOffice.getBankId() == bank.getId())
                .collect(Collectors.toList());
    }

    public List<BankOffice> getAllBankOffices() {
        return new ArrayList<>(bankOffices);
    }

    public void updateBankOffice(int id, String name) {
        BankOffice bankOffice = getBankOfficeIfExists(id);
        bankOffice.setName(name);
    }

    public void deleteBankAtm(int officeId, int bankId) {
        BankOffice bankOffice = getBankOfficeIfExists(officeId);
        bankOffices.remove(bankOffice);
        bankService.removeOffice(bankId);
    }

    private BankOffice getBankOfficeIfExists(int id) {
        return getBankOfficeById(id).orElseThrow(() -> new NoSuchElementException("BankOffice was not found"));
    }

    private double generateOfficeMoney(Bank bank) {
        return new Random().nextDouble(bank.getTotalMoney());
    }
}