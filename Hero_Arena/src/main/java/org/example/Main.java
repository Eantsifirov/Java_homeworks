package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Создание первого персонажа");

        int type1;
        do {
            System.out.print("Тип (1 — Воин, 2 — Маг): ");
            type1 = scanner.nextInt();

            if (type1 != 1 && type1 != 2) {
                System.out.println("Неверный тип персонажа.");
            }
        } while (type1 != 1 && type1 != 2);

        scanner.nextLine();

        System.out.print("Имя: ");
        String name1 = scanner.nextLine();

        System.out.print("Максимальное здоровье: ");
        int maxHealth1 = scanner.nextInt();

        System.out.print("Базовая атака: ");
        int baseAttack1 = scanner.nextInt();

        Hero fighter1;

        if (type1 == 1) {
            System.out.print("Броня: ");
            int armor = scanner.nextInt();
            fighter1 = new Warrior(name1, maxHealth1, baseAttack1, armor);
        } else {
            System.out.print("Максимальная мана: ");
            int maxMana = scanner.nextInt();
            fighter1 = new Mage(name1, maxHealth1, baseAttack1, maxMana);
        }

        System.out.println("Создание второго персонажа");

        int type2;
        do {
            System.out.print("Тип (1 — Воин, 2 — Маг): ");
            type2 = scanner.nextInt();

            if (type2 != 1 && type2 != 2) {
                System.out.println("Неверный тип персонажа.");
            }
        } while (type2 != 1 && type2 != 2);

        scanner.nextLine();

        System.out.print("Имя: ");
        String name2 = scanner.nextLine();

        System.out.print("Максимальное здоровье: ");
        int maxHealth2 = scanner.nextInt();

        System.out.print("Базовая атака: ");
        int baseAttack2 = scanner.nextInt();

        Hero fighter2;

        if (type2 == 1) {
            System.out.print("Броня: ");
            int armor = scanner.nextInt();
            fighter2 = new Warrior(name2, maxHealth2, baseAttack2, armor);
        } else {
            System.out.print("Максимальная мана: ");
            int maxMana = scanner.nextInt();
            fighter2 = new Mage(name2, maxHealth2, baseAttack2, maxMana);
        }

        Arena arena = new Arena(fighter1, fighter2);
        arena.startTournament();
    }
}

