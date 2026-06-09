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

    @PostMapping("/{anuncioId}")
    public EstatisticasAnuncioDTO criar(@PathVariable Long anuncioId) {
        return service.criar(anuncioId);
    }

    @GetMapping("/{anuncioId}")
    public EstatisticasAnuncioDTO buscar(@PathVariable Long anuncioId) {
        return service.buscarPorAnuncio(anuncioId);
    }

    @PostMapping("/{anuncioId}/visualizacao")
    public EstatisticasAnuncioDTO registrarVisualizacao(
            @PathVariable Long anuncioId,
            @RequestParam(defaultValue = "false") boolean unica) {
        return service.registrarVisualizacao(anuncioId, unica);
    }

    @PostMapping("/{anuncioId}/clique")
    public EstatisticasAnuncioDTO registrarClique(@PathVariable Long anuncioId) {
        return service.registrarClique(anuncioId);
    }

}
