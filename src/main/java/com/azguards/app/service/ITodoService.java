package com.azguards.app.service;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.azguards.app.dto.TodoDto;
import com.azguards.app.dto.TodoFolder;

public interface ITodoService {

    public void save(TodoDto todoDto);

    public void update(TodoDto todoDto, String id);

    public TodoDto get(String id);

    public List<TodoDto> getByUserId(String userId);

    public List<TodoDto> getAll();

    public Map<String, Object> delete(@Valid String id);

    public List<TodoDto> getByTitle(String title);

    public List<TodoFolder> getTodoFolder();
}
