package com.uts.gestionsaberpro.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "docentes")
public class Docente {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "facultad_id")
    private Facultad facultad;

    @Column(length = 50)
    private String especialidad;

    @Column(nullable = false)
    private Boolean activo = true;

    public Docente() {}
    public Docente(Usuario u, Facultad f, String esp) {
        this.usuario = u; this.facultad = f; this.especialidad = esp;
    }

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public Usuario getUsuario() { return usuario; } public void setUsuario(Usuario u) { this.usuario = u; }
    public Facultad getFacultad() { return facultad; } public void setFacultad(Facultad f) { this.facultad = f; }
    public String getEspecialidad() { return especialidad; } public void setEspecialidad(String e) { this.especialidad = e; }
    public Boolean getActivo() { return activo; } public void setActivo(Boolean a) { this.activo = a; }
}
