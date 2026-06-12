package com.uts.gestionsaberpro.controller;

import com.uts.gestionsaberpro.entity.*;
import com.uts.gestionsaberpro.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.HashSet;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final CoordinadorRepository coordinadorRepo;
    private final DocenteRepository     docenteRepo;
    private final FacultadRepository    facultadRepo;
    private final UsuarioRepository     usuarioRepo;
    private final RoleRepository        roleRepo;
    private final PasswordEncoder       passwordEncoder;

    public AdminController(CoordinadorRepository c, DocenteRepository d, FacultadRepository f,
                            UsuarioRepository u, RoleRepository r, PasswordEncoder p) {
        this.coordinadorRepo = c; this.docenteRepo = d; this.facultadRepo = f;
        this.usuarioRepo = u; this.roleRepo = r; this.passwordEncoder = p;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("coordinadores", coordinadorRepo.findAll());
        model.addAttribute("docentes",      docenteRepo.findAll());
        model.addAttribute("facultades",    facultadRepo.findAll());
        model.addAttribute("pageTitle",     "Panel Administrador");
        return "admin/dashboard";
    }

    // ── CRUD Coordinadores ───────────────────────────────
    @PostMapping("/coordinadores/crear")
    public String crearCoordinador(
            @RequestParam String documento,
            @RequestParam String primerNombre,
            @RequestParam String primerApellido,
            @RequestParam String correo,
            @RequestParam String password,
            @RequestParam(required = false) String areaAsignada,
            @RequestParam String tipoDocumento) {

        Role role = roleRepo.findByName(Role.RoleName.ROLE_COORDINADOR).orElseThrow();
        Usuario u = new Usuario();
        u.setDocumento(documento);
        u.setTipoDocumento(Usuario.TipoDocumento.valueOf(tipoDocumento));
        u.setPrimerNombre(primerNombre);
        u.setPrimerApellido(primerApellido);
        u.setCorreo(correo);
        u.setPassword(passwordEncoder.encode(password));
        u.setRoles(new HashSet<>() {{ add(role); }});
        u = usuarioRepo.save(u);

        Coordinador coord = new Coordinador();
        coord.setUsuario(u);
        coord.setAreaAsignada(areaAsignada);
        coordinadorRepo.save(coord);

        return "redirect:/admin/dashboard";
    }

    @GetMapping("/coordinadores/desactivar/{id}")
    public String desactivarCoord(@PathVariable Long id) {
        coordinadorRepo.findById(id).ifPresent(c -> {
            c.setActivo(false);
            coordinadorRepo.save(c);
        });
        return "redirect:/admin/dashboard";
    }

    // ── CRUD Docentes ────────────────────────────────────
    @PostMapping("/docentes/crear")
    public String crearDocente(
            @RequestParam String documento,
            @RequestParam String primerNombre,
            @RequestParam String primerApellido,
            @RequestParam String correo,
            @RequestParam String password,
            @RequestParam(required = false) Long facultadId,
            @RequestParam(required = false) String especialidad) {

        Role role = roleRepo.findByName(Role.RoleName.ROLE_DOCENTE).orElseThrow();
        Usuario u = new Usuario();
        u.setDocumento(documento);
        u.setTipoDocumento(Usuario.TipoDocumento.CC);
        u.setPrimerNombre(primerNombre);
        u.setPrimerApellido(primerApellido);
        u.setCorreo(correo);
        u.setPassword(passwordEncoder.encode(password));
        u.setRoles(new HashSet<>() {{ add(role); }});
        u = usuarioRepo.save(u);

        Docente docente = new Docente();
        docente.setUsuario(u);
        docente.setEspecialidad(especialidad);
        if (facultadId != null)
            facultadRepo.findById(facultadId).ifPresent(docente::setFacultad);
        docenteRepo.save(docente);

        return "redirect:/admin/dashboard";
    }

    @GetMapping("/docentes/desactivar/{id}")
    public String desactivarDocente(@PathVariable Long id) {
        docenteRepo.findById(id).ifPresent(d -> {
            d.setActivo(false);
            docenteRepo.save(d);
        });
        return "redirect:/admin/dashboard";
    }

    // ── CRUD Facultades ──────────────────────────────────
    @PostMapping("/facultades/crear")
    public String crearFacultad(
            @RequestParam String nombre,
            @RequestParam(required = false) String codigo) {
        facultadRepo.save(new Facultad(nombre, codigo));
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/facultades/eliminar/{id}")
    public String eliminarFacultad(@PathVariable Long id) {
        facultadRepo.deleteById(id);
        return "redirect:/admin/dashboard";
    }
}
