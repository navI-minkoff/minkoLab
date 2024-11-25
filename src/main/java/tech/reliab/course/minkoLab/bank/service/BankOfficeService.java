package tech.reliab.course.minkoLab.bank.service;

import tech.reliab.course.minkoLab.bank.entity.Bank;
import tech.reliab.course.minkoLab.bank.entity.BankOffice;

import java.util.List;
import java.util.Optional;

public interface BankOfficeService {

    BankOffice createBankOffice(String name, String address, boolean canPlaceAtm,
                                boolean canIssueLoan, boolean cashWithdrawal, boolean cashDeposit,
                                double rentCost, Bank bank);

    Optional<BankOffice> getBankOfficeById(int id);

    List<BankOffice> getAllBankOfficesByBank(Bank bank);

    List<BankOffice> getAllBankOffices();

    void updateBankOffice(int id, String name);

    void deleteBankAtm(int officeId, int bankId);
}