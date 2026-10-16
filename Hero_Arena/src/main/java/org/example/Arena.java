package org.example;

public class Arena {
    private final Hero fighter1;
    private final Hero fighter2;
    private int roundCounter = 0;

    Arena(Hero hero1, Hero hero2) {
        if (hero1 == null || hero2 == null) {
            throw new IllegalArgumentException("Герои не могут быть null");
        }

        this.fighter1 = hero1;
        this.fighter2 = hero2;
    }

    void startTournament() {
        while (fighter1.isAlive() && fighter2.isAlive()) {
            roundCounter++;
            System.out.println("Раунд " + roundCounter);

            int actor = (int) (Math.random() * 2) + 1;

            Hero attacker, defender;
            if (actor == 1) {
                attacker = fighter1;
                defender = fighter2;
            } else {
                defender = fighter1;
                attacker = fighter2;
            }

            ActionType action = attacker.makeTurn(defender);
            System.out.println("Действие " + attacker.getName() + ": " + action.getDescription());

            if (!defender.isAlive()) {
                System.out.println(attacker.getName() + " победил");
                break;
            }

            action = defender.makeTurn(attacker);
            System.out.println("Действие " + defender.getName() + ": " + action.getDescription());

            if (!attacker.isAlive()) {
                System.out.println(defender.getName() + " победил");
                break;
            }


            System.out.println(attacker.getStatus());
            System.out.println(defender.getStatus());
        }
    }
}
