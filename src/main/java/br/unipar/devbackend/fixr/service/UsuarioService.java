package br.unipar.devbackend.fixr.service;

import br.unipar.devbackend.fixr.Repository.UsuarioRepository;
import br.unipar.devbackend.fixr.dto.UsuarioDTO;
import br.unipar.devbackend.fixr.model.Usuario;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository){
        this.repository = repository;
    }

    public Usuario salvar(UsuarioDTO usuarioDTO){
        Usuario usuario = new Usuario();
        usuario.setNome(usuarioDTO.nome());
        usuario.setEmail(usuarioDTO.email());
        usuario.setUserType(usuarioDTO.userType());
        return repository.save(usuario);
    }

    public List<Usuario> listar(){
        return repository.findAll();
    }

    public Usuario buscarPorId(Long id){
        return repository.findById(id).orElseThrow(()-> new RuntimeException("Não encontrado")) ;
    }

    public Usuario atualizar(Long id, Usuario usuarioAtualizado){
        return repository.findById(id).map(usuario ->{
            usuarioAtualizado.setNome(usuarioAtualizado.getNome());
            usuarioAtualizado.setEmail(usuarioAtualizado.getEmail());
            usuarioAtualizado.setUserType(usuarioAtualizado.getUserType());
            usuarioAtualizado.setSenhaHash(usuarioAtualizado.getSenhaHash());
            return repository.save(usuario);
        }).orElseThrow(()-> new RuntimeException("Erro"));
    }

    public void apagar(Long id){
        repository.deleteById(id);
    }

}
