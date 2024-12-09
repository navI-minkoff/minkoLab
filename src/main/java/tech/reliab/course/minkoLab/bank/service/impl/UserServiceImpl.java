package tech.reliab.course.minkoLab.bank.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.reliab.course.minkoLab.bank.entity.User;
import tech.reliab.course.minkoLab.bank.model.UserRequest;
import tech.reliab.course.minkoLab.bank.repository.UserRepository;
import tech.reliab.course.minkoLab.bank.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final int MONTHLY_INCOME_BOUND = 10001;
    private static final double DIVIDER = 1000.0;
    private static final int FACTOR = 100;

    private final UserRepository userRepository;

    public User createUser(UserRequest userRequest) {
        User user = new User(userRequest.getFullName(), userRequest.getBirthDate(), userRequest.getJob());
        user.setMonthlyIncome(generateMonthlyIncome());
        user.setCreditRating(generateCreditRating(user.getMonthlyIncome()));
        return userRepository.save(user);
    }

    private int generateMonthlyIncome() {
        return new Random().nextInt(MONTHLY_INCOME_BOUND);
    }


    private int generateCreditRating(double monthlyIncome) {
        return (int) Math.ceil(monthlyIncome / DIVIDER) * FACTOR;
    }

    public User getUserById(int id) {
        return userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("User was not found"));
    }

    public User getUserDtoById(int id) {
        return getUserById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(int id, String name) {
        User user = getUserById(id);
        user.setFullName(name);
        return userRepository.save(user);
    }

    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }
}