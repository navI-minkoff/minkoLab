package tech.reliab.course.minkoLab.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.reliab.course.minkoLab.bank.entity.Bank;
import java.util.Optional;

public interface BankRepository extends JpaRepository<Bank, Integer> {
    Optional<Bank> findById(int id);
    void deleteById(int id);
}