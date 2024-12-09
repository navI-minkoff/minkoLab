package tech.reliab.course.minkoLab.bank.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.reliab.course.minkoLab.bank.entity.Bank;
import tech.reliab.course.minkoLab.bank.entity.BankAtm;
import tech.reliab.course.minkoLab.bank.entity.BankAtmStatus;
import tech.reliab.course.minkoLab.bank.model.BankAtmRequest;
import tech.reliab.course.minkoLab.bank.repository.BankAtmRepository;
import tech.reliab.course.minkoLab.bank.service.BankAtmService;
import tech.reliab.course.minkoLab.bank.service.BankOfficeService;
import tech.reliab.course.minkoLab.bank.service.BankService;
import tech.reliab.course.minkoLab.bank.service.EmployeeService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class BankAtmServiceImpl implements BankAtmService {

    private final BankAtmRepository bankAtmRepository;
    private final BankService bankService;
    private final BankOfficeService bankOfficeService;
    private final EmployeeService employeeService;



    public BankAtm createBankAtm(BankAtmRequest bankAtmRequest) {
        Bank bank = bankService.getBankById(bankAtmRequest.getBankId());
        BankAtm bankAtm = new BankAtm(bankAtmRequest.getName(), bankAtmRequest.getAddress(), bank,
                bankOfficeService.getBankOfficeById(bankAtmRequest.getLocationId()),
                employeeService.getEmployeeById(bankAtmRequest.getEmployeeId()),
                bankAtmRequest.isCashWithdrawal(), bankAtmRequest.isCashDeposit(), bankAtmRequest.getMaintenanceCost());
        bankAtm.setStatus(BankAtmStatus.randomStatus());
        bankAtm.setAtmMoney(generateAtmMoney(bank));
        return bankAtmRepository.save(bankAtm);
    }

    private double generateAtmMoney(Bank bank) {
        return new Random().nextDouble(bank.getTotalMoney());
    }

    public BankAtm getBankAtmById(int id) {
        return bankAtmRepository.findById(id).orElseThrow(() -> new NoSuchElementException("BankAtm was not found"));
    }

    public BankAtm getBankAtmDtoById(int id) {
        return getBankAtmById(id);
    }

    public List<BankAtm> getAllBankAtms() {
        return bankAtmRepository.findAll();
    }

    public List<BankAtm> getAllBankAtmsByBankId(int bankId) {
        return bankAtmRepository.findAllByBankId(bankId);
    }

    public BankAtm updateBankAtm(int id, String name) {
        BankAtm bankAtm = getBankAtmById(id);
        bankAtm.setName(name);
        return bankAtmRepository.save(bankAtm);
    }

    public void deleteBankAtm(int id) {
        bankAtmRepository.deleteById(id);
    }
}