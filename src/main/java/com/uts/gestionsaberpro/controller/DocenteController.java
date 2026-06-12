package com.uts.gestionsaberpro.controller;

import com.uts.gestionsaberpro.repository.*;
import com.uts.gestionsaberpro.service.ExcelExportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/docente")
public class DocenteController {

    private final DocenteRepository    docenteRepo;
    private final EstudianteRepository estudianteRepo;
    private final ResultadoRepository  resultadoRepo;
    private final FacultadRepository   facultadRepo;
    private final ExcelExportService   excelService;

    public DocenteController(DocenteRepository d, EstudianteRepository e,
                              ResultadoRepository r, FacultadRepository f,
                              ExcelExportService ex) {
        this.docenteRepo    = d;
        this.estudianteRepo = e;
        this.resultadoRepo  = r;
        this.facultadRepo   = f;
        this.excelService   = ex;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        docenteRepo.findByUsuarioCorreo(principal.getName())
                .ifPresent(d -> model.addAttribute("docente", d));
        model.addAttribute("totalEstudiantes", estudianteRepo.count());
        model.addAttribute("facultades", facultadRepo.findAll());
        model.addAttribute("pageTitle", "Panel Docente");
        return "docente/dashboard";
    }

    @GetMapping("/estudiantes")
    public String estudiantes(Model model) {
        model.addAttribute("estudiantes", estudianteRepo.findAll());
        model.addAttribute("pageTitle", "Alumnos");
        return "docente/estudiantes";
    }

    @GetMapping("/buscar")
    public String buscar(@RequestParam(required = false) String cedula,
                         @RequestParam(required = false) Long facultadId,
                         Model model) {
        if (cedula != null && !cedula.isBlank()) {
            estudianteRepo.findByUsuarioDocumento(cedula)
                    .ifPresent(e -> model.addAttribute("estudiante", e));
            model.addAttribute("cedula", cedula);
        }
        if (facultadId != null) {
            model.addAttribute("estudiantesFacultad",
                    estudianteRepo.findAll().stream()
                            .filter(e -> e.getPrograma() != null
                                    && e.getPrograma().getFacultad() != null
                                    && e.getPrograma().getFacultad().getId().equals(facultadId))
                            .toList());
            model.addAttribute("facultadSeleccionada", facultadId);
        }
        model.addAttribute("facultades", facultadRepo.findAll());
        return "docente/buscar";
    }

    @GetMapping("/informe-general")
    public String informeGeneral(Model model) {
        model.addAttribute("estudiantes", estudianteRepo.findAll());
        return "docente/informe-general";
    }

    @GetMapping("/informe-general/excel")
    public ResponseEntity<byte[]> exportarInformeGeneral() throws Exception {
        byte[] data = excelService.generarInformeGeneral(estudianteRepo.findAll());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=informe_general_docente.xlsx")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(data);
    }

    @GetMapping("/beneficios")
    public String beneficios(Model model) {
        model.addAttribute("beneficiarios", resultadoRepo.findBeneficiarios());
        return "docente/beneficios";
    }

    @GetMapping("/beneficios/excel")
    public ResponseEntity<byte[]> exportarBeneficios() throws Exception {
        byte[] data = excelService.generarInformeDetallado(resultadoRepo.findBeneficiarios());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=beneficios_docente.xlsx")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(data);
    }
}
