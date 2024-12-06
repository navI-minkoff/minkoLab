package tech.reliab.course.minkoLab.bank.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Employee {

    private int id;
    private String fullName;
    private LocalDate birthDate;
    private String position;
    private Bank bank;
    private boolean remoteWork;
    private BankOffice bankOffice;
    private boolean canIssueLoans;
    private double salary;

    public Employee(String fullName, LocalDate birthDate, String position, Bank bank, boolean remoteWork, BankOffice bankOffice, boolean canIssueLoans, double salary) {
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.position = position;
        this.bank = bank;
        this.remoteWork = remoteWork;
        this.bankOffice = bankOffice;
        this.canIssueLoans = canIssueLoans;
        this.salary = salary;
    }


    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", birthDate=" + birthDate +
                ", position='" + position + '\'' +
                ", bank=" + bank.getName() +
                ", remoteWork=" + remoteWork +
                ", bankOffice=" + (bankOffice != null ? bankOffice.getName() : "None") +
                ", canIssueLoans=" + canIssueLoans +
                ", salary=" + salary +
                '}';
    }
}