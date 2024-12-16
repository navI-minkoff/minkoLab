package tech.reliad.cource.minkoLab.service;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import tech.reliab.course.minkoLab.bank.MinkoLabApplication;
import tech.reliab.course.minkoLab.bank.entity.Bank;
import tech.reliab.course.minkoLab.bank.entity.PaymentAccount;
import tech.reliab.course.minkoLab.bank.entity.User;
import tech.reliab.course.minkoLab.bank.model.PaymentAccountRequest;
import tech.reliab.course.minkoLab.bank.repository.BankRepository;
import tech.reliab.course.minkoLab.bank.repository.UserRepository;
import tech.reliab.course.minkoLab.bank.service.PaymentAccountService;
import tech.reliad.cource.minkoLab.container.TestContainerConfig;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest(classes = MinkoLabApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = {TestContainerConfig.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PaymentAccountServiceIntegrationTest {

    @Autowired
    private PaymentAccountService paymentAccountService;

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private UserRepository userRepository;

    private Bank testBank;
    private User testUser;

    @BeforeEach
    void setUp() {
        testBank = bankRepository.save(new Bank("Test Bank"));
        testUser = userRepository.save(new User("John Doe", LocalDate.of(1990, 1, 1), "Manager"));
    }

    @BeforeEach
    void cleanDatabase() {
        paymentAccountService.deleteAllPaymentAccounts();
    }

    @Test
    void testCreatePaymentAccount() {
        PaymentAccountRequest request = new PaymentAccountRequest();
        request.setUserId(testUser.getId());
        request.setBankId(testBank.getId());

        PaymentAccount createdAccount = paymentAccountService.createPaymentAccount(request);
        Assertions.assertNotNull(createdAccount.getId(), "ID платёжного аккаунта должен быть не null после создания");
        Assertions.assertEquals(testUser.getId(), createdAccount.getUser().getId(), "Пользователь должен совпадать");
        Assertions.assertEquals(testBank.getId(), createdAccount.getBank().getId(), "Банк должен совпадать");
    }

    @Test
    void testUpdatePaymentAccount() {
        PaymentAccountRequest request = new PaymentAccountRequest();
        request.setUserId(testUser.getId());
        request.setBankId(testBank.getId());

        PaymentAccount account = paymentAccountService.createPaymentAccount(request);

        // Создадим второй банк для обновления
        Bank newBank = bankRepository.save(new Bank("Another Bank"));

        PaymentAccount updatedAccount = paymentAccountService.updatePaymentAccount(account.getId(), newBank.getId());
        Assertions.assertEquals(newBank.getId(), updatedAccount.getBank().getId(), "Банк в платёжном аккаунте должен обновиться");
    }

    @Test
    void testGetPaymentAccountById() {
        PaymentAccountRequest request = new PaymentAccountRequest();
        request.setUserId(testUser.getId());
        request.setBankId(testBank.getId());

        PaymentAccount account = paymentAccountService.createPaymentAccount(request);
        PaymentAccount found = paymentAccountService.getPaymentAccountDtoById(account.getId());
        Assertions.assertNotNull(found, "Платёжный аккаунт должен быть найден по ID");
        Assertions.assertEquals(testBank.getId(), found.getBank().getId(), "Банк должен совпадать при получении по ID");
    }

    @Test
    void testGetAllPaymentAccounts() {
        PaymentAccountRequest request1 = new PaymentAccountRequest();
        request1.setUserId(testUser.getId());
        request1.setBankId(testBank.getId());
        paymentAccountService.createPaymentAccount(request1);

        PaymentAccountRequest request2 = new PaymentAccountRequest();
        request2.setUserId(testUser.getId());
        request2.setBankId(testBank.getId());
        paymentAccountService.createPaymentAccount(request2);

        List<PaymentAccount> accounts = paymentAccountService.getAllPaymentAccounts();
        Assertions.assertEquals(2, accounts.size(), "Должно вернуться 2 платёжных аккаунта");
    }

    @Test
    void testDeletePaymentAccount() {
        PaymentAccountRequest request = new PaymentAccountRequest();
        request.setUserId(testUser.getId());
        request.setBankId(testBank.getId());

        PaymentAccount account = paymentAccountService.createPaymentAccount(request);
        paymentAccountService.deletePaymentAccount(account.getId());

        Assertions.assertThrows(
                RuntimeException.class,
                () -> paymentAccountService.getPaymentAccountDtoById(account.getId()),
                "После удаления попытка получить аккаунт по ID должна вызывать исключение"
        );
    }
}