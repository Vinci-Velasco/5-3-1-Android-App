package com.example.a5_3_1app;

/**
 * Class that represents the exercise model which stores properties of an exercise including:
 * name, training max number, corresponding day, and if it's an extra exercise
 */
public class ExerciseModel {
    private String name;
    private int trainingMax;
    private int day;
    private boolean isExtraExercise;

    public ExerciseModel(String name, int trainingMax, int day, boolean isExtraExercise) {
        this.name = name;
        this.trainingMax = trainingMax;
        this.day = day;
        this.isExtraExercise = isExtraExercise;
    }

    @Override
    public String toString() {
        return "ExerciseModel{" +
                "name='" + name + '\'' +
                ", trainingMax=" + trainingMax +
                ", day=" + day +
                ", isExtraExercise=" + isExtraExercise +
                '}';
    }

    // getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTrainingMax() {
        return trainingMax;
    }

    public void setTrainingMax(int trainingMax) {
        this.trainingMax = trainingMax;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public boolean isExtraExercise() {
        return isExtraExercise;
    }

    public void setExtraExercise(boolean extraExercise) {
        isExtraExercise = extraExercise;
    }
}
