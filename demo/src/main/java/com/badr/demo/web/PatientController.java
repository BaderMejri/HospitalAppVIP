package com.badr.demo.web;


import com.badr.demo.entities.Patient;
import com.badr.demo.repositories.PatientRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class PatientController {
    private PatientRepository patientRepository;

    @GetMapping(path="/user/index")
    public String patients(Model model,
                           @RequestParam(name="page",defaultValue = "0") int page,
                           @RequestParam(name="size",defaultValue = "5") int size,
                           @RequestParam(name="Keyword",defaultValue = "") String Keyword
    ){
        Page<Patient> pagePatients = patientRepository.findByNameContains(Keyword, PageRequest.of(page, size));
        model.addAttribute("ListPatients", pagePatients.getContent());
        model.addAttribute("pages", new int[pagePatients.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("Keyword", Keyword);

       return "patients";
    }

    @DeleteMapping("/admin/delete")
    public String delete(Long id, String Keyword, int page){
        patientRepository.deleteById(id);
        return "redirect:/user/index?page="+page+"&Keyword="+Keyword;
    }

    @GetMapping("/")
    public String home(){
        return "home";
    }

    @GetMapping("/user/patients")
    @ResponseBody
    public List<Patient> ListPatients(){
        return patientRepository.findAll();
    }

    @GetMapping("/admin/formPatients")
    public String formPatients(Model model){
        model.addAttribute("patient",new Patient());
        return "formPatients";
    }

    @PostMapping("/admin/save")
    public String save(Model model, @Valid Patient patient, BindingResult bindingResult, @RequestParam(defaultValue = "") String Keyword, @RequestParam(defaultValue = "0") int page){
        if (bindingResult.hasErrors()) return "formPatients";
        patientRepository.save(patient);
        return "redirect:/user/index?page="+page+"&Keyword="+Keyword;
    }
    @PostMapping("/admin/editPatients")
    public String editPatients(Model model, Long id, String Keyword, int page){
        Patient patient = patientRepository.findById(id).orElse(null);
        if(patient==null) throw new RuntimeException("Patient Introuvable");
        model.addAttribute("patient", patient);
        model.addAttribute("keyword", Keyword);
        model.addAttribute("page", page);
        return "editPatients";
    }
}
