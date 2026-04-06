package br.unipar.devbackend.fixr.dto;

public record LoginDTO(
     String email,
     String senha


) {
    public String getEmail() { return email; }
    public String getSenha() { return senha; }
}
