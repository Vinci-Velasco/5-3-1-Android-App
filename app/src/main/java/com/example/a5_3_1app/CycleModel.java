package com.example.a5_3_1app;

import java.time.LocalDate;

/**
 * Class that represents the 4 main exercises of a cycle. Mainly used for training history
 * activity
 */
public class CycleModel {
    private final int ohpTM;
    private final int squatTM;
    private final int benchPressTM;
    private final int deadliftTM;
    private final LocalDate date;

    public CycleModel(int ohpTM, int squatTM, int benchPressTM, int deadliftTM, LocalDate date) {
        this.ohpTM = ohpTM;
        this.squatTM = squatTM;
        this.benchPressTM = benchPressTM;
        this.deadliftTM = deadliftTM;
        this.date = date;
    }

    public int getOhpTM() {
        return ohpTM;
    }

    public int getSquatTM() {
        return squatTM;
    }

    public int getBenchPressTM() {
        return benchPressTM;
    }

    public int getDeadliftTM() {
        return deadliftTM;
    }

    public LocalDate getDate() {
        return date;
    }
}
