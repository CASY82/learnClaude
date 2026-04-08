package com.example.todo.presentation.controller;

import com.example.todo.application.dto.TodoCreateRequest;
import com.example.todo.application.facade.TodoFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/todos")
@RequiredArgsConstructor
public class TodoViewController {

    private final TodoFacade todoFacade;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("todos", todoFacade.getTodos());
        model.addAttribute("request", new TodoCreateRequest());
        return "todos/list";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("request") TodoCreateRequest request,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("todos", todoFacade.getTodos());
            return "todos/list";
        }
        todoFacade.createTodo(request);
        return "redirect:/todos";
    }

    @PostMapping("/{id}/toggle")
    public String toggle(@PathVariable("id") Long id) {
        todoFacade.toggleTodo(id);
        return "redirect:/todos";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        todoFacade.deleteTodo(id);
        return "redirect:/todos";
    }
}
