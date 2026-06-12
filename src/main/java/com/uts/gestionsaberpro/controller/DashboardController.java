package com.uts.gestionsaberpro.controller;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class DashboardController {
    @GetMapping("/dashboard") public String dashboard(Authentication auth) {
        if (auth == null) return "redirect:/login";
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) return "redirect:/admin/dashboard";
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_COORDINADOR"))) return "redirect:/coordinador/dashboard";
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_DOCENTE"))) return "redirect:/docente/dashboard";
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ESTUDIANTE"))) return "redirect:/estudiante/dashboard";
        return "redirect:/login";
    }
    @GetMapping("/") public String root() { return "redirect:/dashboard"; }
    @GetMapping("/error/403") public String forbidden() { return "error/403"; }
}
