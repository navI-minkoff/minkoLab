package tech.reliad.cource.minkoLab.service;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import tech.reliab.course.minkoLab.bank.MinkoLabApplication;
import tech.reliab.course.minkoLab.bank.entity.*;
import tech.reliab.course.minkoLab.bank.model.CreditAccountRequest;
import tech.reliab.course.minkoLab.bank.repository.*;
import tech.reliab.course.minkoLab.bank.service.CreditAccountService;
import tech.reliad.cource.minkoLab.container.TestContainerConfig;

import java.io.Console;
import java.time.LocalDate;
import java.util.List;

@SpringBootTest(classes = MinkoLabApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = {TestContainerConfig.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CreditAccountServiceIntegrationTest {

    @Autowired
    private CreditAccountService creditAccountService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PaymentAccountRepository paymentAccountRepository;

    @Autowired
    private BankOfficeRepository bankOfficeRepository;

    private User testUser;
    private Bank testBank;
    private BankOffice testBankOffice;
    private Employee testEmployee;
    private PaymentAccount testPaymentAccount;


    @BeforeEach
    void setUp() {
        testBank = bankRepository.save(new Bank("Sample Bank"));

        testBankOffice = new BankOffice("Sample Office", "Main Street 1", true, true, true, true, 6000, testBank);
        bankOfficeRepository.save(testBankOffice);
        testBankOffice.setStatus(BankOfficeStatus.WORKING);

        testUser = userRepository.save(new User("Alice Johnson", LocalDate.of(1985, 6, 15), "Engineer"));

        testEmployee = employeeRepository.save(new Employee(
            "Bob Green", LocalDate.of(1980, 3, 25),
            "Account Manager",
            testBank,
            true,
            testBankOffice,
            true,
            5500
        ));

        testPaymentAccount = paymentAccountRepository.save(new PaymentAccount(testUser, testBank));
    }

    @BeforeEach
    void clearDataBase() {
        creditAccountService.deleteAllCreditAccounts();
    }

    @Test
    void testCreateCreditAccount() {
        CreditAccountRequest request = new CreditAccountRequest();
        request.setUserId(testUser.getId());
        request.setBankId(testBank.getId());
        request.setStartDate(LocalDate.of(2023, 2, 1));
        request.setLoanTermMonths(15);
        request.setInterestRate(4.5);

        request.setEmployeeId(testEmployee.getId());
        request.setPaymentAccountId(testPaymentAccount.getId());

        CreditAccount createdAccount = creditAccountService.createCreditAccount(request);
        Assertions.assertEquals(testUser.getId(), createdAccount.getUser().getId(), "The user should match");
        Assertions.assertEquals(testBank.getId(), createdAccount.getBank().getId(), "The bank should match");
        Assertions.assertEquals(LocalDate.of(2023, 2, 1), createdAccount.getStartDate(), "The loan start date should match");
        Assertions.assertEquals(15, createdAccount.getLoanTermMonths(), "The loan term should be 15 months");
        Assertions.assertEquals(testEmployee.getId(), createdAccount.getEmployee().getId(), "The employee should match");
        Assertions.assertEquals(testPaymentAccount.getId(), createdAccount.getPaymentAccount().getId(), "The payment account should match");
    }

    @Test
    void testUpdateCreditAccount() {
        CreditAccountRequest request = new CreditAccountRequest();
        request.setUserId(testUser.getId());
        request.setBankId(testBank.getId());
        request.setStartDate(LocalDate.of(2023, 4, 1));
        request.setLoanTermMonths(18);
        request.setInterestRate(5.0);
        request.setEmployeeId(testEmployee.getId());
        request.setPaymentAccountId(testPaymentAccount.getId());

        CreditAccount createdAccount = creditAccountService.createCreditAccount(request);

        Bank newBank = bankRepository.save(new Bank("Alternate Bank"));
        CreditAccount updatedAccount = creditAccountService.updateCreditAccount(createdAccount.getId(), newBank.getId());
        Assertions.assertEquals(newBank.getId(), updatedAccount.getBank().getId(), "The bank in the credit account should be updated");
    }

    @Test
    void testGetCreditAccountById() {
        CreditAccountRequest request = new CreditAccountRequest();
        request.setUserId(testUser.getId());
        request.setBankId(testBank.getId());
        request.setStartDate(LocalDate.of(2023, 8, 5));
        request.setLoanTermMonths(24);
        request.setInterestRate(4.0);
        request.setEmployeeId(testEmployee.getId());
        request.setPaymentAccountId(testPaymentAccount.getId());

        CreditAccount createdAccount = creditAccountService.createCreditAccount(request);

        CreditAccount found = creditAccountService.getCreditAccountDtoById(createdAccount.getId());
        Assertions.assertNotNull(found, "The credit account should be found by ID");
    }

    @Test
    void testGetAllCreditAccounts() {
        CreditAccountRequest request1 = new CreditAccountRequest();
        request1.setUserId(testUser.getId());
        request1.setBankId(testBank.getId());
        request1.setStartDate(LocalDate.of(2021, 9, 1));
        request1.setLoanTermMonths(6);
        request1.setInterestRate(6.5);
        request1.setEmployeeId(testEmployee.getId());
        request1.setPaymentAccountId(testPaymentAccount.getId());
        creditAccountService.createCreditAccount(request1);

        CreditAccountRequest request2 = new CreditAccountRequest();
        request2.setUserId(testUser.getId());
        request2.setBankId(testBank.getId());
        request2.setStartDate(LocalDate.of(2021, 10, 10));
        request2.setLoanTermMonths(18);
        request2.setInterestRate(7.0);
        request2.setEmployeeId(testEmployee.getId());
        request2.setPaymentAccountId(testPaymentAccount.getId());
        creditAccountService.createCreditAccount(request2);

        List<CreditAccount> accounts = creditAccountService.getAllCreditAccounts();
        Assertions.assertEquals(2, accounts.size(), "There should be 2 credit accounts returned");
    }

    @Test
    void testDeleteCreditAccount() {
        CreditAccountRequest request = new CreditAccountRequest();
        request.setUserId(testUser.getId());
        request.setBankId(testBank.getId());
        request.setStartDate(LocalDate.of(2023, 7, 15));
        request.setLoanTermMonths(9);
        request.setInterestRate(6.0);
        request.setEmployeeId(testEmployee.getId());
        request.setPaymentAccountId(testPaymentAccount.getId());

        CreditAccount createdAccount = creditAccountService.createCreditAccount(request);
        creditAccountService.deleteCreditAccount(createdAccount.getId());

        Assertions.assertThrows(
                RuntimeException.class,
                () -> creditAccountService.getCreditAccountDtoById(createdAccount.getId()),
                "After deletion, attempting to get the account by ID should throw an exception"
        );
    }
}
