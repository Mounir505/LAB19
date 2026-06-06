package com.example.roommvvmdemo.data.local;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

// @Entity indique à Room de créer une table SQLite nommée "notes_table"
@Entity(tableName = "notes_table")
public class Note {

    // La clé primaire est générée automatiquement par Room
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String description;

    // Le constructeur ne prend pas l'ID car il est auto-généré
    public Note(String title, String description) {
        this.title = title;
        this.description = description;
    }

    // Getters et Setters nécessaires pour que Room puisse lire et écrire les données
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
}