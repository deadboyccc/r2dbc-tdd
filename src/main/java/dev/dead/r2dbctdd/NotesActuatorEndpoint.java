package dev.dead.r2dbctdd;

import org.springframework.boot.actuate.endpoint.annotation.DeleteOperation;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
@Endpoint(id = "notes")
public class NotesActuatorEndpoint {

    private final List<Note> notes = new ArrayList<>();

    @ReadOperation
    public List<Note> notes() {
        return notes;
    }

    @WriteOperation
    public void addNote(String text) {
        notes.add(new Note(text));
    }

    @DeleteOperation
    public void clearNotes() {
        notes.clear();
    }

    record Note(String text, Instant time) {
        Note(String text) {
            this(text, Instant.now());
        }
    }
}
