package com.example.roommvvmdemo.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

// @Dao regroupe les opérations d'accès à la base de données
@Dao
public interface NoteDao {

    @Insert
    void insert(Note note);

    @Delete
    void delete(Note note);

    @Query("DELETE FROM notes_table")
    void deleteAllNotes();

    // Retourne un LiveData : l'UI sera notifiée automatiquement en cas de modification
    @Query("SELECT * FROM notes_table ORDER BY id DESC")
    LiveData<List<Note>> getAllNotes();
}