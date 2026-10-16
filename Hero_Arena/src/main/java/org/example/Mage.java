package org.example;

public class Mage extends Hero implements Restable, Castable {
    private final int maxMana;
    private int mana;

    public Mage(String name, int maxHealth, int baseAttack, int maxMana) {
        super(name, maxHealth, baseAttack);
        this.mana = Math.max(0, maxMana);
        this.maxMana = Math.max(0, maxMana);
    }

    @Override
    public void attack(Hero target) {
        if (target == null) {
            System.out.println("Ошибка: цель отсутствует");
            return;
        }

        if (mana >= 10) {
            int damage = getBaseAttack() * 2;
            mana -= 10;
            System.out.println("Каст нанёс " + target.getName() + " " + damage + " единиц урона");
            target.takeDamage(damage);
        } else {
            int damage = getBaseAttack() / 2;
            mana = Math.min(maxMana, mana + 5);
            System.out.println("Удар посохом нанёс " + target.getName() + " " + damage + " единиц урона");
            target.takeDamage(damage);
        }
    }

    @Override
    public ActionType makeTurn(Hero target) {
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
        return (mana >= 25);
    }

    @Override
    public void castSpecialSkill(Hero target) {
        if (target == null) {
            System.out.println("Ошибка: цель отсутствует");
            return;
        }

        if (!canCast()) {
            System.out.println("Заклинание сорвалось");
            return;
        }

        int specialDamage = getBaseAttack() * 3;
        mana -= 25;
        System.out.println("Специальное заклинание нанесло " + target.getName() + " " + specialDamage + " урона");
        target.takeDamage(specialDamage);
    }

    @Override
    public boolean needsRest() {
        return (mana == 0 || getHealth() < 0.3 * getMaxHealth());
    }

    @Override
    public void rest() {
        mana = maxMana;
        int restored = Math.min(getBaseAttack(), getMaxHealth() - getHealth());
        heal(restored);
        System.out.println("Восстановлено " + restored + " здоровья и полностью восстановлена мана");
    }

    public String getStatus() {
        return getName() + ": здоровье " + getHealth() + "/" + getMaxHealth() + ", броня " + mana;
    }
}
