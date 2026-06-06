package com.example.roommvvmdemo.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.roommvvmdemo.data.NoteRepository;
import com.example.roommvvmdemo.data.local.Note;

import java.util.List;

// On utilise AndroidViewModel car le Repository a besoin du contexte de l'Application
public class NoteViewModel extends AndroidViewModel {

    private final NoteRepository repository;
    private final LiveData<List<Note>> allNotes;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteRepository(application);
        allNotes = repository.getAllNotes();
    }

    public void insert(Note note) {
        repository.insert(note);
    }

    public void delete(Note note) {
        repository.delete(note);
    }

    public void deleteAllNotes() {
        repository.deleteAllNotes();
    }

    // Expose les données pour que l'Activity puisse les observer
    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }
}