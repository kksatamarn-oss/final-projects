package com.example.loginapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private BookService bookService;

    @Autowired
    private LibraryService libraryService;

    @Autowired
    private UserService userService;

    @GetMapping("/shelf")
    public String shelf(@RequestParam(value = "keyword", required = false) String keyword,
                        @RequestParam(value = "category", required = false) String category,
                        @RequestParam(value = "status", required = false) String status,
                        Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) { return "redirect:/login"; }
        List<Book> books = bookService.filterBooks(keyword, category, status);
        model.addAttribute("books", books);
        model.addAttribute("currentUser", user); // Add current user to model
        model.addAttribute("keyword", keyword);
        model.addAttribute("category", category);
        model.addAttribute("status", status);
        return "shelf";
    }

    @GetMapping("/shelf/random")
    public String randomBook(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) { return "redirect:/login"; }
        bookService.findRandomBook().ifPresent(book -> model.addAttribute("books", Collections.singletonList(book)));
        model.addAttribute("currentUser", user);
        return "shelf";
    }

    @GetMapping("/history")
    public String history(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) { return "redirect:/login"; }
        List<BorrowingRecord> records = libraryService.getBorrowingHistory(user);
        model.addAttribute("records", records);
        return "history";
    }

    @GetMapping("/notifications")
    public String notifications(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) { return "redirect:/login"; }
        List<Notification> notifications = libraryService.getNotifications(user);
        model.addAttribute("notifications", notifications);
        return "notifications";
    }

    @GetMapping("/profile")
    public String myProfile(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) { return "redirect:/login"; }
        User freshUser = userService.findByUsername(user.getUsername());
        model.addAttribute("user", freshUser);
        return "profile";
    }

    @GetMapping("/profile/{username}")
    public String publicProfile(@PathVariable String username, Model model) {
        User user = userService.findByUsername(username);
        if (user != null) {
            model.addAttribute("user", user);
            return "public-profile";
        }
        return "redirect:/shelf";
    }

    @PostMapping("/profile/bio")
    public String updateBio(@RequestParam String bio, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) { return "redirect:/login"; }
        try {
            userService.updateBio(user.getId(), bio);
            redirectAttributes.addFlashAttribute("message", "Bio updated successfully!");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/profile";
    }

    @PostMapping("/profile/password")
    public String changePassword(@RequestParam String oldPassword, @RequestParam String newPassword,
                                 HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) { return "redirect:/login"; }
        try {
            userService.changePassword(user.getId(), oldPassword, newPassword);
            redirectAttributes.addFlashAttribute("message", "Password changed successfully!");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/profile";
    }

    @PostMapping("/borrow/{bookId}")
    public String borrowBook(@PathVariable Long bookId, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) { return "redirect:/login"; }
        try {
            libraryService.borrowBook(bookId, user);
            redirectAttributes.addFlashAttribute("message", "Book borrowed successfully!");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/shelf";
    }

    @PostMapping("/return/{bookId}")
    public String returnBook(@PathVariable Long bookId, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) { return "redirect:/login"; }
        try {
            libraryService.returnBook(bookId, user);
            redirectAttributes.addFlashAttribute("message", "Book returned successfully!");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/history";
    }

    @PostMapping("/reserve/{bookId}")
    public String reserveBook(@PathVariable Long bookId, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) { return "redirect:/login"; }
        try {
            libraryService.reserveBook(bookId, user);
            redirectAttributes.addFlashAttribute("message", "Book reserved successfully!");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/shelf";
    }
}
