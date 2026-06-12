package com.uts.gestionsaberpro.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "estudiantes")
public class Estudiante {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "programa_id")
    private Programa programa;

    @Column(name = "numero_registro", length = 30)
    private String numeroRegistro;

    @Column(nullable = false)
    private Integer semestre = 10;

    @Column(nullable = false)
    private Boolean activo = true;

    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResultadoSaberPro> resultados = new ArrayList<>();

    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PagoSaberPro> pagos = new ArrayList<>();

    public Estudiante() {}

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public Usuario getUsuario() { return usuario; } public void setUsuario(Usuario u) { this.usuario = u; }
    public Programa getPrograma() { return programa; } public void setPrograma(Programa p) { this.programa = p; }
    public String getNumeroRegistro() { return numeroRegistro; } public void setNumeroRegistro(String n) { this.numeroRegistro = n; }
    public Integer getSemestre() { return semestre; } public void setSemestre(Integer s) { this.semestre = s; }
    public Boolean getActivo() { return activo; } public void setActivo(Boolean a) { this.activo = a; }
    public List<ResultadoSaberPro> getResultados() { return resultados; } public void setResultados(List<ResultadoSaberPro> r) { this.resultados = r; }
    public List<PagoSaberPro> getPagos() { return pagos; } public void setPagos(List<PagoSaberPro> p) { this.pagos = p; }
}
