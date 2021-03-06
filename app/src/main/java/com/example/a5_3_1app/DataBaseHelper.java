package com.example.a5_3_1app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String EXERCISES_TABLE = "EXERCISES";
    public static final String COLUMN_NAME = "Name";
    public static final String COLUMN_TRAINING_MAX = "TrainingMax";
    public static final String COLUMN_DAY = "Day";
    public static final String COLUMN_IS_EXTRA_EXERCISE = "IsExtraExercise";

    public static final String CURRENT_CYCLE_PROGRESS_TABLE = "CYCLE_PROGRESS";
    public static final String COLUMN_WEEK = "Week";
    public static final String COLUMN_IS_COMPLETED = "IsCompleted";

    public static final String TRAINING_HISTORY_TABLE = "TRAINING_HISTORY";
    public static final String COLUMN_OHP = "OHP";
    public static final String COLUMN_SQUAT = "Squat";
    public static final String COLUMN_BENCH_PRESS = "BenchPress";
    public static final String COLUMN_DEADLIFT = "Deadlift";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "exercise.db", null, 1);
    }

    // called first time db is accessed
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // create exercises table
        String createTableStatement1 = "CREATE TABLE " + EXERCISES_TABLE + " (" +
                COLUMN_NAME + " TEXT, " + COLUMN_TRAINING_MAX + " INT, " +
                COLUMN_DAY + " INT, " + COLUMN_IS_EXTRA_EXERCISE + " BOOL)";
        sqLiteDatabase.execSQL(createTableStatement1);

        // create table for cycle progress (days completed)
        String createTableStatement2 = "CREATE TABLE " + CURRENT_CYCLE_PROGRESS_TABLE + " (" +
                COLUMN_WEEK + " INT, " + COLUMN_DAY + " INT, " +
                COLUMN_IS_COMPLETED + " BOOL)";
        sqLiteDatabase.execSQL(createTableStatement2);

        // create training history table (workout numbers of previous cycles)
        // only contains workout numbers of the 4 main lifts

        String createTableStatement3 = "CREATE TABLE " + TRAINING_HISTORY_TABLE + "(" + COLUMN_OHP
                + " INT, " + COLUMN_SQUAT +  " INT, " + COLUMN_BENCH_PRESS + " INT, " +
                COLUMN_DEADLIFT + " INT)";
        sqLiteDatabase.execSQL(createTableStatement3);
    }

    // called if database version changes
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    // EXERCISE methods
    // ================

    /**
     * Add an exercise the SQLite database
     * @param exerciseModel object of the exercise to be inserted
     * @return true if successful, false otherwise
     */
    public boolean addExerciseToDB(ExerciseModel exerciseModel) {
        SQLiteDatabase db = this.getWritableDatabase(); // for insertable actions
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME, exerciseModel.getName());
        cv.put(COLUMN_TRAINING_MAX, exerciseModel.getTrainingMax());
        cv.put(COLUMN_DAY, exerciseModel.getDay());
        cv.put(COLUMN_IS_EXTRA_EXERCISE, exerciseModel.isExtraExercise());

        long insert = db.insert(EXERCISES_TABLE, null, cv);
        return (insert != -1);
    }

    /**
     * Query the database to obtain a certain exercise model
     * @param day  the day of the exercise model
     * @param isExtraExercise if the exercise is an extra one
     * @return ExerciseModel object that has that name and day
     */
    public ExerciseModel getExerciseFromDB(int day, boolean isExtraExercise) {
        int isExtraExerciseAsInt = isExtraExercise ? 1: 0;
        String queryString = "SELECT * FROM " + EXERCISES_TABLE + " WHERE "
                + COLUMN_IS_EXTRA_EXERCISE + "='" + isExtraExerciseAsInt + "' AND " + COLUMN_DAY +
                "=" + day;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        // if there is a match with one item in the db
        if (cursor.moveToFirst()) {

            // param represents column of the table
            String exerciseName = cursor.getString(0);
            int exerciseTM = cursor.getInt(1);
            int exerciseDay = cursor.getInt(2);
            boolean isExercise = cursor.getInt(3) == 1 ? true : false;

            cursor.close();
            db.close();
            return new ExerciseModel(exerciseName, exerciseTM, exerciseDay, isExercise);

        } else {
            cursor.close();
            db.close();
            return null;
        }
    }

    /**
     * Update an exercise in the database
     * @param exerciseModel The exercise to be updated
     * @return boolean value if the update was successful
     */
    public boolean updateExercise(ExerciseModel exerciseModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TRAINING_MAX, exerciseModel.getTrainingMax());

        int numOfRows;

        // if extra exercise, update name field as well
        if (exerciseModel.isExtraExercise()) {
            cv.put(COLUMN_NAME, exerciseModel.getName());
            numOfRows = db.update(EXERCISES_TABLE, cv, COLUMN_DAY + "=? AND " +
                    COLUMN_IS_EXTRA_EXERCISE + "=?", new String[]{
                    String.valueOf(exerciseModel.getDay()), String.valueOf(1)});

            // only update training max
        } else {
            numOfRows = db.update(EXERCISES_TABLE, cv, COLUMN_NAME + "=? AND " +
                    COLUMN_DAY + "=?", new String[]{
                    exerciseModel.getName(), String.valueOf(exerciseModel.getDay())});
        }

        return numOfRows == 1;
    }

    /** Searches the db for the exercise. If already in the db, update the exercise. Otherwise
     * add the exercise.
     * @param exerciseModel
     * @return true if add or update was sucessful, false otherwise.
     */
    public boolean addOrUpdateExercise(ExerciseModel exerciseModel) {
        ExerciseModel exerciseModelInDB = getExerciseFromDB(
                exerciseModel.getDay(), exerciseModel.isExtraExercise());

        // not in the db therefore add it into the db
        if (exerciseModelInDB == null) {
            return addExerciseToDB(exerciseModel);

            // is in the db therefore update the one in the db
        } else {
            return updateExercise(exerciseModel);
        }
    }

    /**
     * Returns a list of all the exercises from the db as ExerciseModel objects
     * @return List of all ExerciseModels
     */
    public List<ExerciseModel> getAllExercises() {
        List<ExerciseModel> allExercises = new ArrayList<>();

        String queryString = "SELECT * FROM " + EXERCISES_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {
            do {
                String exerciseName = cursor.getString(0);
                int exerciseTM = cursor.getInt(1);
                int exerciseDay = cursor.getInt(2);
                boolean isExercise = cursor.getInt(3) == 1 ? true : false;

                ExerciseModel exerciseModel = new ExerciseModel(exerciseName, exerciseTM,
                        exerciseDay, isExercise);

                allExercises.add(exerciseModel);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return allExercises;
    }


    // CYCLE_PROGRESS_TABLE methods
    // ============================

    /**
     * Get the progress of all the days in the current cycle
     * @return a list of boolean values representing a finished day in the cycle or not
     */
    public List<Boolean> getAllCycleProgress() {
        List<Boolean> allDaysCompletedStatus = new ArrayList<>();

        String queryString = "SELECT * FROM " + CURRENT_CYCLE_PROGRESS_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {
            do {
                boolean dayCompletedStatus = cursor.getInt(2) == 1 ? true : false;
                allDaysCompletedStatus.add(dayCompletedStatus);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return allDaysCompletedStatus;
    }

    /**
     * If CYCLE_PROGRESS table not populated, populate the database with
     * all cycle days as NOT completed
     */
    public void populateCycleProgressTable() {
        List<Boolean> allCycleProgress = getAllCycleProgress();

        if (allCycleProgress.size() == 0) {
            SQLiteDatabase db = this.getWritableDatabase();

            for (int i = 1; i < 4; i++) {
                for (int j = 1; j < 5; j++) {
                    ContentValues cv = new ContentValues();

                    cv.put(COLUMN_WEEK, i);
                    cv.put(COLUMN_DAY, j);
                    cv.put(COLUMN_IS_COMPLETED, false);
                    db.insert(CURRENT_CYCLE_PROGRESS_TABLE, null, cv);
                }
            }
        }
    }

    /**
     * Updates a day in the cycle to be completed
     * @param week the week of the cycle to be updated
     * @param day the day of the cycle to be updated
     * @param newStatus the new boolean value if the day in the cycle is completed or not
     * @return true if update was successful, false otherwise
     */
    public boolean updateCycleProgress(int week, int day, boolean newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_IS_COMPLETED, newStatus);
        int numOfRows = db.update(CURRENT_CYCLE_PROGRESS_TABLE, cv, COLUMN_WEEK + "=? AND "
            + COLUMN_DAY + "=?", new String[] {String.valueOf(week), String.valueOf(day)});

        return numOfRows == 1;
    }

    /**
     * Set all cycle progress to false to reset
     * @return
     */
    public boolean resetProgressCycle() {
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 4; j++) {

                if(!updateCycleProgress(i, j, false)) {
                    return false;
                }
            }
        }

        return true;
    }

// TRAINING_HISTORY_TABLE methods
// ==============================

    /**
     * Adds cycle to training history database
     * @param cycle the cycle to be added
     */
    public boolean addToTrainingHistoryDB(CycleModel cycle) {
        SQLiteDatabase db = this.getWritableDatabase(); // for insertable actions
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_OHP, cycle.getOhpTM());
        cv.put(COLUMN_SQUAT, cycle.getSquatTM());
        cv.put(COLUMN_BENCH_PRESS, cycle.getBenchPressTM());
        cv.put(COLUMN_DEADLIFT, cycle.getDeadliftTM());

        long insert = db.insert(TRAINING_HISTORY_TABLE, null, cv);
        return (insert != -1);
    }

    /**
     * Grabs all previous cycles
     * @return list of previous cycles
     */
    public List<CycleModel> getTrainingHistory() {
        List<CycleModel> allCycles = new ArrayList<>();

        String queryString = "SELECT * FROM " + TRAINING_HISTORY_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {
            do {

                // param represents column of the table
                int ohpTM = cursor.getInt(0);
                int squatTM = cursor.getInt(1);
                int benchTM = cursor.getInt(2);
                int deadliftTM = cursor.getInt(3);

                CycleModel cycleModel = new CycleModel(ohpTM, squatTM, benchTM, deadliftTM);
                allCycles.add(cycleModel);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return allCycles;
    }

 }
