package com.uts.gestionsaberpro.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagos_saber_pro")
public class PagoSaberPro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;

    @Column(name = "fecha_pago")
    private LocalDate fechaPago;

    @Column(name = "numero_recibo", length = 50)
    private String numeroRecibo;

    @Column(name = "valor_pagado", precision = 10)
    private java.math.BigDecimal valorPagado;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 20)
    private EstadoPago estado = EstadoPago.PENDIENTE;

    @Column(name = "observacion_coordinador", length = 255)
    private String observacionCoordinador;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    public PagoSaberPro() {}

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public Estudiante getEstudiante() { return estudiante; } public void setEstudiante(Estudiante e) { this.estudiante = e; }
    public LocalDate getFechaPago() { return fechaPago; } public void setFechaPago(LocalDate f) { this.fechaPago = f; }
    public String getNumeroRecibo() { return numeroRecibo; } public void setNumeroRecibo(String n) { this.numeroRecibo = n; }
    public java.math.BigDecimal getValorPagado() { return valorPagado; } public void setValorPagado(java.math.BigDecimal v) { this.valorPagado = v; }
    public String getDescripcion() { return descripcion; } public void setDescripcion(String d) { this.descripcion = d; }
    public EstadoPago getEstado() { return estado; } public void setEstado(EstadoPago e) { this.estado = e; }
    public String getObservacionCoordinador() { return observacionCoordinador; } public void setObservacionCoordinador(String o) { this.observacionCoordinador = o; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; } public void setFechaRegistro(LocalDateTime f) { this.fechaRegistro = f; }

    public enum EstadoPago {
        PENDIENTE, APROBADO, RECHAZADO
    }
}
