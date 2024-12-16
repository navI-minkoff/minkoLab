package tech.reliad.cource.minkoLab.service;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import tech.reliab.course.minkoLab.bank.MinkoLabApplication;
import tech.reliab.course.minkoLab.bank.entity.User;
import tech.reliab.course.minkoLab.bank.model.UserRequest;
import tech.reliab.course.minkoLab.bank.service.UserService;
import tech.reliad.cource.minkoLab.container.TestContainerConfig;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest(classes = MinkoLabApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = {TestContainerConfig.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Test
    void testCreateUser() {
        UserRequest request = new UserRequest();
        request.setFullName("John Smith");
        request.setBirthDate(LocalDate.of(1988, 3, 15));
        request.setJob("Engineer");

        User createdUser = userService.createUser(request);
        Assertions.assertNotNull(createdUser.getId(), "После создания пользователь должен иметь ID");
        Assertions.assertEquals("John Smith", createdUser.getFullName(), "Имя должно совпадать с переданным");
        Assertions.assertEquals("Engineer", createdUser.getJob(), "Должность должна совпадать с указанной");
    }

    @Test
    void testUpdateUser() {
        UserRequest request = new UserRequest();
        request.setFullName("Sara Johnson");
        request.setBirthDate(LocalDate.of(1982, 10, 22));
        request.setJob("Consultant");

        User user = userService.createUser(request);
        User updatedUser = userService.updateUser(user.getId(), "Sara Lee");
        Assertions.assertEquals("Sara Lee", updatedUser.getFullName(), "Имя пользователя должно быть обновлено");
    }

    @Test
    void testGetUserById() {
        UserRequest request = new UserRequest();
        request.setFullName("David Brown");
        request.setBirthDate(LocalDate.of(1990, 5, 30));
        request.setJob("Writer");

        User user = userService.createUser(request);
        User foundUser = userService.getUserDtoById(user.getId());
        Assertions.assertNotNull(foundUser, "Пользователь должен быть найден по ID");
        Assertions.assertEquals("David Brown", foundUser.getFullName(), "Имя найденного пользователя должно совпадать");
    }

    @Test
    void testGetAllUsers() {
        UserRequest request1 = new UserRequest();
        request1.setFullName("User One");
        request1.setBirthDate(LocalDate.of(1995, 6, 18));
        request1.setJob("Tester");
        userService.createUser(request1);

        UserRequest request2 = new UserRequest();
        request2.setFullName("User Two");
        request2.setBirthDate(LocalDate.of(1987, 8, 7));
        request2.setJob("Designer");
        userService.createUser(request2);

        List<User> users = userService.getAllUsers();
        Assertions.assertEquals(2, users.size(), "Должно быть возвращено два пользователя");
    }

    @Test
    void testDeleteUser() {
        UserRequest request = new UserRequest();
        request.setFullName("User to Remove");
        request.setBirthDate(LocalDate.of(1994, 11, 25));
        request.setJob("Clerk");

        User user = userService.createUser(request);
        userService.deleteUser(user.getId());

        Assertions.assertThrows(
                RuntimeException.class,
                () -> userService.getUserDtoById(user.getId()),
                "Попытка получить пользователя по ID после удаления должна вызывать исключение"
        );
    }
}
