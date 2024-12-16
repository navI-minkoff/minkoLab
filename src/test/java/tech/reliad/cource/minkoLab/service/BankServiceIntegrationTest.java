package tech.reliad.cource.minkoLab.service;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import tech.reliab.course.minkoLab.bank.MinkoLabApplication;
import tech.reliab.course.minkoLab.bank.entity.Bank;
import tech.reliab.course.minkoLab.bank.service.BankOfficeService;
import tech.reliab.course.minkoLab.bank.service.BankService;
import tech.reliad.cource.minkoLab.container.TestContainerConfig;

import java.util.List;

@SpringBootTest(classes = MinkoLabApplication.class)
@ContextConfiguration(classes = {TestContainerConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BankServiceIntegrationTest {

    @Autowired
    private BankService bankService;

    @Autowired
    private BankOfficeService bankOfficeService;

    @BeforeEach
    void cleanDatabase() {
        bankOfficeService.deleteAllBankOffices();
        bankService.deleteAllBanks();
    }

    @Test
    void shouldCreateBank() {
        Bank createdBank = bankService.createBank("Test Bank");
        Assertions.assertNotNull(createdBank.getId(), "После создания банка его ID не должен быть равен null");
        Assertions.assertEquals("Test Bank", createdBank.getName(), "Название банка должно соответствовать переданному");
    }

    @Test
    void shouldUpdateBank() {
        Bank createdBank = bankService.createBank("Old Name");
        Bank updatedBank = bankService.updateBank(createdBank.getId(), "New Name");
        Assertions.assertEquals("New Name", updatedBank.getName(), "Название банка должно обновиться на новое значение");
    }

    @Test
    void shouldGetBankById() {
        Bank createdBank = bankService.createBank("Unique Bank");
        Bank foundBank = bankService.getBankDtoById(createdBank.getId());
        Assertions.assertEquals("Unique Bank", foundBank.getName(), "Банк, найденный по ID, должен иметь правильное название");
    }

    @Test
    void shouldReturnAllBanks() {
        bankService.createBank("Bank A");
        bankService.createBank("Bank B");
        List<Bank> banks = bankService.getAllBanks();
        Assertions.assertEquals(2, banks.size(), "Ожидается, что метод вернет два банка");
    }

    @Test
    void shouldDeleteBank() {
        Bank createdBank = bankService.createBank("Bank to Delete");
        bankService.deleteBank(createdBank.getId());
        Assertions.assertThrows(
                RuntimeException.class,
                () -> bankService.getBankDtoById(createdBank.getId()),
                "После удаления банка попытка получения его по ID должна привести к исключению"
        );
    }
}
