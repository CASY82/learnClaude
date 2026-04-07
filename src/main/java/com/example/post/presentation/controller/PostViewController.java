package com.example.post.presentation.controller;

import com.example.post.application.dto.PostCreateRequest;
import com.example.post.application.dto.PostResponse;
import com.example.post.application.dto.PostUpdateRequest;
import com.example.post.application.facade.PostFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostViewController {

    private final PostFacade postFacade;

    @GetMapping
    public String list(Model model) {
        List<PostResponse> posts = postFacade.getPosts();
        model.addAttribute("posts", posts);
        return "posts/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("request", new PostCreateRequest("", ""));
        return "posts/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("request") PostCreateRequest request,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "posts/form";
        }
        postFacade.createPost(request);
        return "redirect:/posts";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        PostResponse post = postFacade.getPost(id);
        model.addAttribute("post", post);
        return "posts/detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable("id") Long id, Model model) {
        PostResponse post = postFacade.getPost(id);
        model.addAttribute("post", post);
        model.addAttribute("request", new PostUpdateRequest(post.title(), post.content())); // PostResponse는 record라 accessor 사용
        return "posts/edit";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable("id") Long id,
                         @Valid @ModelAttribute("request") PostUpdateRequest request,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("post", postFacade.getPost(id));
            return "posts/edit";
        }
        postFacade.updatePost(id, request);
        return "redirect:/posts/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        postFacade.deletePost(id);
        return "redirect:/posts";
    }
}
