package br.unipar.devbackend.fixr.Controller;

import br.unipar.devbackend.fixr.dto.ChatsDTO;
import br.unipar.devbackend.fixr.model.Chats;
import br.unipar.devbackend.fixr.service.ChatsService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chats")
@CrossOrigin(origins = "*")
public class ChatsController {

    private final ChatsService chatsService;

    public ChatsController(ChatsService chatsService){
        this.chatsService = chatsService;
    }

    @PostMapping
    public Chats cadastrar(@Valid @RequestBody ChatsDTO chatsDTO){
        return chatsService.criar(chatsDTO);
    }

    @GetMapping
    public List<Chats> listar(){
        return chatsService.listar();
    }

    @GetMapping("/{id}")
    public Chats listarPorId(@PathVariable Long id){
        return chatsService.listarPorId(id);
    }

    @PutMapping("/{id}")
    public Chats atualizar(@PathVariable Long id, @RequestBody ChatsDTO chatsDTO){
        return chatsService.atualizar(id, chatsDTO);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id){
        chatsService.deletar(id);
    }

}
