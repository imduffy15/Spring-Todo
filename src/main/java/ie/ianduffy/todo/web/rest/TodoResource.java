package ie.ianduffy.todo.web.rest;

import com.codahale.metrics.annotation.Timed;
import ie.ianduffy.todo.domain.Todo;
import ie.ianduffy.todo.domain.User;
import ie.ianduffy.todo.repository.TodoRepository;
import ie.ianduffy.todo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * REST controller for managing Todo.
 */
@RestController
@RequestMapping("/app")
public class TodoResource {

    private final Logger log = LoggerFactory.getLogger(TodoResource.class);

    @Inject
    private TodoRepository todoRepository;

    @Inject
    private UserService userService;

    /**
     * POST  /rest/todos -> Create a new todo.
     */
    @RequestMapping(value = "/rest/todos",
            method = RequestMethod.POST,
            produces = "application/json")
    @Timed
    public ResponseEntity create(@RequestBody Todo todo) {
        log.debug("REST request to save Todo : {}", todo);
        User user = userService.getUserWithAuthorities();

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        todo.setUser(user);

        todoRepository.save(todo);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * GET  /rest/todos -> get all the todos.
     */
    @RequestMapping(value = "/rest/todos",
            method = RequestMethod.GET,
            produces = "application/json")
    @Timed
    public ResponseEntity<List<Todo>> getAll() {
        log.debug("REST request to get all Todos");
        User user = userService.getUserWithAuthorities();

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(todoRepository.findByUser(user), HttpStatus.OK);
    }

    /**
     * GET  /rest/todos/:id -> get the "id" todo.
     */
    @RequestMapping(value = "/rest/todos/{id}",
            method = RequestMethod.GET,
            produces = "application/json")
    @Timed
    public ResponseEntity<Todo> get(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Todo : {}", id);

        User user = userService.getUserWithAuthorities();

        Todo todo = todoRepository.findByIdAndUser(id, user);
        if (todo == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(todo, HttpStatus.OK);
    }

    /**
     * DELETE  /rest/todos/:id -> delete the "id" todo.
     */
    @RequestMapping(value = "/rest/todos/{id}",
            method = RequestMethod.DELETE,
            produces = "application/json")
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Todo : {}", id);
        todoRepository.delete(id);
    }
}
