package com.azguards.app.controller.v1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.azguards.app.dto.TodoDto;
import com.azguards.app.dto.TodoFolder;
import com.azguards.app.service.ITodoService;

@RestController("todoControllerV1")
@RequestMapping("/api/v1/todo")
public class TodoController {

    @Autowired
    public ITodoService iTodoService;

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> saveTodo(@Valid @RequestBody final TodoDto todoDto) throws Exception {
        Map<String, Object> response = new HashMap<>(3);
        try {
            iTodoService.save(todoDto);
            response.put("message", "Todo type saved successfully");
            response.put("status", HttpStatus.OK.value());
        } catch (Exception exception) {
            exception.printStackTrace();
            response.put("message", "Error to save todo");
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("data", "Error");
        }
        return ResponseEntity.accepted().body(response);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> get(@PathVariable String id) throws Exception {
        Map<String, Object> response = new HashMap<>(3);
        try {
            TodoDto todoDto = iTodoService.get(id);
            if (todoDto != null) {
                response.put("message", "Todo fetch successfully");
                response.put("status", HttpStatus.OK.value());
                response.put("data", todoDto);
            } else {
                response.put("message", "Todo not found");
                response.put("status", HttpStatus.NOT_FOUND.value());
                response.put("data", todoDto);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            response.put("message", "Error to fetching todo");
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("data", "Error");
        }
        return ResponseEntity.accepted().body(response);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<?> updateTodo(@PathVariable String id, @Valid @RequestBody TodoDto todoDto) throws Exception {
        Map<String, Object> response = new HashMap<>(3);
        try {
            iTodoService.update(todoDto, id);
            response.put("message", "Todo type update successfully");
            response.put("status", HttpStatus.OK.value());
            response.put("data", todoDto);
        } catch (Exception exception) {
            exception.printStackTrace();
            response.put("message", "Error to update todo");
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("data", "Error");
        }
        return ResponseEntity.accepted().body(response);
    }

    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getTodoByUserId(@PathVariable String userId) throws Exception {
        Map<String, Object> response = new HashMap<>(3);
        try {
            List<TodoDto> todoDto = iTodoService.getByUserId(userId);
            if (!todoDto.isEmpty() && todoDto != null) {
                response.put("message", "Todo fetch successfully");
                response.put("status", HttpStatus.OK.value());
                response.put("data", todoDto);
            } else {
                response.put("message", "Todo not found");
                response.put("status", HttpStatus.NOT_FOUND.value());
                response.put("data", todoDto);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            response.put("message", "Error to fetching todo");
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("data", "Error");
        }
        return ResponseEntity.accepted().body(response);
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getAll() throws Exception {
        Map<String, Object> response = new HashMap<>(3);
        try {
            List<TodoDto> todoDto = iTodoService.getAll();
            if (!todoDto.isEmpty() && todoDto != null) {
                response.put("message", "Todo fetch successfully");
                response.put("status", HttpStatus.OK.value());
                response.put("data", todoDto);
            } else {
                response.put("message", "Todo not found");
                response.put("status", HttpStatus.NOT_FOUND.value());
                response.put("data", todoDto);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            response.put("message", "Error to fetching todo");
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("data", "Error");
        }
        return ResponseEntity.accepted().body(response);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<?> delete(@Valid @PathVariable final String id) throws Exception {
        return ResponseEntity.accepted().body(iTodoService.delete(id));
    }

    @RequestMapping(value = "/filter/title", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getTodoBytitle(@RequestParam(required = false) final String title) throws Exception {
        Map<String, Object> response = new HashMap<>(3);
        try {
            List<TodoDto> todoDto = iTodoService.getByTitle(title);
            if (!todoDto.isEmpty() && todoDto != null) {
                response.put("message", "Todo fetch successfully");
                response.put("status", HttpStatus.OK.value());
                response.put("data", todoDto);
            } else {
                response.put("message", "Todo not found");
                response.put("status", HttpStatus.NOT_FOUND.value());
                response.put("data", todoDto);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            response.put("message", "Error to fetching todo");
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("data", "Error");
        }
        return ResponseEntity.accepted().body(response);
    }

    @RequestMapping(value = "/folder", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getTodoFolder() throws Exception {
        Map<String, Object> response = new HashMap<>(3);
        try {
            List<TodoFolder> todoFolderDto = iTodoService.getTodoFolder();
            if (!todoFolderDto.isEmpty() && todoFolderDto != null) {
                response.put("message", "Todo folder fetch successfully");
                response.put("status", HttpStatus.OK.value());
                response.put("data", todoFolderDto);
            } else {
                response.put("message", "Todo folder not found");
                response.put("status", HttpStatus.NOT_FOUND.value());
                response.put("data", todoFolderDto);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            response.put("message", "Error to fetching todo");
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("data", "Error");
        }
        return ResponseEntity.accepted().body(response);
    }
}
