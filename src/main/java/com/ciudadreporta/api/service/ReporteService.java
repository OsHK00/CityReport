package com.ciudadreporta.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciudadreporta.api.dto.request.ReporteRequest;
import com.ciudadreporta.api.dto.response.ReporteResponse;
import com.ciudadreporta.api.dto.response.ReporteResponse.CategoriaInfo;
import com.ciudadreporta.api.dto.response.ReporteResponse.ImagenInfo;
import com.ciudadreporta.api.dto.response.ReporteResponse.UsuarioInfo;
import com.ciudadreporta.api.dto.response.FeedResponse;
import com.ciudadreporta.api.dto.response.VotoResponse;
import com.ciudadreporta.api.event.ReporteCreadoEvent;
import com.ciudadreporta.api.model.CategoriaModel;
import com.ciudadreporta.api.model.ImagenReporteModel;
import com.ciudadreporta.api.model.ReporteModel;
import com.ciudadreporta.api.model.TipoVoto;
import com.ciudadreporta.api.model.UsuarioModel;
import com.ciudadreporta.api.model.VotoReporteModel;
import com.ciudadreporta.api.repository.CategoriaRepository;
import com.ciudadreporta.api.repository.ReporteRepository;
import com.ciudadreporta.api.repository.UsuarioRepository;
import com.ciudadreporta.api.repository.VotoReporteRepository;

@Service
public class ReporteService {

    @Autowired
    private ReporteRepository reporteRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private VotoReporteRepository votoReporteRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Transactional
    public ReporteResponse crear(ReporteRequest request) {
        CategoriaModel categoria = categoriaRepository.findById(request.categoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));

        UsuarioModel usuario = usuarioRepository.findById(UUID.fromString(request.usuarioId()))
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        ReporteModel reporte = ReporteModel.builder()
                .titulo(request.titulo())
                .descripcion(request.descripcion())
                .latitud(request.latitud())
                .longitud(request.longitud())
                .categoria(categoria)
                .usuario(usuario)
                .imagenes(new ArrayList<>())
                .build();

        ReporteModel guardado = reporteRepository.save(reporte);

        eventPublisher.publishEvent(new ReporteCreadoEvent(this, guardado.getId(), guardado.getCategoria().getId()));

        return toReporteResponse(guardado);
    }

    @Transactional(readOnly = true)
    public ReporteResponse obtenerPorId(UUID id) {
        ReporteModel reporte = reporteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));
        return toReporteResponse(reporte);
    }

    @Transactional(readOnly = true)
    public FeedResponse obtenerFeed(UUID cursor, int size) {
        List<ReporteModel> content;
        boolean hasMore;

        if (cursor == null) {
            content = reporteRepository.findFirstPage(PageRequest.of(0, size + 1));
        } else {
            content = reporteRepository.findNextPage(cursor, PageRequest.of(0, size + 1));
        }

        hasMore = content.size() > size;
        if (hasMore) {
            content.remove(content.size() - 1);
        }

        UUID nextCursor = content.isEmpty() ? null : content.get(content.size() - 1).getId();
        List<ReporteResponse> data = content.stream().map(this::toReporteResponse).toList();
        return new FeedResponse(data, nextCursor, hasMore);
    }

    @Transactional
    public ReporteResponse actualizar(UUID id, ReporteRequest request) {
        ReporteModel reporte = reporteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));

        CategoriaModel categoria = categoriaRepository.findById(request.categoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria no encontrada"));

        reporte.setTitulo(request.titulo());
        reporte.setDescripcion(request.descripcion());
        reporte.setLatitud(request.latitud());
        reporte.setLongitud(request.longitud());
        reporte.setCategoria(categoria);

        ReporteModel guardado = reporteRepository.save(reporte);
        return toReporteResponse(guardado);
    }

    @Transactional
    public void eliminar(UUID id) {
        if (!reporteRepository.existsById(id)) {
            throw new RuntimeException("Reporte no encontrado");
        }
        reporteRepository.deleteById(id);
    }

    @Transactional
    public VotoResponse votar(UUID reporteId, UUID usuarioId, String tipoStr) {
        TipoVoto tipo;
        try {
            tipo = TipoVoto.valueOf(tipoStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Tipo de voto invalido. Use UPVOTE o DOWNVOTE");
        }

        ReporteModel reporte = reporteRepository.findById(reporteId)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));

        UsuarioModel usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        VotoReporteModel votoExistente = votoReporteRepository
                .findByUsuarioAndReporte(usuario, reporte).orElse(null);

        if (votoExistente != null) {
            if (votoExistente.getTipo() == tipo) {
                votoReporteRepository.delete(votoExistente);
                long upvotes = votoReporteRepository.countByReporteIdAndTipo(reporteId, TipoVoto.UPVOTE);
                long downvotes = votoReporteRepository.countByReporteIdAndTipo(reporteId, TipoVoto.DOWNVOTE);
                return new VotoResponse(reporteId, upvotes, downvotes, "Voto eliminado");
            }
            votoExistente.setTipo(tipo);
            votoReporteRepository.save(votoExistente);
        } else {
            VotoReporteModel voto = VotoReporteModel.builder()
                    .usuario(usuario)
                    .reporte(reporte)
                    .tipo(tipo)
                    .build();
            votoReporteRepository.save(voto);
        }

        long upvotes = votoReporteRepository.countByReporteIdAndTipo(reporteId, TipoVoto.UPVOTE);
        long downvotes = votoReporteRepository.countByReporteIdAndTipo(reporteId, TipoVoto.DOWNVOTE);
        return new VotoResponse(reporteId, upvotes, downvotes, "Voto registrado");
    }

    private ReporteResponse toReporteResponse(ReporteModel reporte) {
        List<ImagenInfo> imagenes = new ArrayList<>();
        if (reporte.getImagenes() != null) {
            for (ImagenReporteModel img : reporte.getImagenes()) {
                imagenes.add(new ImagenInfo(img.getId(), img.getUrl(), img.getOrden()));
            }
        }

        long upvotes = votoReporteRepository.countByReporteIdAndTipo(reporte.getId(), TipoVoto.UPVOTE);
        long downvotes = votoReporteRepository.countByReporteIdAndTipo(reporte.getId(), TipoVoto.DOWNVOTE);

        return new ReporteResponse(
            reporte.getId(),
            reporte.getTitulo(),
            reporte.getDescripcion(),
            reporte.getLatitud(),
            reporte.getLongitud(),
            new CategoriaInfo(reporte.getCategoria().getId(), reporte.getCategoria().getNombre()),
            new UsuarioInfo(reporte.getUsuario().getId(), reporte.getUsuario().getNombre()),
            imagenes,
            upvotes,
            downvotes,
            reporte.getCreatedAt()
        );
    }
}
