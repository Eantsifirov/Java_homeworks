package org.example;

public class Warrior extends Hero implements Restable, Castable {
    private final int armor;

    public Warrior(String name, int maxHealth, int baseAttack, int armor) {
        super(name, maxHealth, baseAttack);
        this.armor = Math.max(armor, 0);
    }

    @Override
    void takeDamage(int damage) {
        int actualDamage = Math.max(damage - armor, 1);
        super.takeDamage(actualDamage);
    }

    @Override
    void attack(Hero target) {
        if (target == null) {
            System.out.println("Ошибка: цель отсутствует");
            return;
        }

        System.out.println(target.getName() + " нанесён урон: " + getBaseAttack());
        target.takeDamage(getBaseAttack());
    }

    @Override
    ActionType makeTurn(Hero target) {
        if (canCast()) {
            castSpecialSkill(target);
            return ActionType.SPECIAL_SKILL;
        } else if (needsRest()) {
            rest();
            return ActionType.REST;
        } else {
            attack(target);
            return ActionType.BASE_ATTACK;
        }
    }

    @Override
    public boolean canCast() {
        if (getHealth() < 0.5 * getMaxHealth()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void castSpecialSkill(Hero target) {
        if (target == null) {
            System.out.println("Ошибка: цель отсутствует");
            return;
        }

        if (!canCast()) {
            System.out.println("Воин попытался ударить щитом, но потерял равновесие");
            return;
        }

        int damage = getBaseAttack() + armor * 2;
        System.out.println(getName() + " ударил щитом");
        target.takeDamage(damage);
    }

    @Override
    public boolean needsRest() {
        return (getHealth() < 0.15 * getMaxHealth());
    }

    @Override
    public void rest() {
        int restored = Math.min(getMaxHealth() - getHealth(), armor * 2);
        heal(armor * 2);
        System.out.println("Восстановлено " + restored + "единиц здоровья");
    }

    @Override
    public String getStatus() {
        return getName() + ": здоровье " + getHealth() + "/" + getMaxHealth() + ", броня " + armor;
    }
}
