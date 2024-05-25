package com.example.ProjectFinal.web;


import com.example.ProjectFinal.entities.Docteur;
import com.example.ProjectFinal.entities.Specialité;
import com.example.ProjectFinal.entities.rendez_Vous;
import com.example.ProjectFinal.repositories.DocteurRepository;
import com.example.ProjectFinal.repositories.RendezVousRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
public class RDVController {
    @Autowired
    private RendezVousRepository rendezVousRepository;

    @GetMapping("/api/rdvs")
    @ResponseBody
    public List<rendez_Vous> getRDVS() {
        return rendezVousRepository.findAll();
    }
}
