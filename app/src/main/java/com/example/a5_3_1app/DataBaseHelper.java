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

    public static final String EXERCISES = "EXERCISES";
    public static final String COLUMN_NAME = "Name";
    public static final String COLUMN_TRAINING_MAX = "TrainingMax";
    public static final String COLUMN_DAY = "Day";
    public static final String COLUMN_IS_EXTRA_EXERCISE = "IsExtraExercise";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "exercise.db", null, 1);
    }

    // called first time db is accessed
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableStatement = "CREATE TABLE " + EXERCISES + " (" +
                COLUMN_NAME + " TEXT, " + COLUMN_TRAINING_MAX + " INT, " +
                COLUMN_DAY + " INT, " + COLUMN_IS_EXTRA_EXERCISE + " BOOL)";
        sqLiteDatabase.execSQL(createTableStatement);
    }

    // called if database version changes
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    /**
     * Add an exercise the SQLite database
     *
     * @param exerciseModel object of the exercise to be inserted
     * @return true if successful, false otherwise
     */
    public boolean addToDB(ExerciseModel exerciseModel) {
        SQLiteDatabase db = this.getWritableDatabase(); // for insertable actions
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME, exerciseModel.getName());
        cv.put(COLUMN_TRAINING_MAX, exerciseModel.getTrainingMax());
        cv.put(COLUMN_DAY, exerciseModel.getDay());
        cv.put(COLUMN_IS_EXTRA_EXERCISE, exerciseModel.isExtraExercise());

        long insert = db.insert(EXERCISES, null, cv);
        return (insert != -1);
    }

    /**
     * Query the database to obtain a certain exercise model
     *
     * @param day  the day of the exercise model
     * @param isExtraExercise if the exercise is an extra one
     * @return ExerciseModel object that has that name and day
     */
    public ExerciseModel getExerciseFromDB(int day, boolean isExtraExercise) {
        int isExtraExerciseAsInt = isExtraExercise ? 1: 0;
        String queryString = "SELECT * FROM " + EXERCISES + " WHERE "
                + COLUMN_IS_EXTRA_EXERCISE + "='" + isExtraExerciseAsInt + "' AND " + COLUMN_DAY +
                "=" + day;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        // if there is a match with one item in the db
        if (cursor.moveToFirst()) {
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

    /** Searches the db for the exercise. If already in the db, update the exercise. Otherwise
     * add the exercise.
     * @param exerciseModel
     * @return true if add or update was sucessful, false otherwise.
     */
    public boolean addOrUpdate(ExerciseModel exerciseModel) {
        ExerciseModel exerciseModelInDB = getExerciseFromDB(
                exerciseModel.getDay(), exerciseModel.isExtraExercise());

        // not in the db therefore add it into the db
        if (exerciseModelInDB == null) {
            return addToDB(exerciseModel);

            // is in the db therefore update the one in the db
        } else {
            return updateExercise(exerciseModel);
        }
    }

    /**
     * Returns a list of all the exercises from the db as ExerciseModel objects
     * @return List of all ExerciseModels
     */
    public List<ExerciseModel> getAll() {
        List<ExerciseModel> allExercises = new ArrayList<>();

        String queryString = "SELECT * FROM " + EXERCISES;
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
        } else {

        }

        cursor.close();
        db.close();
        return allExercises;
    }

    // just for testing purposes
    public void deleteAllRecords() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ EXERCISES);
    }

    // update the exercise in the db
    private boolean updateExercise(ExerciseModel exerciseModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TRAINING_MAX, exerciseModel.getTrainingMax());

        int numOfRows;

        // if extra exercise, update name field as well
        if (exerciseModel.isExtraExercise()) {
            cv.put(COLUMN_NAME, exerciseModel.getName());
            numOfRows = db.update(EXERCISES, cv, COLUMN_DAY + "=? AND " +
                    COLUMN_IS_EXTRA_EXERCISE + "=?", new String[]{
                    String.valueOf(exerciseModel.getDay()), String.valueOf(1)});

            // only update training max
        } else {
            numOfRows = db.update(EXERCISES, cv, COLUMN_NAME + "=? AND " +
                    COLUMN_DAY + "=?", new String[]{
                    exerciseModel.getName(), String.valueOf(exerciseModel.getDay())});
        }

        return numOfRows == 1;
    }
 }
