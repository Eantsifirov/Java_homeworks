package org.example;

import java.util.Scanner;
import java.util.Locale;

enum BankType {
    NEO("НеоКредит Банк", 0.01),
    AUM("Арум Финтех", 0.02),
    VTA("Вектор Альянс Банк", 0.00);

    public final String bankName;
    public final double commission;

    BankType(String bankName, double commission) {
        this.bankName = bankName;
        this.commission = commission;
    }

    public String getName() {
        return bankName;
    }

    public double getCommission() {
        return commission;
    }
}

// Мои примечания по энумам:
// enum задаёт конечный пул допустимых значений;
// Значения enum - уже созданные объекты этого типа;
// Каждому значению данные можно передать через конструктор;
// Однако, конструктор необязателен, если нет дополнительных данных.

interface WithdrawalOperations {
    double withdrawMoney(double accountBalance, Double requiredSum, BankType bankType);

    default double applyCommission(Double requiredSum, BankType bankType) {
        if (requiredSum == null || bankType == null) {
            return 0;
        }

        return Math.round(requiredSum * bankType.getCommission() * 100) / 100.0;
    }
}

interface DepositOperations {
    double insertMoney(double accountBalance, Double inputSum);
}

// Мои примечания по интерфейсам:
// interface - соглашение о том, что должен уметь объект;
// если объект имплементирует интерфейс, в нём должна быть описанная функция;
// default-метод интерфейса уже содержит готовую реализацию.

class Account {
    public int cardNumber;
    public int pin;
    public double accountBalance;
    public BankType bankType;

    Account(int cardNumber, int pin, double accountBalance, BankType bankType) {

        // Если номер карты невалиден, то установим 10000 по умолчанию
        if (cardNumber < 10000 || 99999 < cardNumber) {
            System.out.println("Невалидный номер карты. Используется 10000");
            this.cardNumber = 10000;
        } else {
            this.cardNumber = cardNumber;
        }

        // Если пин невалиден, то установим 100:
        if (pin < 100 || 999 < pin) {
            System.out.println("Невалидный пин. Используется 100");
            this.pin = 100;
        } else {
            this.pin = pin;
        }

        if (bankType == null) {
            this.bankType = BankType.NEO;
        } else {
            this.bankType = bankType;
        }

        if (accountBalance < 0 || !Double.isFinite(accountBalance)) {
            this.accountBalance = 0;
        } else {
            this.accountBalance = Math.round(accountBalance * 100) / 100.0;
        }
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public int getPin() {
        return pin;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public BankType getBankType() {
        return bankType;
    }

    @Override
    public String toString() {
        return bankType.getName() + " Карта: " + cardNumber + ", Баланс: " + String.format(Locale.US, "%.2f", accountBalance) + " руб.";
    }
}

class CashMachine implements WithdrawalOperations, DepositOperations {

    @Override
    public double insertMoney(double accountBalance, Double inputSum) {
        if (inputSum == null || inputSum < 0 || !Double.isFinite(inputSum)) {
            System.out.println("Попытка отрицательного пополнения отклонена.");
            return accountBalance;
        } else {
            return Math.round((accountBalance + inputSum) * 100) / 100.0;
        }
    }

    @Override
    public double withdrawMoney(double accountBalance, Double requiredSum, BankType bankType) {
        if (requiredSum == null || requiredSum <= 0 || !Double.isFinite(requiredSum)) {
            System.out.println("Недопустимое значение суммы списания");
            return accountBalance;
        }

        double commission = applyCommission(requiredSum, bankType);
        double finalSum = requiredSum + commission;

        if (finalSum > accountBalance) {
            System.out.println("Недостаточно средств для списания");
            return accountBalance;
        }

        return Math.round((accountBalance - finalSum) * 100) / 100.0;
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Account account = new Account(12345, 999, 10000.00, BankType.AUM);

        System.out.println("Добрый день! Введите номер карты и пин-код.");

        if (!scanner.hasNextInt()) {
            System.out.println("Введён некорректный номер карты");
            return;
        }
        int cardNumber = scanner.nextInt();

        if (!scanner.hasNextInt()) {
            System.out.println("Введён некорректный пин");
            return;
        }
        int pin = scanner.nextInt();

        if (cardNumber != account.getCardNumber() || pin != account.getPin()) {
            System.out.println("Введены неверные данные. В доступе отказано.");
            return;
        }

        CashMachine tester = new CashMachine();

        //

        if (!scanner.hasNextDouble()) {
            System.out.println("Некорректная сумма пополнения");
            return;
        }
        double testInput = scanner.nextDouble();

        account.accountBalance = tester.insertMoney(account.getAccountBalance(), testInput);
        System.out.println(account.accountBalance);

        if (!scanner.hasNextDouble()) {
            System.out.println("Некорректная сумма снятия");
            return;
        }
        double testWithdrawal = scanner.nextDouble();

        account.accountBalance = tester.withdrawMoney(account.getAccountBalance(), testWithdrawal, account.getBankType());
        System.out.println(account.accountBalance);
    }
}