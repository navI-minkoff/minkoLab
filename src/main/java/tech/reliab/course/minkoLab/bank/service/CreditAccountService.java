package tech.reliab.course.minkoLab.bank.service;

import tech.reliab.course.minkoLab.bank.entity.CreditAccount;
import tech.reliab.course.minkoLab.bank.model.CreditAccountRequest;
import java.util.List;

public interface CreditAccountService {

    CreditAccount createCreditAccount(CreditAccountRequest creditAccountRequest);

    CreditAccount getCreditAccountById(int id);

    CreditAccount getCreditAccountDtoById(int id);

    List<CreditAccount> getAllCreditAccounts();

    CreditAccount updateCreditAccount(int id, int bankId);

    void deleteCreditAccount(int id);

    void deleteAllCreditAccounts();
}