package com.example.bookmark.presentation.controller;

import com.example.bookmark.application.dto.BookmarkCreateRequest;
import com.example.bookmark.application.dto.BookmarkResponse;
import com.example.bookmark.application.dto.BookmarkUpdateRequest;
import com.example.bookmark.application.facade.BookmarkFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/bookmarks")
@RequiredArgsConstructor
public class BookmarkViewController {

    private final BookmarkFacade bookmarkFacade;

    @GetMapping
    public String list(@RequestParam(value = "category", required = false) String category, Model model) {
        List<BookmarkResponse> bookmarks = bookmarkFacade.getBookmarks(category);
        model.addAttribute("bookmarks", bookmarks);
        model.addAttribute("categories", bookmarkFacade.getCategories());
        model.addAttribute("selectedCategory", category);
        return "bookmarks/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("request", new BookmarkCreateRequest("", "", "", ""));
        return "bookmarks/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("request") BookmarkCreateRequest request,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "bookmarks/form";
        }
        bookmarkFacade.createBookmark(request);
        return "redirect:/bookmarks";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        BookmarkResponse bookmark = bookmarkFacade.getBookmark(id);
        model.addAttribute("bookmark", bookmark);
        return "bookmarks/detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable("id") Long id, Model model) {
        BookmarkResponse bookmark = bookmarkFacade.getBookmark(id);
        model.addAttribute("bookmark", bookmark);
        model.addAttribute("request", new BookmarkUpdateRequest(
                bookmark.title(), bookmark.url(), bookmark.description(), bookmark.category()));
        return "bookmarks/edit";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable("id") Long id,
                         @Valid @ModelAttribute("request") BookmarkUpdateRequest request,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("bookmark", bookmarkFacade.getBookmark(id));
            return "bookmarks/edit";
        }
        bookmarkFacade.updateBookmark(id, request);
        return "redirect:/bookmarks/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        bookmarkFacade.deleteBookmark(id);
        return "redirect:/bookmarks";
    }

    @PostMapping("/{id}/pin")
    public String togglePin(@PathVariable("id") Long id) {
        bookmarkFacade.togglePin(id);
        return "redirect:/bookmarks";
    }
}
