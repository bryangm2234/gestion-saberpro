package com.uts.gestionsaberpro.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Entity
@Table(name = "resultados_saber_pro")
public class ResultadoSaberPro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;

    @Column(name = "fecha_examen")
    private LocalDate fechaExamen;

    @Column(name = "comunicacion_escrita", precision = 6, scale = 2)
    private BigDecimal comunicacionEscrita;

    @Column(name = "razonamiento_cuantitativo", precision = 6, scale = 2)
    private BigDecimal razonamientoCuantitativo;

    @Column(name = "lectura_critica", precision = 6, scale = 2)
    private BigDecimal lecturaCritica;

    @Column(name = "competencias_ciudadanas", precision = 6, scale = 2)
    private BigDecimal competenciasCiudadanas;

    @Column(name = "ingles", precision = 6, scale = 2)
    private BigDecimal ingles;

    @Column(name = "formulacion_proyectos", precision = 6, scale = 2)
    private BigDecimal formulacionProyectos;

    @Column(name = "pensamiento_cientifico", precision = 6, scale = 2)
    private BigDecimal pensamientoCientifico;

    @Column(name = "disenio_software", precision = 6, scale = 2)
    private BigDecimal disenioSoftware;

    @Column(name = "puntaje_global", precision = 6, scale = 2)
    private BigDecimal puntajeGlobal;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_global", length = 20)
    private NivelDesempeno nivelGlobal;

    @Column(name = "nivel_ingles", length = 5)
    private String nivelIngles;

    @Column(name = "tiene_beneficio")
    private Boolean tieneBeneficio = false;

    @Column(name = "nota_trabajo_grado", precision = 3, scale = 1)
    private BigDecimal notaTrabajoGrado;

    @Column(name = "beca_derechos_grado", precision = 5, scale = 2)
    private BigDecimal becaDerechosGrado;

    @Column(name = "vigencia_incentivo")
    private LocalDate vigenciaIncentivo;

    @Column(name = "descripcion_beneficio", length = 255)
    private String descripcionBeneficio;

    public ResultadoSaberPro() {}

    public void calcularPuntajeYNivel() {
        BigDecimal suma = BigDecimal.ZERO;
        int cantidad = 0;
        BigDecimal[] modulos = {comunicacionEscrita, razonamientoCuantitativo,
                lecturaCritica, competenciasCiudadanas,
                formulacionProyectos, pensamientoCientifico, disenioSoftware};
        for (BigDecimal m : modulos) {
            if (m != null) { suma = suma.add(m); cantidad++; }
        }
        this.puntajeGlobal = (cantidad > 0)
                ? suma.divide(BigDecimal.valueOf(cantidad), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
        this.nivelGlobal = NivelDesempeno.from(this.puntajeGlobal);
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Estudiante getEstudiante() { return estudiante; }
    public void setEstudiante(Estudiante estudiante) { this.estudiante = estudiante; }
    public LocalDate getFechaExamen() { return fechaExamen; }
    public void setFechaExamen(LocalDate fechaExamen) { this.fechaExamen = fechaExamen; }
    public BigDecimal getComunicacionEscrita() { return comunicacionEscrita; }
    public void setComunicacionEscrita(BigDecimal v) { this.comunicacionEscrita = v; }
    public BigDecimal getRazonamientoCuantitativo() { return razonamientoCuantitativo; }
    public void setRazonamientoCuantitativo(BigDecimal v) { this.razonamientoCuantitativo = v; }
    public BigDecimal getLecturaCritica() { return lecturaCritica; }
    public void setLecturaCritica(BigDecimal v) { this.lecturaCritica = v; }
    public BigDecimal getCompetenciasCiudadanas() { return competenciasCiudadanas; }
    public void setCompetenciasCiudadanas(BigDecimal v) { this.competenciasCiudadanas = v; }
    public BigDecimal getIngles() { return ingles; }
    public void setIngles(BigDecimal ingles) { this.ingles = ingles; }
    public BigDecimal getFormulacionProyectos() { return formulacionProyectos; }
    public void setFormulacionProyectos(BigDecimal v) { this.formulacionProyectos = v; }
    public BigDecimal getPensamientoCientifico() { return pensamientoCientifico; }
    public void setPensamientoCientifico(BigDecimal v) { this.pensamientoCientifico = v; }
    public BigDecimal getDisenioSoftware() { return disenioSoftware; }
    public void setDisenioSoftware(BigDecimal v) { this.disenioSoftware = v; }
    public BigDecimal getPuntajeGlobal() { return puntajeGlobal; }
    public void setPuntajeGlobal(BigDecimal puntajeGlobal) { this.puntajeGlobal = puntajeGlobal; }
    public NivelDesempeno getNivelGlobal() { return nivelGlobal; }
    public void setNivelGlobal(NivelDesempeno nivelGlobal) { this.nivelGlobal = nivelGlobal; }
    public String getNivelIngles() { return nivelIngles; }
    public void setNivelIngles(String nivelIngles) { this.nivelIngles = nivelIngles; }
    public Boolean getTieneBeneficio() { return tieneBeneficio; }
    public void setTieneBeneficio(Boolean tieneBeneficio) { this.tieneBeneficio = tieneBeneficio; }
    public BigDecimal getNotaTrabajoGrado() { return notaTrabajoGrado; }
    public void setNotaTrabajoGrado(BigDecimal v) { this.notaTrabajoGrado = v; }
    public BigDecimal getBecaDerechosGrado() { return becaDerechosGrado; }
    public void setBecaDerechosGrado(BigDecimal v) { this.becaDerechosGrado = v; }
    public LocalDate getVigenciaIncentivo() { return vigenciaIncentivo; }
    public void setVigenciaIncentivo(LocalDate vigenciaIncentivo) { this.vigenciaIncentivo = vigenciaIncentivo; }
    public String getDescripcionBeneficio() { return descripcionBeneficio; }
    public void setDescripcionBeneficio(String descripcionBeneficio) { this.descripcionBeneficio = descripcionBeneficio; }

    public enum NivelDesempeno {
        INFERIOR, BAJO, MEDIO, ALTO, SUPERIOR;
        public static NivelDesempeno from(BigDecimal puntaje) {
            if (puntaje == null) return INFERIOR;
            int p = puntaje.intValue();
            if (p >= 241) return SUPERIOR;
            if (p >= 201) return ALTO;
            if (p >= 151) return MEDIO;
            if (p >= 101) return BAJO;
            return INFERIOR;
        }
    }
}
