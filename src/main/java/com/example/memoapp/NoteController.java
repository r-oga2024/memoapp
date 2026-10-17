
package com.example.memoapp;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class NoteController {

    private final NoteRepository noteRepository;

    public NoteController(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    // メモ一覧を表示
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("notes", noteRepository.findAll());
        return "index";
    }

    // 新しいメモを保存
    @PostMapping("/notes")
    public String createNote(
            @RequestParam String title,
            @RequestParam String content,
            RedirectAttributes redirectAttributes) {

        Note note = new Note(title, content);
        noteRepository.save(note);

        redirectAttributes.addFlashAttribute(
                "message", "メモを保存しました。");

        return "redirect:/";
    }

    // 編集画面を表示
    @GetMapping("/notes/edit/{id}")
    public String editNote(
            @PathVariable Long id,
            Model model,
            RedirectAttributes redirectAttributes) {

        Optional<Note> result = noteRepository.findById(id);

        if (result.isEmpty()) {
            redirectAttributes.addFlashAttribute(
                    "message", "メモが見つかりません。");
            return "redirect:/";
        }

        model.addAttribute("note", result.get());
        return "edit";
    }

    // メモを更新
    @PostMapping("/notes/update/{id}")
    public String updateNote(
            @PathVariable Long id,
            @RequestParam String title,
            @RequestParam String content,
            RedirectAttributes redirectAttributes) {

        Optional<Note> result = noteRepository.findById(id);

        if (result.isPresent()) {
            Note note = result.get();
            note.setTitle(title);
            note.setContent(content);

            noteRepository.save(note);

            redirectAttributes.addFlashAttribute(
                    "message", "メモを更新しました。");
        } else {
            redirectAttributes.addFlashAttribute(
                    "message", "メモが見つかりません。");
        }

        return "redirect:/";
    }

    // メモを削除
    @PostMapping("/notes/delete/{id}")
    public String deleteNote(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        if (noteRepository.existsById(id)) {
            noteRepository.deleteById(id);

            redirectAttributes.addFlashAttribute(
                    "message", "メモを削除しました。");
        } else {
            redirectAttributes.addFlashAttribute(
                    "message", "メモが見つかりません。");
        }

        return "redirect:/";
    }
}
