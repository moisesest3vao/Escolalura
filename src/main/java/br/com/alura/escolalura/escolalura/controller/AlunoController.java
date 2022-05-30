package br.com.alura.escolalura.escolalura.controller;

import br.com.alura.escolalura.escolalura.model.Aluno;
import br.com.alura.escolalura.escolalura.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/aluno")
public class AlunoController {

    @Autowired
    AlunoRepository alunoRepository;

    @GetMapping("/cadastrar")
    public String cadastraAluno(Model model){
        model.addAttribute("aluno", new Aluno());
        return "aluno/cadastrar";
    }

    @PostMapping("/salvar")
    public String salvaAluno(@ModelAttribute Aluno aluno){
        System.out.println(aluno);
        alunoRepository.save(aluno);
        return "redirect:/";
    }

    @GetMapping("/listar")
    public String listaAluno(Model model){
        List<Aluno> alunos = alunoRepository.findAll();
        model.addAttribute("alunos", alunos);
        return "aluno/listar";
    }
}
