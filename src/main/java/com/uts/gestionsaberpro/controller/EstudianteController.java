package com.uts.gestionsaberpro.controller;

import com.uts.gestionsaberpro.entity.*;
import com.uts.gestionsaberpro.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;

@Controller
@RequestMapping("/estudiante")
public class EstudianteController {

    private final EstudianteRepository     estudianteRepo;
    private final ResultadoRepository      resultadoRepo;
    private final PagoSaberProRepository   pagoRepo;

    public EstudianteController(EstudianteRepository e, ResultadoRepository r,
                                 PagoSaberProRepository p) {
        this.estudianteRepo = e;
        this.resultadoRepo  = r;
        this.pagoRepo       = p;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        estudianteRepo.findByUsuarioCorreo(principal.getName()).ifPresent(e -> {
            model.addAttribute("estudiante", e);
            resultadoRepo.findTopByEstudianteIdOrderByFechaExamenDesc(e.getId())
                    .ifPresent(r -> model.addAttribute("ultimoResultado", r));
            // Pago más reciente
            pagoRepo.findTopByEstudianteIdOrderByFechaRegistroDesc(e.getId())
                    .ifPresent(p -> model.addAttribute("ultimoPago", p));
        });
        model.addAttribute("pageTitle", "Portal Estudiante");
        return "student/dashboard";
    }

    @GetMapping("/perfil")
    public String perfil(Model model, Principal principal) {
        estudianteRepo.findByUsuarioCorreo(principal.getName())
                .ifPresent(e -> model.addAttribute("estudiante", e));
        return "student/perfil";
    }

    @GetMapping("/resultados")
    public String resultados(Model model, Principal principal) {
        estudianteRepo.findByUsuarioCorreo(principal.getName()).ifPresent(e -> {
            model.addAttribute("estudiante", e);
            model.addAttribute("resultados", resultadoRepo.findByEstudianteId(e.getId()));
        });
        return "student/resultados";
    }

    @GetMapping("/beneficios")
    public String beneficios(Model model, Principal principal) {
        estudianteRepo.findByUsuarioCorreo(principal.getName()).ifPresent(e -> {
            model.addAttribute("estudiante", e);
            resultadoRepo.findTopByEstudianteIdOrderByFechaExamenDesc(e.getId())
                    .ifPresent(r -> model.addAttribute("ultimoResultado", r));
        });
        return "student/beneficios";
    }

    // ── Cargue de Pago Saber Pro ─────────────────────────────────
    @GetMapping("/pago")
    public String formPago(Model model, Principal principal) {
        estudianteRepo.findByUsuarioCorreo(principal.getName()).ifPresent(e -> {
            model.addAttribute("estudiante", e);
            model.addAttribute("pagos", pagoRepo.findByEstudianteId(e.getId()));
        });
        return "student/pago";
    }

    @PostMapping("/pago/registrar")
    public String registrarPago(
            @RequestParam String numeroRecibo,
            @RequestParam String fechaPago,
            @RequestParam(required = false) String valorPagado,
            @RequestParam(required = false) String descripcion,
            Principal principal) {
        estudianteRepo.findByUsuarioCorreo(principal.getName()).ifPresent(est -> {
            PagoSaberPro pago = new PagoSaberPro();
            pago.setEstudiante(est);
            pago.setNumeroRecibo(numeroRecibo);
            pago.setFechaPago(LocalDate.parse(fechaPago));
            if (valorPagado != null && !valorPagado.isBlank())
                pago.setValorPagado(new BigDecimal(valorPagado));
            pago.setDescripcion(descripcion);
            pago.setEstado(PagoSaberPro.EstadoPago.PENDIENTE);
            pagoRepo.save(pago);
        });
        return "redirect:/estudiante/pago";
    }

    // ── Resolución de Beneficios ─────────────────────────────────
    @GetMapping("/resolucion-beneficios")
    public String resolucionBeneficios(Model model, Principal principal) {
        estudianteRepo.findByUsuarioCorreo(principal.getName()).ifPresent(e -> {
            model.addAttribute("estudiante", e);
            resultadoRepo.findTopByEstudianteIdOrderByFechaExamenDesc(e.getId())
                    .ifPresent(r -> model.addAttribute("resultado", r));
        });
        return "student/resolucion-beneficios";
    }
}
