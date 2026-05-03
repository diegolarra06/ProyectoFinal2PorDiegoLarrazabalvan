package com.daw.adoptauncompanero.entities;

import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.*;

// =============================================================
// ENTIDAD ANIMAL
// Cubre las funcionalidades:
//  - 2.3.1 Catálogo (listar animales con filtros)
//  - 2.3.2 Ficha del animal (información detallada)
//  - 2.2.4.1 Gestión por administrador (CRUD)
// =============================================================
@Entity
@Table(name = "animales")
public class AnimalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_animal")
    private Integer idAnimal;

    @Column(name = "nombre", nullable = false)         // 2.3.1.2
    private String nombre;

    @Column(name = "especie", nullable = false)        // 2.3.1.3 / 2.3.1.5 (filtro)
    private String especie;

    @Column(name = "edad")                             // 2.3.1.4 / 2.3.1.6 (filtro)
    private Integer edad;

    @Column(name = "tamano")                           // 2.3.1.7 (filtro)
    private String tamano;

    @Column(name = "personalidad", columnDefinition = "TEXT")    // 2.3.2.2
    private String personalidad;

    @Column(name = "necesidades_especiales", columnDefinition = "TEXT")  // 2.3.2.2
    private String necesidadesEspeciales;

    @Column(name = "estado_sanitario", columnDefinition = "TEXT")        // 2.3.2.2
    private String estadoSanitario;

    // 2.3.2.3: estado del animal (DISPONIBLE, RESERVADO, ADOPTADO)
    // Es también un filtro del catálogo (2.3.1.8)
    @Column(name = "estado", nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoAnimal estado;

    @Column(name = "fecha_alta")
    private LocalDateTime fechaAlta;

    // 2.3.2.1: imágenes del animal (PDF 6.2 - almacenamiento)
    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImagenAnimalEntity> imagenes;

    @OneToMany(mappedBy = "animal")
    private List<SolicitudAdopcionEntity> solicitudes;

    // ENUM con los tres estados (2.3.2.3)
    public enum EstadoAnimal {
        DISPONIBLE, RESERVADO, ADOPTADO
    }

    public AnimalEntity() {}

    public AnimalEntity(Integer idAnimal, String nombre, String especie, Integer edad, String tamano,
                        String personalidad, String necesidadesEspeciales, String estadoSanitario,
                        EstadoAnimal estado, LocalDateTime fechaAlta) {
        this.idAnimal = idAnimal;
        this.nombre = nombre;
        this.especie = especie;
        this.edad = edad;
        this.tamano = tamano;
        this.personalidad = personalidad;
        this.necesidadesEspeciales = necesidadesEspeciales;
        this.estadoSanitario = estadoSanitario;
        this.estado = estado;
        this.fechaAlta = fechaAlta;
    }

    public Integer getIdAnimal() { return idAnimal; }
    public void setIdAnimal(Integer idAnimal) { this.idAnimal = idAnimal; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEspecie() { return especie; }
    public void setEspecie(String especie) { this.especie = especie; }

    public Integer getEdad() { return edad; }
    public void setEdad(Integer edad) { this.edad = edad; }

    public String getTamano() { return tamano; }
    public void setTamano(String tamano) { this.tamano = tamano; }

    public String getPersonalidad() { return personalidad; }
    public void setPersonalidad(String personalidad) { this.personalidad = personalidad; }

    public String getNecesidadesEspeciales() { return necesidadesEspeciales; }
    public void setNecesidadesEspeciales(String necesidadesEspeciales) { this.necesidadesEspeciales = necesidadesEspeciales; }

    public String getEstadoSanitario() { return estadoSanitario; }
    public void setEstadoSanitario(String estadoSanitario) { this.estadoSanitario = estadoSanitario; }

    public EstadoAnimal getEstado() { return estado; }
    public void setEstado(EstadoAnimal estado) { this.estado = estado; }

    public LocalDateTime getFechaAlta() { return fechaAlta; }
    public void setFechaAlta(LocalDateTime fechaAlta) { this.fechaAlta = fechaAlta; }

    public List<ImagenAnimalEntity> getImagenes() { return imagenes; }
    public void setImagenes(List<ImagenAnimalEntity> imagenes) { this.imagenes = imagenes; }

    public List<SolicitudAdopcionEntity> getSolicitudes() { return solicitudes; }
    public void setSolicitudes(List<SolicitudAdopcionEntity> solicitudes) { this.solicitudes = solicitudes; }
}
