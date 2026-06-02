package com.ciudadreporta.api.event;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.ciudadreporta.api.model.CategoriaModel;
import com.ciudadreporta.api.model.SuscripcionModel;
import com.ciudadreporta.api.repository.CategoriaRepository;
import com.ciudadreporta.api.repository.ReporteRepository;
import com.ciudadreporta.api.repository.SuscripcionRepository;
import com.ciudadreporta.api.service.EmailService;

@Component
public class NotificacionSuscripcionListener {

    @Autowired
    private SuscripcionRepository suscripcionRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private EmailService emailService;

    @EventListener
    public void onReporteCreado(ReporteCreadoEvent event) {
        CategoriaModel categoria = categoriaRepository.findById(event.getCategoriaId()).orElse(null);
        if (categoria == null) return;

        List<SuscripcionModel> suscriptores = suscripcionRepository.findByCategoria(categoria);

        for (SuscripcionModel suscripcion : suscriptores) {
            String email = suscripcion.getUsuario().getEmail();
            String asunto = "Nuevo reporte en " + categoria.getNombre();
            String cuerpo = "Se ha creado un nuevo reporte en la categoria '"
                    + categoria.getNombre()
                    + "'. Ingresa a la aplicacion para verlo.";
            emailService.enviarNotificacion(email, asunto, cuerpo);
        }
    }
}
