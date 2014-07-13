package ie.ianduffy.todo.web.rest;

import ie.ianduffy.todo.Application;
import ie.ianduffy.todo.domain.Todo;
import ie.ianduffy.todo.domain.User;
import ie.ianduffy.todo.repository.TodoRepository;
import ie.ianduffy.todo.repository.UserRepository;
import ie.ianduffy.todo.security.SecurityUtils;
import ie.ianduffy.todo.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.inject.Inject;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the TodoResource REST controller.
 *
 * @see TodoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
@ActiveProfiles("dev")
public class TodoResourceTest {

    private static final Long DEFAULT_ID = new Long(1L);

    private static final Boolean DEFAULT_SAMPLE_COMPLETED_ATTR = false;

    private static final String DEFAULT_SAMPLE_TASK_ATTR = "sampleTaskAttribute";

    private static final Boolean UPDATED_SAMPLE_COMPLETED_ATTR = true;

    @Inject
    private TodoRepository todoRepository;

    @Inject
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private SecurityUtils SecurityUtils;

    private MockMvc restTodoMockMvc;

    private Todo todo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TodoResource todoResource = new TodoResource();
        ReflectionTestUtils.setField(todoResource, "todoRepository", todoRepository);
        ReflectionTestUtils.setField(todoResource, "userService", userService);

        this.restTodoMockMvc = MockMvcBuilders.standaloneSetup(todoResource).build();

        todo = new Todo();
        todo.setId(DEFAULT_ID);
        todo.setTask(DEFAULT_SAMPLE_TASK_ATTR);
        todo.setCompleted(DEFAULT_SAMPLE_COMPLETED_ATTR);
    }

    @Test
    public void testCRUDTodo() throws Exception {
        User user = userRepository.findOne("user");
        when(userService.getUserWithAuthorities()).thenReturn(user);

        // Create Todo
        restTodoMockMvc.perform(post("/app/rest/todos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(todo)))
                .andExpect(status().isCreated());

        // Read Todo
        restTodoMockMvc.perform(get("/app/rest/todos/{id}", DEFAULT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(DEFAULT_ID.intValue()))
                .andExpect(jsonPath("$.task").value(DEFAULT_SAMPLE_TASK_ATTR.toString()))
                .andExpect(jsonPath("$.completed").value(DEFAULT_SAMPLE_COMPLETED_ATTR));

    	// Update Todo
    	todo.setCompleted(UPDATED_SAMPLE_COMPLETED_ATTR);

    	restTodoMockMvc.perform(post("/app/rest/todos")
    			.contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(todo)))
                .andExpect(status().isCreated());

    	// Read updated Todo
    	restTodoMockMvc.perform(get("/app/rest/todos/{id}", DEFAULT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(DEFAULT_ID.intValue()))
    			.andExpect(jsonPath("$.completed").value(UPDATED_SAMPLE_COMPLETED_ATTR));

        // Delete Todo
        restTodoMockMvc.perform(delete("/app/rest/todos/{id}", DEFAULT_ID)
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Read nonexisting Todo
        restTodoMockMvc.perform(get("/app/rest/todos/{id}", DEFAULT_ID)
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());

    }
}
