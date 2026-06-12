package com.uts.gestionsaberpro.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "coordinadores")
public class Coordinador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @Column(name = "area_asignada", length = 100)
    private String areaAsignada;

    @Column(nullable = false)
    private Boolean activo = true;

    public Coordinador() {}

    public Coordinador(Usuario usuario, String areaAsignada) {
        this.usuario = usuario;
        this.areaAsignada = areaAsignada;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public String getAreaAsignada() { return areaAsignada; }
    public void setAreaAsignada(String areaAsignada) { this.areaAsignada = areaAsignada; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}
