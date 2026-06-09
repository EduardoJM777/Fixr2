package br.unipar.devbackend.fixr.service;

import br.unipar.devbackend.fixr.Repository.AnunciosRepository;
import br.unipar.devbackend.fixr.Repository.EstatisticasAnuncioRepository;
import br.unipar.devbackend.fixr.dto.EstatisticasAnuncioDTO;
import br.unipar.devbackend.fixr.model.Anuncios;
import br.unipar.devbackend.fixr.model.EstatisticasAnuncio;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstatisticasAnuncioService {

    private final EstatisticasAnuncioRepository estatisticasRepository;
    private final AnunciosRepository anuncioRepository;

    public EstatisticasAnuncioDTO criar(Long anuncioId) {
        Anuncios anuncio = anuncioRepository.findById(anuncioId)
                .orElseThrow(() -> new EntityNotFoundException("Anúncio não encontrado: " + anuncioId));

        if (estatisticasRepository.findByAnuncioId(anuncioId).isPresent()) {
            throw new RuntimeException("Estatísticas já existem para o anúncio " + anuncioId);
        }

        EstatisticasAnuncio stats = new EstatisticasAnuncio();
        stats.setAnuncio(anuncio);
        stats.setCliques(0);
        stats.setVisualizacoesUnicas(0);
        stats.setVisualizacoesTotais(0);
        stats.setRankingPosicao(0);

        EstatisticasAnuncio salvo = estatisticasRepository.save(stats);
        recalcularRankings();
        return toDTO(salvo);
    }

    public EstatisticasAnuncioDTO buscarPorAnuncio(Long anuncioId) {
        EstatisticasAnuncio stats = estatisticasRepository.findByAnuncioId(anuncioId)
                .orElseThrow(() -> new EntityNotFoundException("Estatísticas não encontradas para o anúncio " + anuncioId));
        return toDTO(stats);
    }

    public EstatisticasAnuncioDTO registrarVisualizacao(Long anuncioId, boolean unica) {
        EstatisticasAnuncio stats = estatisticasRepository.findByAnuncioId(anuncioId)
                .orElseThrow(() -> new EntityNotFoundException("Estatísticas não encontradas para o anúncio " + anuncioId));

        stats.setVisualizacoesTotais(stats.getVisualizacoesTotais() + 1);
        if (unica) stats.setVisualizacoesUnicas(stats.getVisualizacoesUnicas() + 1);

        estatisticasRepository.save(stats);
        return toDTO(stats);
    }

    public EstatisticasAnuncioDTO registrarClique(Long anuncioId) {
        EstatisticasAnuncio stats = estatisticasRepository.findByAnuncioId(anuncioId)
                .orElseThrow(() -> new EntityNotFoundException("Estatísticas não encontradas para o anúncio " + anuncioId));

        stats.setCliques(stats.getCliques() + 1);
        estatisticasRepository.save(stats);
        recalcularRankings();
        return toDTO(stats);
    }

    private void recalcularRankings() {
        List<EstatisticasAnuncio> todos = estatisticasRepository.findAllOrderedByCliques();
        for (int i = 0; i < todos.size(); i++) {
            todos.get(i).setRankingPosicao(i + 1);
        }
        estatisticasRepository.saveAll(todos);
    }

    private EstatisticasAnuncioDTO toDTO(EstatisticasAnuncio stats) {
        return new EstatisticasAnuncioDTO(
                stats.getAnuncio().getId(),
                stats.calcularCTR(),
                stats.getVisualizacoesUnicas(),
                stats.getVisualizacoesTotais(),
                stats.getRankingPosicao()
        );
    }

}
