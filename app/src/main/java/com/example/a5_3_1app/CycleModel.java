package com.example.a5_3_1app;

/**
 * Class that represents the 4 main exercises of a cycle. Mainly used for training history
 * activity
 */
public class CycleModel {
    private final int ohpTM;
    private final int squatTM;
    private final int benchPressTM;
    private final int deadliftTM;

    public CycleModel(int ohpTM, int squatTM, int benchPressTM, int deadliftTM) {
        this.ohpTM = ohpTM;
        this.squatTM = squatTM;
        this.benchPressTM = benchPressTM;
        this.deadliftTM = deadliftTM;
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
}
