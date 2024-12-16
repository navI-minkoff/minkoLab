package tech.reliad.cource.minkoLab.service;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import tech.reliab.course.minkoLab.bank.MinkoLabApplication;
import tech.reliab.course.minkoLab.bank.entity.Bank;
import tech.reliab.course.minkoLab.bank.entity.BankOffice;
import tech.reliab.course.minkoLab.bank.entity.Employee;
import tech.reliab.course.minkoLab.bank.model.EmployeeRequest;
import tech.reliab.course.minkoLab.bank.repository.BankRepository;
import tech.reliab.course.minkoLab.bank.repository.BankOfficeRepository;
import tech.reliab.course.minkoLab.bank.service.EmployeeService;
import tech.reliad.cource.minkoLab.container.TestContainerConfig;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest(classes = MinkoLabApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = {TestContainerConfig.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EmployeeServiceIntegrationTest {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private BankOfficeRepository bankOfficeRepository;

    private Bank testBank;
    private BankOffice testBankOffice;

    @BeforeEach
    void setUp() {
        testBank = bankRepository.save(new Bank("Sample Bank"));
        testBankOffice = bankOfficeRepository.save(new BankOffice("Central Office", "Main Avenue, 15", true, true, true, true, 5500, testBank));
    }

    @BeforeEach
    void cleanDatabase() {
        employeeService.deleteAllEmployees();
    }

    @Test
    void testCreateEmployee() {
        EmployeeRequest request = new EmployeeRequest();
        request.setFullName("John Smith");
        request.setBirthDate(LocalDate.of(1992, 2, 3));
        request.setPosition("Manager");
        request.setBankId(testBank.getId());
        request.setRemoteWork(false);
        request.setBankOfficeId(testBankOffice.getId());
        request.setCanIssueLoans(true);
        request.setSalary(5200);

        Employee createdEmployee = employeeService.createEmployee(request);

        Assertions.assertNotNull(createdEmployee.getId(), "ID сотрудника должен быть инициализирован после создания");
        Assertions.assertEquals("John Smith", createdEmployee.getFullName(), "Имя должно быть корректным");
        Assertions.assertEquals("Manager", createdEmployee.getPosition(), "Должность должна быть правильной");
        Assertions.assertEquals(testBank.getId(), createdEmployee.getBank().getId(), "Банк должен соответствовать");
        Assertions.assertEquals(testBankOffice.getId(), createdEmployee.getBankOffice().getId(), "Офис должен быть верным");
        Assertions.assertTrue(createdEmployee.isCanIssueLoans(), "Сотрудник должен иметь право на выдачу кредитов");
        Assertions.assertEquals(5200, createdEmployee.getSalary(), "Зарплата должна быть корректной");
    }

    @Test
    void testUpdateEmployee() {
        EmployeeRequest request = new EmployeeRequest();
        request.setFullName("Jane Doe");
        request.setBirthDate(LocalDate.of(1985, 5, 15));
        request.setPosition("Consultant");
        request.setBankId(testBank.getId());
        request.setRemoteWork(true);
        request.setBankOfficeId(testBankOffice.getId());
        request.setCanIssueLoans(false);
        request.setSalary(3200);

        Employee createdEmployee = employeeService.createEmployee(request);
        Employee updatedEmployee = employeeService.updateEmployee(createdEmployee.getId(), "Jane Black");

        Assertions.assertEquals("Jane Black", updatedEmployee.getFullName(), "Имя должно быть обновлено");
    }

    @Test
    void testGetEmployeeById() {
        EmployeeRequest request = new EmployeeRequest();
        request.setFullName("Mark Smith");
        request.setBirthDate(LocalDate.of(1991, 3, 5));
        request.setPosition("Clerk");
        request.setBankId(testBank.getId());
        request.setRemoteWork(false);
        request.setBankOfficeId(testBankOffice.getId());
        request.setCanIssueLoans(false);
        request.setSalary(2700);

        Employee createdEmployee = employeeService.createEmployee(request);
        Employee foundEmployee = employeeService.getEmployeeDtoById(createdEmployee.getId());

        Assertions.assertNotNull(foundEmployee, "По ID должен быть найден сотрудник");
        Assertions.assertEquals("Mark Smith", foundEmployee.getFullName(), "Имя сотрудника должно совпадать");
    }

    @Test
    void testGetAllEmployees() {
        EmployeeRequest request1 = new EmployeeRequest();
        request1.setFullName("Employee X");
        request1.setBirthDate(LocalDate.of(1993, 4, 12));
        request1.setPosition("Analyst");
        request1.setBankId(testBank.getId());
        request1.setRemoteWork(false);
        request1.setBankOfficeId(testBankOffice.getId());
        request1.setCanIssueLoans(true);
        request1.setSalary(3600);
        employeeService.createEmployee(request1);

        EmployeeRequest request2 = new EmployeeRequest();
        request2.setFullName("Employee Y");
        request2.setBirthDate(LocalDate.of(1988, 7, 8));
        request2.setPosition("Cashier");
        request2.setBankId(testBank.getId());
        request2.setRemoteWork(true);
        request2.setBankOfficeId(testBankOffice.getId());
        request2.setCanIssueLoans(false);
        request2.setSalary(2100);
        employeeService.createEmployee(request2);

        List<Employee> employees = employeeService.getAllEmployees();

        Assertions.assertEquals(2, employees.size(), "Количество сотрудников должно быть равно 2");
    }

    @Test
    void testDeleteEmployee() {
        EmployeeRequest request = new EmployeeRequest();
        request.setFullName("Employee to Remove");
        request.setBirthDate(LocalDate.of(1994, 8, 14));
        request.setPosition("Teller");
        request.setBankId(testBank.getId());
        request.setRemoteWork(false);
        request.setBankOfficeId(testBankOffice.getId());
        request.setCanIssueLoans(false);
        request.setSalary(1900);

        Employee createdEmployee = employeeService.createEmployee(request);

        employeeService.deleteEmployee(createdEmployee.getId());

        Assertions.assertThrows(
                RuntimeException.class,
                () -> employeeService.getEmployeeDtoById(createdEmployee.getId()),
                "После удаления попытка получения сотрудника по ID должна вызвать исключение"
        );
    }
}
