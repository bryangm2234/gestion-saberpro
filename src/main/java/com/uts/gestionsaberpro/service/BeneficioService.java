package com.uts.gestionsaberpro.service;

import com.uts.gestionsaberpro.entity.ResultadoSaberPro;
import com.uts.gestionsaberpro.entity.ResultadoSaberPro.NivelDesempeno;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class BeneficioService {

    private static final BigDecimal UMBRAL_SUPERIOR = new BigDecimal("241");
    private static final BigDecimal UMBRAL_ALTO     = new BigDecimal("201");

    public void calcularBeneficios(ResultadoSaberPro resultado) {
        BigDecimal puntaje = resultado.getPuntajeGlobal();
        if (puntaje == null) { limpiarBeneficios(resultado); return; }

        if (puntaje.compareTo(UMBRAL_SUPERIOR) > 0) {
            resultado.setTieneBeneficio(true);
            resultado.setNotaTrabajoGrado(new BigDecimal("5.0"));
            resultado.setBecaDerechosGrado(new BigDecimal("100.00"));
            resultado.setVigenciaIncentivo(fechaOHoy(resultado).plusYears(1));
            resultado.setDescripcionBeneficio("Exoneración nota 5.0 + Beca 100% · Puntaje superior a 241");
        } else if (puntaje.compareTo(UMBRAL_ALTO) >= 0) {
            resultado.setTieneBeneficio(true);
            resultado.setNotaTrabajoGrado(new BigDecimal("4.5"));
            resultado.setBecaDerechosGrado(new BigDecimal("50.00"));
            resultado.setVigenciaIncentivo(fechaOHoy(resultado).plusMonths(6));
            resultado.setDescripcionBeneficio("Exonerado trabajo de grado (nota 4.5) + Beca 50%");
        } else {
            limpiarBeneficios(resultado);
        }
    }

    public boolean esBeneficiario(ResultadoSaberPro r) {
        return r != null && r.getPuntajeGlobal() != null
                && r.getPuntajeGlobal().compareTo(UMBRAL_ALTO) >= 0;
    }

    private void limpiarBeneficios(ResultadoSaberPro r) {
        r.setTieneBeneficio(false);
        r.setNotaTrabajoGrado(null);
        r.setBecaDerechosGrado(null);
        r.setVigenciaIncentivo(null);
        r.setDescripcionBeneficio(null);
    }

    private LocalDate fechaOHoy(ResultadoSaberPro r) {
        return r.getFechaExamen() != null ? r.getFechaExamen() : LocalDate.now();
    }
}
