package br.unipar.devbackend.fixr.Controller;

import br.unipar.devbackend.fixr.dto.ClienteDTO;
import br.unipar.devbackend.fixr.dto.EstatisticasDTO;
import br.unipar.devbackend.fixr.model.Cliente;
import br.unipar.devbackend.fixr.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/cliente")
@CrossOrigin(origins = "*")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService service){
        this.clienteService = service;
    }

    @PostMapping
    public Cliente cadastrar(@Valid @RequestBody ClienteDTO clienteDTO){
        return clienteService.cadastrar(clienteDTO);
    }

    @PutMapping("/{id}")
    public Cliente atualizar(@PathVariable Long id, @Valid @RequestBody ClienteDTO clienteDTO){
        return clienteService.atualizar(id, clienteDTO);
    }

    @PatchMapping("/{id}/foto")
    public ResponseEntity<Void> atualizarFoto(@PathVariable Long id,
                                              @RequestParam("foto") MultipartFile foto) throws IOException {
        clienteService.atualizarFoto(id, foto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public List<Cliente> listar(){
        return clienteService.listar();
    }

    @GetMapping("/{id}")
    public Cliente buscarPorId(@PathVariable Long id){
        return clienteService.buscarPorId(id);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id){
        clienteService.deletar(id);
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<EstatisticasDTO> getEstatisticas(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.buscarEstatisticas(id));
    }

}
