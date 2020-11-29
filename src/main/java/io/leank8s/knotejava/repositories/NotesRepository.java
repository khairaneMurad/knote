package io.leank8s.knotejava.repositories;

import io.leank8s.knotejava.domain.Note;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotesRepository extends MongoRepository<Note, String> {

}
