package com.example.swithme.controller;

import com.example.swithme.entity.Study;
import com.example.swithme.repository.StudyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class StudyController {
    @Autowired
    private StudyRepository studyRepository;

    @GetMapping("/studies")
    public String listStudies(Model model) {
        List<Study> studies = studyRepository.findAll();
        model.addAttribute("studies", studies);
        return "study/list";
    }

    @GetMapping("/studies/new")
    public String showCreateForm(Model model) {
        model.addAttribute("study", new Study());
        return "study/createForm";
    }

    @PostMapping("/studies")
    public String createStudy(@ModelAttribute Study study) {
        studyRepository.save(study);
        return "redirect:/studies";
    }

    @GetMapping("/studies/calendar")
    public String showCalendar(Model model) {
        List<Study> studies = studyRepository.findAll();
        model.addAttribute("studies", studies);
        return "study/calendar";
    }

    @GetMapping("/studies/details/{id}")
    public String showStudyDetails(@PathVariable Long id, Model model) {
        Optional<Study> optionalStudy = studyRepository.findById(id);
        if (optionalStudy.isPresent()) {
            Study study = optionalStudy.get();
            model.addAttribute("study", study);
            return "study/details";
        }
        return "redirect:/studies/calendar";
    }

    @GetMapping("/studies/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Study> optionalStudy = studyRepository.findById(id);
        if (optionalStudy.isPresent()) {
            Study study = optionalStudy.get();
            model.addAttribute("study", study);
            return "study/editForm";
        }
        return "redirect:/studies/calendar";
    }

    @PostMapping("/studies/update")
    public String updateStudy(@ModelAttribute Study study) {
        studyRepository.save(study);
        return "redirect:/studies/calendar";
    }

    @GetMapping("/studies/delete/{id}")
    public String deleteStudy(@PathVariable Long id) {
        studyRepository.deleteById(id);
        return "redirect:/studies/calendar";
    }
}


