package tech.reliab.course.minkoLab.bank.controller.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.reliab.course.minkoLab.bank.controller.PaymentAccountController;
import tech.reliab.course.minkoLab.bank.entity.PaymentAccount;
import tech.reliab.course.minkoLab.bank.model.PaymentAccountRequest;
import tech.reliab.course.minkoLab.bank.service.PaymentAccountService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment-accounts")
public class PaymentAccountImpl implements PaymentAccountController {
    private final PaymentAccountService paymentAccountService;

    @Override
    @PostMapping
    public ResponseEntity<PaymentAccount> createPaymentAccount(PaymentAccountRequest paymentAccountRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentAccountService.createPaymentAccount(paymentAccountRequest));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentAccount(int id) {
        paymentAccountService.deletePaymentAccount(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping("/{id}")
    public ResponseEntity<PaymentAccount> updatePaymentAccount(int id, int bankId) {
        return ResponseEntity.ok(paymentAccountService.updatePaymentAccount(id, bankId));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<PaymentAccount> getBankByPaymentAccount(int id) {
        return ResponseEntity.ok(paymentAccountService.getPaymentAccountDtoById(id));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<PaymentAccount>> getAllPaymentAccounts() {
        return ResponseEntity.ok(paymentAccountService.getAllPaymentAccounts());
    }
}