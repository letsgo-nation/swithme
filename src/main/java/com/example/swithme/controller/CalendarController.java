package com.example.swithme.controller;

import com.example.swithme.dto.CalendarResponseDto;
import com.example.swithme.entity.Calendar;
import com.example.swithme.entity.User;
import com.example.swithme.repository.CalendarRepository;
import com.example.swithme.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarRepository studyRepository;

//    @GetMapping("/studies")
//    public String listStudies(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        User user = userDetails.getUser();
//        List<Calendar> studies = studyRepository.findAllByUser(user);
//        model.addAttribute("studies", studies);
//        return "study/list";
//    }

    @GetMapping("/studies/new")
    public String showCreateForm(Model model) {
        model.addAttribute("study", new Calendar());
        return "study/createForm";
    }

    @PostMapping("/studies")
    public String createStudy(@ModelAttribute Calendar study, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        study.setUser(user);

        studyRepository.save(study);
        return "redirect:/studies/calendar";
    }

    //캘린더 출력
    @GetMapping("/studies/calendar")
    public String showCalendar(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        List<Calendar> studies = studyRepository.findAllByUser(user);
        model.addAttribute("studies", studies);
        return "study/calendar";
    }

    @GetMapping("/studies/details/{id}")
    public String showStudyDetails(@PathVariable Long id, Model model) {
        Optional<Calendar> optionalStudy = studyRepository.findById(id);
        if (optionalStudy.isPresent()) {
            Calendar study = optionalStudy.get();
            model.addAttribute("study", study);
            return "study/details";
        }
        return "redirect:/studies/calendar";
    }

    @GetMapping("/studies/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Calendar> optionalStudy = studyRepository.findById(id);
        if (optionalStudy.isPresent()) {
            Calendar study = optionalStudy.get();
            model.addAttribute("study", study);
            return "study/editForm";
        }
        return "redirect:/studies/calendar";
    }

    @PostMapping("/studies/update")
    public String updateStudy(@ModelAttribute Calendar study, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        study.setUser(user);
        studyRepository.save(study);
        return "redirect:/studies/calendar";
    }

    @GetMapping("/studies/delete/{id}")
    public String deleteStudy(@PathVariable Long id) {
        studyRepository.deleteById(id);
        return "redirect:/studies/calendar";
    }
}


