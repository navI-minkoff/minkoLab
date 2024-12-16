package tech.reliad.cource.minkoLab.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import tech.reliab.course.minkoLab.bank.entity.Bank;
import tech.reliab.course.minkoLab.bank.entity.BankAtm;
import tech.reliab.course.minkoLab.bank.entity.BankOffice;
import tech.reliab.course.minkoLab.bank.entity.Employee;
import tech.reliab.course.minkoLab.bank.entity.User;
import tech.reliab.course.minkoLab.bank.model.BankAtmRequest;
import tech.reliab.course.minkoLab.bank.model.BankOfficeRequest;
import tech.reliab.course.minkoLab.bank.model.EmployeeRequest;
import tech.reliab.course.minkoLab.bank.repository.BankAtmRepository;
import tech.reliab.course.minkoLab.bank.repository.BankOfficeRepository;
import tech.reliab.course.minkoLab.bank.repository.BankRepository;
import tech.reliab.course.minkoLab.bank.repository.EmployeeRepository;
import tech.reliab.course.minkoLab.bank.repository.UserRepository;
import tech.reliab.course.minkoLab.bank.service.BankAtmService;
import tech.reliab.course.minkoLab.bank.MinkoLabApplication;
import tech.reliab.course.minkoLab.bank.service.BankOfficeService;
import tech.reliab.course.minkoLab.bank.service.EmployeeService;
import tech.reliad.cource.minkoLab.container.TestContainerConfig;

import javax.swing.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = MinkoLabApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = {TestContainerConfig.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BankAtmServiceIntegrationTest {

    @Autowired
    private BankAtmService exampleBankAtmService;

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private BankAtmRepository bankAtmRepository;

    @Autowired
    private BankOfficeRepository bankOfficeRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BankOfficeService bankOfficeService;

    @Autowired
    private EmployeeService employeeService;

    @BeforeEach
    void cleanDatabase() {
        List<Employee> employees = employeeRepository.findAll();
        for (Employee employee : employees) {
            employee.setBankOffice(null);
        }
        employeeRepository.saveAll(employees);
        bankAtmRepository.deleteAll();
        bankOfficeRepository.deleteAll();
        employeeRepository.deleteAll();
        userRepository.deleteAll();
        bankRepository.deleteAll();
    }

    @BeforeEach
    void setUp() {
        bankRepository.save(new Bank("Тестовый банк"));
    }

    @Test
    @DirtiesContext
    void testRequestBankInfo() {
        List<Bank> banks = bankRepository.findAll();
        assertEquals(1, banks.size(), "Ожидается, что в базе данных будет 1 банк");

        Bank testBank = banks.get(0);
        assertTrue(bankAtmRepository.findAllByBankId(testBank.getId()).isEmpty(), "На данный момент не должно быть банкоматов для этого банка");
        assertTrue(bankOfficeRepository.findAllByBankId(testBank.getId()).isEmpty(), "На данный момент не должно быть офисов для этого банка");
    }

    @Test
    void testInitializeBankInfo() {
        Bank bank = bankRepository.findAll().get(0);

        BankOfficeRequest request = new BankOfficeRequest();
        request.setName("Главный офис");
        request.setAddress("Центральная улица 1");
        request.setCanPlaceAtm(true);
        request.setCanIssueLoan(true);
        request.setCashWithdrawal(true);
        request.setCashDeposit(false);
        request.setRentCost(1000);
        request.setBankId(bank.getId());

        BankOffice createdOffice = bankOfficeService.createBankOffice(request);

        EmployeeRequest request1 = new EmployeeRequest();
        request1.setFullName("Иван Иванов");
        request1.setBirthDate(LocalDate.of(1990, 5, 15));
        request1.setPosition("Менеджер");
        request1.setBankId(bank.getId());
        request1.setRemoteWork(false);
        request1.setBankOfficeId(createdOffice.getId());
        request1.setCanIssueLoans(true);
        request1.setSalary(5000);

        Employee employee = employeeService.createEmployee(request1);

        exampleBankAtmService.createBankAtm(new BankAtmRequest(bank.getName(), "Street 1",
                1, 1, 1, true, true, 1));

        Bank savedBank = bankRepository.findById(bank.getId()).orElseThrow(() -> new AssertionError("Банк не найден"));
        assertEquals(bank.getName(), savedBank.getName(), "Имя сохраненного банка не совпадает");

        List<BankAtm> bankAtms = bankAtmRepository.findAllByBankId(savedBank.getId());
        List<BankOffice> bankOffices = bankOfficeRepository.findAllByBankId(savedBank.getId());
        List<Employee> employees = employeeRepository.findAllByBankId(savedBank.getId());

        assertEquals(1, bankAtms.size(), "Должен быть создан 1 банкомат");
        assertEquals(1, bankOffices.size(), "Должен быть создан 1 офис");
        assertEquals(1, employees.size(), "Должен быть создан 1 сотрудник");
    }
}
