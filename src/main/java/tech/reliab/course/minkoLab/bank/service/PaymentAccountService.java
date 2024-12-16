package tech.reliab.course.minkoLab.bank.service;

import tech.reliab.course.minkoLab.bank.entity.PaymentAccount;
import tech.reliab.course.minkoLab.bank.model.PaymentAccountRequest;
import java.util.List;

public interface PaymentAccountService {

    PaymentAccount createPaymentAccount(PaymentAccountRequest paymentAccountRequest);

    PaymentAccount getPaymentAccountById(int id);

    PaymentAccount getPaymentAccountDtoById(int id);

    List<PaymentAccount> getAllPaymentAccounts();

    PaymentAccount updatePaymentAccount(int id, int bankId);

    void deletePaymentAccount(int id);

    void deleteAllPaymentAccounts();
}