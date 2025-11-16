package com.erp.erp.workflow.engine;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.erp.erp.dto.TaskResponse;

@Service
public class WorkflowTaskService {

    private final TaskService taskService;
    private final RuntimeService runtimeService;

    public WorkflowTaskService(TaskService taskService, RuntimeService runtimeService) {
        this.taskService = taskService;
        this.runtimeService = runtimeService;
    }

    public void startProcess(String processKey, Map<String, Object> vars) {
        runtimeService.startProcessInstanceByKey(processKey, vars);
    }

    public List<TaskResponse> listTasksForUser(String username) {
        List<Task> assigned = taskService.createTaskQuery()
                .taskAssignee(username)
                .list();

        List<Task> candidate = taskService.createTaskQuery()
                .taskCandidateUser(username)
                .list();

        Set<String> seen = new java.util.HashSet<>();
        return java.util.stream.Stream.concat(assigned.stream(), candidate.stream())
                .filter(t -> seen.add(t.getId()))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public void assignTask(String taskId, String username) {
        taskService.setAssignee(taskId, username);
    }

    public void completeTask(String taskId, Map<String, Object> vars) {
        taskService.complete(taskId, vars);
    }

    private TaskResponse toDto(Task t) {
        Map<String, Object> vars = taskService.getVariables(t.getId());
        return TaskResponse.builder()
                .taskId(t.getId())
                .name(t.getName())
                .assignee(t.getAssignee())
                .processInstanceId(t.getProcessInstanceId())
                .documentNumber((String) vars.get("documentNumber"))
                .created(String.valueOf(t.getCreateTime()))
                .build();
    }
}
