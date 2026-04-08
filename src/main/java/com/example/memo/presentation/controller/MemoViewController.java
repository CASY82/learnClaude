package com.example.memo.presentation.controller;

import com.example.memo.application.dto.MemoCreateRequest;
import com.example.memo.application.dto.MemoResponse;
import com.example.memo.application.dto.MemoUpdateRequest;
import com.example.memo.application.facade.MemoFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/memos")
@RequiredArgsConstructor
public class MemoViewController {

    private final MemoFacade memoFacade;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("memos", memoFacade.getMemos());
        return "memos/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("request", new MemoCreateRequest());
        return "memos/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("request") MemoCreateRequest request,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "memos/form";
        }
        memoFacade.createMemo(request);
        return "redirect:/memos";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        model.addAttribute("memo", memoFacade.getMemo(id));
        return "memos/detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable("id") Long id, Model model) {
        MemoResponse memo = memoFacade.getMemo(id);
        MemoUpdateRequest request = new MemoUpdateRequest(memo.content());
        model.addAttribute("memo", memo);
        model.addAttribute("request", request);
        return "memos/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable("id") Long id,
                         @Valid @ModelAttribute("request") MemoUpdateRequest request,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("memo", memoFacade.getMemo(id));
            return "memos/form";
        }
        memoFacade.updateMemo(id, request);
        return "redirect:/memos/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        memoFacade.deleteMemo(id);
        return "redirect:/memos";
    }
}
