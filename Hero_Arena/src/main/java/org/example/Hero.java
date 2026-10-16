package org.example;

abstract class Hero implements Castable, Restable {
    private static final int MIN_STAT_VALUE = 1;

    private final String name;
    private final int maxHealth;
    private final int baseAttack;
    private int health;

    Hero(String name, int maxHealth, int baseAttack) {
        this.name = name == null || name.isBlank() ? "Безымянный" : name;

        if (maxHealth >= MIN_STAT_VALUE) {
            this.health = maxHealth;
            this.maxHealth = maxHealth;
        } else {
            this.health = 1;
            this.maxHealth = 1;
        }

        if (baseAttack >= MIN_STAT_VALUE) {
            this.baseAttack = baseAttack;
        } else {
            this.baseAttack = 1;
        }
    }

    public String getName() {
        return name;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getBaseAttack() {
        return baseAttack;
    }

    public int getHealth() {
        return health;
    }

    abstract void attack(Hero target);
    abstract ActionType makeTurn(Hero target);

    void takeDamage(int damage) {
        if (!isAlive()) {
            return;
        }

        if (damage <= 0) {
            System.out.println("Некорректное значение урона!");
            return;
        }

        if (health > damage) {
            health -= damage;
            return;
        } else {
            health = 0;
            System.out.println(name + " пал в бою!");
        }
    }

    void heal(int amount) {
        if (amount <= 0) {
            System.out.println("Некорректное значение исцеления");
            return;
        }

        if (health + amount <= maxHealth){
            health += amount;
        } else {
            health = maxHealth;
        }

        return;
    }

    boolean isAlive() {
        return health > 0;
    }

    public abstract String getStatus();
}
