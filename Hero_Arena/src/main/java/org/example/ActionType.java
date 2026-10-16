package org.example;

public enum ActionType {
    BASE_ATTACK("Атака"),
    SPECIAL_SKILL("Специальный навык"),
    REST("Пропуск хода");

    private final String description;

    ActionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

