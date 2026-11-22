package com.example.taskmanager.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void getAllTasksReturnsRepositoryResults() {
        List<Task> tasks = List.of(new Task("Write tests", "Cover service behavior", false));
        when(taskRepository.findAll()).thenReturn(tasks);

        assertEquals(tasks, taskService.getAllTasks());
    }

    @Test
    void createTaskSavesRequestFields() {
        TaskRequest request = new TaskRequest("Write tests", "Cover service behavior", true);
        when(taskRepository.save(org.mockito.ArgumentMatchers.any(Task.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Task result = taskService.createTask(request);

        assertEquals("Write tests", result.getTitle());
        assertEquals("Cover service behavior", result.getDescription());
        assertEquals(true, result.isCompleted());
    }

    @Test
    void updateTaskChangesExistingTask() {
        Task existing = new Task("Old title", "Old description", false);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(taskRepository.save(existing)).thenReturn(existing);

        Task result = taskService.updateTask(1L, new TaskRequest("New title", "New description", true));

        assertEquals("New title", result.getTitle());
        assertEquals("New description", result.getDescription());
        assertEquals(true, result.isCompleted());
    }

    @Test
    void getTaskThrowsWhenTaskDoesNotExist() {
        when(taskRepository.findById(42L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.getTask(42L));
    }

    @Test
    void deleteTaskDeletesExistingTask() {
        Task existing = new Task("Delete me", null, false);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existing));

        taskService.deleteTask(1L);

        verify(taskRepository).delete(existing);
    }
}
