package br.com.alura.escolalura.escolalura.controller;

import br.com.alura.escolalura.escolalura.model.Aluno;
import br.com.alura.escolalura.escolalura.model.Habilidade;
import br.com.alura.escolalura.escolalura.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/habilidade")
public class HabilidadeController {

    @Autowired
    private AlunoRepository alunoRepository;

    @GetMapping("/cadastrar/{id}")
    public String cadastraHabilidade(@PathVariable String id, Model model){
        Aluno aluno = alunoRepository.findById(id);
        model.addAttribute("aluno", aluno);
        model.addAttribute("habilidade", new Habilidade());
        return "habilidade/cadastrar";
    }

    @PostMapping("/salvar/{id}")
    public String salvar(@PathVariable String id, @ModelAttribute Habilidade habilidade){
        Aluno aluno = alunoRepository.findById(id);
        Aluno alunoEditado = aluno.adicionarHabilidade(aluno, habilidade);
        alunoRepository.save(aluno);
        return "redirect:/aluno/listar";
    }
}
