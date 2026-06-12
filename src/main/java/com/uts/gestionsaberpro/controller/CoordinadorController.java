package com.uts.gestionsaberpro.controller;

import com.uts.gestionsaberpro.entity.*;
import com.uts.gestionsaberpro.repository.*;
import com.uts.gestionsaberpro.service.BeneficioService;
import com.uts.gestionsaberpro.service.ExcelExportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.HashSet;

@Controller
@RequestMapping("/coordinador")
public class CoordinadorController {

    private final CoordinadorRepository    coordinadorRepo;
    private final EstudianteRepository     estudianteRepo;
    private final ResultadoRepository      resultadoRepo;
    private final ProgramaRepository       programaRepo;
    private final RoleRepository           roleRepo;
    private final UsuarioRepository        usuarioRepo;
    private final PasswordEncoder          passwordEncoder;
    private final ExcelExportService       excelService;
    private final PagoSaberProRepository   pagoRepo;
    private final BeneficioService         beneficioService;

    public CoordinadorController(CoordinadorRepository coordinadorRepo,
                                  EstudianteRepository estudianteRepo,
                                  ResultadoRepository resultadoRepo,
                                  ProgramaRepository programaRepo,
                                  RoleRepository roleRepo,
                                  UsuarioRepository usuarioRepo,
                                  PasswordEncoder passwordEncoder,
                                  ExcelExportService excelService,
                                  PagoSaberProRepository pagoRepo,
                                  BeneficioService beneficioService) {
        this.coordinadorRepo = coordinadorRepo;
        this.estudianteRepo  = estudianteRepo;
        this.resultadoRepo   = resultadoRepo;
        this.programaRepo    = programaRepo;
        this.roleRepo        = roleRepo;
        this.usuarioRepo     = usuarioRepo;
        this.passwordEncoder = passwordEncoder;
        this.excelService    = excelService;
        this.pagoRepo        = pagoRepo;
        this.beneficioService = beneficioService;
    }

    // ── Dashboard ────────────────────────────────────────────────
    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        long total         = estudianteRepo.count();
        long conResultados = estudianteRepo.findAll().stream()
                .filter(e -> !e.getResultados().isEmpty()).count();
        coordinadorRepo.findByUsuarioCorreo(principal.getName())
                .ifPresent(c -> model.addAttribute("coordinador", c));
        model.addAttribute("totalEstudiantes", total);
        model.addAttribute("conResultados",    conResultados);
        model.addAttribute("sinResultados",    total - conResultados);
        model.addAttribute("beneficiarios",    resultadoRepo.findBeneficiarios().size());
        model.addAttribute("pagosPendientes",  pagoRepo.findByEstado(PagoSaberPro.EstadoPago.PENDIENTE).size());
        model.addAttribute("pageTitle", "Panel Coordinador");
        return "coordinator/dashboard";
    }

    // ── CRUD Estudiantes ─────────────────────────────────────────
    @GetMapping("/estudiantes")
    public String estudiantes(Model model) {
        model.addAttribute("estudiantes", estudianteRepo.findAll());
        model.addAttribute("programas",   programaRepo.findAll());
        model.addAttribute("pageTitle",   "Gestión de Alumnos");
        return "coordinator/estudiantes";
    }

    @PostMapping("/estudiantes/crear")
    public String crearEstudiante(
            @RequestParam String documento,
            @RequestParam String primerNombre,
            @RequestParam String primerApellido,
            @RequestParam(required = false) String segundoNombre,
            @RequestParam(required = false) String segundoApellido,
            @RequestParam String correo,
            @RequestParam(required = false) String telefono,
            @RequestParam(required = false) String numeroRegistro,
            @RequestParam Integer semestre,
            @RequestParam Long programaId) {
        Role roleEst = roleRepo.findByName(Role.RoleName.ROLE_ESTUDIANTE).orElseThrow();
        Usuario u = new Usuario();
        u.setDocumento(documento); u.setTipoDocumento(Usuario.TipoDocumento.CC);
        u.setPrimerNombre(primerNombre); u.setSegundoNombre(segundoNombre);
        u.setPrimerApellido(primerApellido); u.setSegundoApellido(segundoApellido);
        u.setCorreo(correo); u.setTelefono(telefono);
        u.setPassword(passwordEncoder.encode(documento));
        u.setRoles(new HashSet<>() {{ add(roleEst); }});
        u = usuarioRepo.save(u);
        Estudiante est = new Estudiante();
        est.setUsuario(u); est.setSemestre(semestre); est.setNumeroRegistro(numeroRegistro);
        programaRepo.findById(programaId).ifPresent(est::setPrograma);
        estudianteRepo.save(est);
        return "redirect:/coordinador/estudiantes";
    }

    @GetMapping("/estudiantes/desactivar/{id}")
    public String desactivarEstudiante(@PathVariable Long id) {
        estudianteRepo.findById(id).ifPresent(e -> { e.setActivo(false); estudianteRepo.save(e); });
        return "redirect:/coordinador/estudiantes";
    }

    @GetMapping("/estudiantes/activar/{id}")
    public String activarEstudiante(@PathVariable Long id) {
        estudianteRepo.findById(id).ifPresent(e -> { e.setActivo(true); estudianteRepo.save(e); });
        return "redirect:/coordinador/estudiantes";
    }

    // ── Calificar Estudiante ──────────────────────────────────────
    @GetMapping("/estudiantes/calificar/{id}")
    public String formCalificar(@PathVariable Long id, Model model) {
        estudianteRepo.findById(id).ifPresent(e -> model.addAttribute("estudiante", e));
        model.addAttribute("pageTitle", "Calificar Estudiante");
        return "coordinator/calificar";
    }

    @PostMapping("/estudiantes/calificar/{id}")
    public String calificarEstudiante(
            @PathVariable Long id,
            @RequestParam int comunicacionEscrita,
            @RequestParam int razonamientoCuantitativo,
            @RequestParam int lecturaCritica,
            @RequestParam int competenciasCiudadanas,
            @RequestParam int ingles,
            @RequestParam int formulacionProyectos,
            @RequestParam int pensamientoCientifico,
            @RequestParam int disenioSoftware,
            @RequestParam String nivelIngles,
            @RequestParam String fechaExamen) {
        estudianteRepo.findById(id).ifPresent(est -> {
            ResultadoSaberPro r = new ResultadoSaberPro();
            r.setEstudiante(est);
            r.setFechaExamen(LocalDate.parse(fechaExamen));
            r.setComunicacionEscrita(new BigDecimal(comunicacionEscrita));
            r.setRazonamientoCuantitativo(new BigDecimal(razonamientoCuantitativo));
            r.setLecturaCritica(new BigDecimal(lecturaCritica));
            r.setCompetenciasCiudadanas(new BigDecimal(competenciasCiudadanas));
            r.setIngles(new BigDecimal(ingles));
            r.setFormulacionProyectos(new BigDecimal(formulacionProyectos));
            r.setPensamientoCientifico(new BigDecimal(pensamientoCientifico));
            r.setDisenioSoftware(new BigDecimal(disenioSoftware));
            r.setNivelIngles(nivelIngles);
            r.calcularPuntajeYNivel();
            beneficioService.calcularBeneficios(r);
            resultadoRepo.save(r);
        });
        return "redirect:/coordinador/estudiantes";
    }

    // ── Aprobar Pago Saber Pro ───────────────────────────────────
    @GetMapping("/pagos")
    public String pagos(Model model) {
        model.addAttribute("pagos", pagoRepo.findAll());
        model.addAttribute("pendientes", pagoRepo.findByEstado(PagoSaberPro.EstadoPago.PENDIENTE));
        model.addAttribute("pageTitle", "Gestión de Pagos Saber Pro");
        return "coordinator/pagos";
    }

    @GetMapping("/pagos/aprobar/{id}")
    public String aprobarPago(@PathVariable Long id,
                               @RequestParam(required = false) String observacion) {
        pagoRepo.findById(id).ifPresent(p -> {
            p.setEstado(PagoSaberPro.EstadoPago.APROBADO);
            if (observacion != null) p.setObservacionCoordinador(observacion);
            pagoRepo.save(p);
        });
        return "redirect:/coordinador/pagos";
    }

    @GetMapping("/pagos/rechazar/{id}")
    public String rechazarPago(@PathVariable Long id) {
        pagoRepo.findById(id).ifPresent(p -> {
            p.setEstado(PagoSaberPro.EstadoPago.RECHAZADO);
            pagoRepo.save(p);
        });
        return "redirect:/coordinador/pagos";
    }

    // ── Informes ─────────────────────────────────────────────────
    @GetMapping("/informe-general")
    public String informeGeneral(Model model) {
        model.addAttribute("estudiantes", estudianteRepo.findAll());
        model.addAttribute("pageTitle", "Informe General");
        return "coordinator/informe-general";
    }

    @GetMapping("/informe-general/excel")
    public ResponseEntity<byte[]> exportarInformeGeneral() throws Exception {
        byte[] data = excelService.generarInformeGeneral(estudianteRepo.findAll());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=informe_general.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(data);
    }

    @GetMapping("/informe-detallado")
    public String informeDetallado(Model model) {
        model.addAttribute("resultados", resultadoRepo.findAll());
        model.addAttribute("pageTitle", "Informe Detallado");
        return "coordinator/informe-detallado";
    }

    @GetMapping("/informe-detallado/excel")
    public ResponseEntity<byte[]> exportarInformeDetallado() throws Exception {
        byte[] data = excelService.generarInformeDetallado(resultadoRepo.findAll());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=informe_detallado.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(data);
    }

    @GetMapping("/beneficios")
    public String beneficios(Model model) {
        model.addAttribute("beneficiarios", resultadoRepo.findBeneficiarios());
        model.addAttribute("pageTitle", "Informe de Beneficios");
        return "coordinator/beneficios";
    }

    @GetMapping("/beneficios/excel")
    public ResponseEntity<byte[]> exportarBeneficios() throws Exception {
        byte[] data = excelService.generarInformeDetallado(resultadoRepo.findBeneficiarios());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=beneficios.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(data);
    }

    // ── Resolución de Beneficios ─────────────────────────────────
    @GetMapping("/resolucion-beneficios")
    public String resolucionBeneficios(Model model) {
        model.addAttribute("superiores", resultadoRepo.findBeneficiarios().stream()
                .filter(r -> r.getNivelGlobal() == ResultadoSaberPro.NivelDesempeno.SUPERIOR).toList());
        model.addAttribute("altos", resultadoRepo.findBeneficiarios().stream()
                .filter(r -> r.getNivelGlobal() == ResultadoSaberPro.NivelDesempeno.ALTO).toList());
        model.addAttribute("pageTitle", "Resolución de Beneficios");
        return "coordinator/resolucion-beneficios";
    }
}
