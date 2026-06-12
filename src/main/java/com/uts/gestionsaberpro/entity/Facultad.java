package com.uts.gestionsaberpro.entity;
import jakarta.persistence.*;
import java.util.List;
@Entity @Table(name = "facultades")
public class Facultad {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, length = 100) private String nombre;
    @Column(length = 20) private String codigo;
    @Column(nullable = false) private Boolean activo = true;
    @OneToMany(mappedBy = "facultad") private List<Programa> programas;
    public Facultad() {}
    public Facultad(String nombre, String codigo) { this.nombre = nombre; this.codigo = codigo; }
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; } public void setNombre(String n) { this.nombre = n; }
    public String getCodigo() { return codigo; } public void setCodigo(String c) { this.codigo = c; }
    public Boolean getActivo() { return activo; } public void setActivo(Boolean a) { this.activo = a; }
    public List<Programa> getProgramas() { return programas; } public void setProgramas(List<Programa> p) { this.programas = p; }
}
