package com.azguards.app.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.azguards.app.bean.Todo;
import com.azguards.app.dao.ITodoDao;
import com.azguards.app.dto.TodoDto;
import com.azguards.app.dto.TodoFolder;
import com.azguards.app.util.CommonUtil;
import com.azguards.common.lib.util.DateUtil;

@Service
public class TodoService implements ITodoService {

    @Autowired
    private ITodoDao iTodoDao;

    @Override
    public void save(TodoDto todoDto) {
        iTodoDao.save(CommonUtil.convertTodoDtoIntoTodo(todoDto));
    }

    @Override
    public void update(TodoDto todoDto, String id) {
        Todo todo = CommonUtil.convertTodoDtoIntoTodo(todoDto);
        todo.setId(id);
        iTodoDao.update(todo);
    }

    @Override
    public TodoDto get(String id) {
        return CommonUtil.convertTodoIntoTodoDto(iTodoDao.get(id));
    }

    @Override
    public List<TodoDto> getByUserId(String userId) {
        List<Todo> list = iTodoDao.getByUserId(userId);
        List<TodoDto> todoDtos = new LinkedList<>();
        for (Todo todo : list) {
            todoDtos.add(CommonUtil.convertTodoIntoTodoDto(todo));
        }
        return todoDtos;
    }

    @Override
    public List<TodoDto> getAll() {
        List<Todo> list = iTodoDao.getAll();
        List<TodoDto> todoDtos = new LinkedList<>();
        for (Todo todo : list) {
            todoDtos.add(CommonUtil.convertTodoIntoTodoDto(todo));
        }
        return todoDtos;
    }

    @Override
    public Map<String, Object> delete(@Valid String id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Todo todo = iTodoDao.get(id);
            if (todo != null) {
                todo.setIsActive(false);
                todo.setUpdatedOn(DateUtil.getUTCdatetimeAsDate());
                todo.setDeletedOn(DateUtil.getUTCdatetimeAsDate());
                iTodoDao.update(todo);
                response.put("status", HttpStatus.OK.value());
                response.put("message", "Todo deleted successfully");
            } else {
                response.put("status", HttpStatus.NOT_FOUND.value());
                response.put("message", "Todo not found");
            }
        } catch (Exception exception) {
            response.put("message", exception.getCause());
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return response;
    }

    @Override
    public List<TodoDto> getByTitle(String title) {
        List<Todo> list = iTodoDao.getByTitle(title);
        List<TodoDto> todoDtos = new LinkedList<>();
        for (Todo todo : list) {
            todoDtos.add(CommonUtil.convertTodoIntoTodoDto(todo));
        }
        return todoDtos;
    }

    @Override
    public List<TodoFolder> getTodoFolder() {
        return iTodoDao.getTodoFolder();
    }
}
