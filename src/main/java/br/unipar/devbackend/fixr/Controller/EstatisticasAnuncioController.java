package br.unipar.devbackend.fixr.Controller;

import br.unipar.devbackend.fixr.dto.EstatisticasAnuncioDTO;
import br.unipar.devbackend.fixr.service.EstatisticasAnuncioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/estatisticas/anuncio")
@RequiredArgsConstructor
public class EstatisticasAnuncioController {

    private final EstatisticasAnuncioService service;

    // Cria estatísticas para um anúncio
    @PostMapping("/{anuncioId}")
    public EstatisticasAnuncioDTO criar(@PathVariable Long anuncioId) {
        return service.criar(anuncioId);
    }

    // Busca estatísticas de um anúncio
    @GetMapping("/{anuncioId}")
    public EstatisticasAnuncioDTO buscar(@PathVariable Long anuncioId) {
        return service.buscarPorAnuncio(anuncioId);
    }

    // Registra visualização — ?unica=true para visualização única
    @PostMapping("/{anuncioId}/visualizacao")
    public EstatisticasAnuncioDTO registrarVisualizacao(
            @PathVariable Long anuncioId,
            @RequestParam(defaultValue = "false") boolean unica) {
        return service.registrarVisualizacao(anuncioId, unica);
    }

    // Registra clique no anúncio
    @PostMapping("/{anuncioId}/clique")
    public EstatisticasAnuncioDTO registrarClique(@PathVariable Long anuncioId) {
        return service.registrarClique(anuncioId);
    }

}
