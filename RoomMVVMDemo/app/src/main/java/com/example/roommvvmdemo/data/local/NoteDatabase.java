package com.example.roommvvmdemo.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

// Déclaration de la base avec ses entités et sa version
@Database(entities = {Note.class}, version = 1, exportSchema = false)
public abstract class NoteDatabase extends RoomDatabase {

    public abstract NoteDao noteDao();

    // Instance unique (Singleton) pour éviter les fuites de mémoire
    private static volatile NoteDatabase instance;

    public static NoteDatabase getInstance(Context context) {
        if (instance == null) {
            // Synchronized garantit qu'un seul thread peut créer l'instance à la fois
            synchronized (NoteDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    NoteDatabase.class,
                                    "notes_database"
                            )
                            // En développement : détruit et recrée la base si la version change
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instance;
    }
}