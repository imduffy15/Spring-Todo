package ie.ianduffy.todo.repository;

import ie.ianduffy.todo.domain.Todo;
import ie.ianduffy.todo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the Todo entity.
 */
public interface TodoRepository extends JpaRepository<Todo, Long> {

    Todo findByIdAndUser(Long id, User User);

    List<Todo> findByUser(User User);
}
