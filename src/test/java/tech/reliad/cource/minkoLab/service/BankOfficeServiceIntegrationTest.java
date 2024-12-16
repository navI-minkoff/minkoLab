package tech.reliad.cource.minkoLab.service;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import tech.reliab.course.minkoLab.bank.MinkoLabApplication;
import tech.reliab.course.minkoLab.bank.entity.Bank;
import tech.reliab.course.minkoLab.bank.entity.BankOffice;
import tech.reliab.course.minkoLab.bank.model.BankOfficeRequest;
import tech.reliab.course.minkoLab.bank.repository.BankRepository;
import tech.reliab.course.minkoLab.bank.service.BankOfficeService;
import tech.reliad.cource.minkoLab.container.TestContainerConfig;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest(classes = MinkoLabApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = {TestContainerConfig.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BankOfficeServiceIntegrationTest {

    @Autowired
    private BankOfficeService bankOfficeService;

    @Autowired
    private BankRepository bankRepository;

    private Bank testBank;

    @BeforeEach
    void setUp() {
        testBank = bankRepository.save(new Bank("Sample Bank"));
    }

    @Test
    void testCreateBankOffice() {
        BankOfficeRequest request = new BankOfficeRequest();
        request.setName("Main Office");
        request.setAddress("Main Street 1");
        request.setCanPlaceAtm(true);
        request.setCanIssueLoan(true);
        request.setCashWithdrawal(true);
        request.setCashDeposit(false);
        request.setRentCost(1000);
        request.setBankId(testBank.getId());

        BankOffice createdOffice = bankOfficeService.createBankOffice(request);
        Assertions.assertNotNull(createdOffice.getId(), "ID офиса должен быть присвоен после создания");
        Assertions.assertEquals("Main Office", createdOffice.getName(), "Ожидаемое имя офиса не совпадает");
        Assertions.assertEquals("Main Street 1", createdOffice.getAddress(), "Адрес офиса не соответствует ожиданиям");
        Assertions.assertTrue(createdOffice.isCanPlaceAtm(), "Офис должен иметь возможность установки банкоматов");
        Assertions.assertTrue(createdOffice.isCanIssueLoan(), "Офис должен иметь возможность выдачи кредитов");
        Assertions.assertTrue(createdOffice.isCashWithdrawal(), "Офис должен поддерживать снятие наличных");
        Assertions.assertFalse(createdOffice.isCashDeposit(), "Офис не должен принимать наличные");
        Assertions.assertEquals(1000, createdOffice.getRentCost(), "Стоимость аренды не совпадает с ожидаемой");
        Assertions.assertEquals(testBank.getId(), createdOffice.getBank().getId(), "Банк офиса не совпадает с ожидаемым");
    }

    @Test
    void testUpdateBankOffice() {
        // Создадим офис
        BankOfficeRequest request = new BankOfficeRequest();
        request.setName("Branch Office");
        request.setAddress("Branch Street 5");
        request.setCanPlaceAtm(false);
        request.setCanIssueLoan(false);
        request.setCashWithdrawal(false);
        request.setCashDeposit(true);
        request.setRentCost(500);
        request.setBankId(testBank.getId());

        BankOffice office = bankOfficeService.createBankOffice(request);
        // Обновим его имя
        BankOffice updatedOffice = bankOfficeService.updateBankOffice(office.getId(), "New Branch Office");
        Assertions.assertEquals("New Branch Office", updatedOffice.getName(), "Имя офиса должно быть изменено на новое");
    }

    @Test
    void testGetBankOfficeById() {
        // Создаём офис
        BankOfficeRequest request = new BankOfficeRequest();
        request.setName("Info Office");
        request.setAddress("Info Street 10");
        request.setCanPlaceAtm(true);
        request.setCanIssueLoan(false);
        request.setCashWithdrawal(true);
        request.setCashDeposit(true);
        request.setRentCost(2000);
        request.setBankId(testBank.getId());

        BankOffice office = bankOfficeService.createBankOffice(request);
        BankOffice found = bankOfficeService.getBankDtoOfficeById(office.getId());
        Assertions.assertNotNull(found, "Не удалось найти офис по ID");
        Assertions.assertEquals("Info Office", found.getName(), "Имя найденного офиса не совпадает с ожидаемым");
    }

    @Test
    void testGetAllBankOffices() {
        // Создадим несколько офисов
        BankOfficeRequest request1 = new BankOfficeRequest();
        request1.setName("Office A");
        request1.setAddress("Address A");
        request1.setCanPlaceAtm(true);
        request1.setCanIssueLoan(true);
        request1.setCashWithdrawal(true);
        request1.setCashDeposit(true);
        request1.setRentCost(1500);
        request1.setBankId(testBank.getId());
        bankOfficeService.createBankOffice(request1);

        BankOfficeRequest request2 = new BankOfficeRequest();
        request2.setName("Office B");
        request2.setAddress("Address B");
        request2.setCanPlaceAtm(false);
        request2.setCanIssueLoan(false);
        request2.setCashWithdrawal(false);
        request2.setCashDeposit(false);
        request2.setRentCost(3000);
        request2.setBankId(testBank.getId());
        bankOfficeService.createBankOffice(request2);

        List<BankOffice> offices = bankOfficeService.getAllBankOffices();
        Assertions.assertEquals(2, offices.size(), "Количество офисов должно быть равно 2");
    }

    @Test
    void testDeleteBankOffice() {
        BankOfficeRequest request = new BankOfficeRequest();
        request.setName("Office to Delete");
        request.setAddress("Delete Street 1");
        request.setCanPlaceAtm(false);
        request.setCanIssueLoan(false);
        request.setCashWithdrawal(false);
        request.setCashDeposit(false);
        request.setRentCost(700);
        request.setBankId(testBank.getId());

        BankOffice office = bankOfficeService.createBankOffice(request);

        bankOfficeService.deleteBankAtm(office.getId());

        Assertions.assertThrows(
                RuntimeException.class,
                () -> bankOfficeService.getBankDtoOfficeById(office.getId()),
                "После удаления офис по ID должен быть недоступен"
        );
    }
}
