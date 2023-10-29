package com.azguards.app.dao;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.azguards.app.bean.Todo;
import com.azguards.app.dto.TodoFolder;

@Repository
@Transactional
@SuppressWarnings({ "deprecation", "unchecked" })
public class TodoDao implements ITodoDao {

    @Autowired
    SessionFactory sessionFactory;

    @Override
    public void save(Todo todo) {
        try {
            Session session = sessionFactory.getCurrentSession();
            session.save(todo);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void update(Todo todo) {
        try {
            Session session = sessionFactory.getCurrentSession();
            session.update(todo);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public Todo get(String id) {
        Todo todo = new Todo();
        try {
            Session session = sessionFactory.getCurrentSession();
            todo = session.get(Todo.class, id);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return todo;
    }

    @Override
    public List<Todo> getByUserId(String userId) {
        List<Todo> todos = new ArrayList<>();
        try {
            Session session = sessionFactory.getCurrentSession();
            Criteria crit = session.createCriteria(Todo.class);
            crit.add(Restrictions.eq("userId", userId));
            crit.add(Restrictions.eq("isActive", true));
            todos = crit.list();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return todos;
    }

    @Override
    public List<Todo> getAll() {
        List<Todo> todos = new ArrayList<>();
        try {
            Session session = sessionFactory.getCurrentSession();
            Criteria crit = session.createCriteria(Todo.class);
            crit.add(Restrictions.eq("isActive", true));
            todos = crit.list();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return todos;
    }

    @Override
    public List<Todo> getByTitle(String title) {
        List<Todo> todos = new ArrayList<>();
        try {
            Session session = sessionFactory.getCurrentSession();
            Criteria crit = session.createCriteria(Todo.class);
            crit.add(Restrictions.eq("title", title));
            crit.add(Restrictions.eq("isActive", true));
            todos = crit.list();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return todos;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List<TodoFolder> getTodoFolder() {
        List<TodoFolder> todoFolders = new ArrayList<TodoFolder>();
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createSQLQuery("SELECT sc.id, sc.name as name FROM todo_folder ");
        List<Object[]> rows = query.list();
        TodoFolder todoFolder = null;
        for (Object[] row : rows) {
            todoFolder = new TodoFolder();
            todoFolder.setId(row[0].toString());
            todoFolder.setName(row[1].toString());
            todoFolders.add(todoFolder);
        }
        return todoFolders;
    }
}