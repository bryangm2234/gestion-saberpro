package com.uts.gestionsaberpro.entity;
import jakarta.persistence.*;
import java.util.List;
@Entity @Table(name = "programas")
public class Programa {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, length = 100) private String nombre;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20) private TipoPrograma tipo;
    @ManyToOne @JoinColumn(name = "facultad_id") private Facultad facultad;
    @Column(nullable = false) private Boolean activo = true;
    @OneToMany(mappedBy = "programa") private List<Estudiante> estudiantes;
    public Programa() {}
    public Programa(String nombre, TipoPrograma tipo) { this.nombre = nombre; this.tipo = tipo; }
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; } public void setNombre(String n) { this.nombre = n; }
    public TipoPrograma getTipo() { return tipo; } public void setTipo(TipoPrograma t) { this.tipo = t; }
    public Facultad getFacultad() { return facultad; } public void setFacultad(Facultad f) { this.facultad = f; }
    public Boolean getActivo() { return activo; } public void setActivo(Boolean a) { this.activo = a; }
    public List<Estudiante> getEstudiantes() { return estudiantes; } public void setEstudiantes(List<Estudiante> e) { this.estudiantes = e; }
    public enum TipoPrograma { PROFESIONAL, TECNOLOGIA }
}
