package com.daw.adoptauncompanero.entities;

import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "animales")
public class AnimalEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_animal")
	private Integer idAnimal;

	@Column(name = "nombre", nullable = false)
	private String nombre;

	@Column(name = "especie", nullable = false)
	private String especie;

	@Column(name = "edad")
	private Integer edad;

	@Column(name = "tamano")
	private String tamano;

	@Column(name = "personalidad", columnDefinition = "TEXT")
	private String personalidad;

	@Column(name = "necesidades_especiales", columnDefinition = "TEXT")
	private String necesidadesEspeciales;

	@Column(name = "estado_sanitario", columnDefinition = "TEXT")
	private String estadoSanitario;

	@Column(name = "estado", nullable = false)
	@Enumerated(EnumType.STRING)
	private EstadoAnimal estado;

	@Column(name = "fecha_alta")
	private LocalDateTime fechaAlta;

	@OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ImagenAnimalEntity> imagenes;

	@OneToMany(mappedBy = "animal")
	private List<SolicitudAdopcionEntity> solicitudes;

	public enum EstadoAnimal {
		DISPONIBLE, RESERVADO, ADOPTADO
	}

	public AnimalEntity() {
	}

	public AnimalEntity(Integer idAnimal, String nombre, String especie, Integer edad, String tamano,
			String personalidad, String necesidadesEspeciales, String estadoSanitario, EstadoAnimal estado,
			LocalDateTime fechaAlta) {
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

	public Integer getIdAnimal() {
		return idAnimal;
	}

	public void setIdAnimal(Integer idAnimal) {
		this.idAnimal = idAnimal;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEspecie() {
		return especie;
	}

	public void setEspecie(String especie) {
		this.especie = especie;
	}

	public Integer getEdad() {
		return edad;
	}

	public void setEdad(Integer edad) {
		this.edad = edad;
	}

	public String getTamano() {
		return tamano;
	}

	public void setTamano(String tamano) {
		this.tamano = tamano;
	}

	public String getPersonalidad() {
		return personalidad;
	}

	public void setPersonalidad(String personalidad) {
		this.personalidad = personalidad;
	}

	public String getNecesidadesEspeciales() {
		return necesidadesEspeciales;
	}

	public void setNecesidadesEspeciales(String necesidadesEspeciales) {
		this.necesidadesEspeciales = necesidadesEspeciales;
	}

	public String getEstadoSanitario() {
		return estadoSanitario;
	}

	public void setEstadoSanitario(String estadoSanitario) {
		this.estadoSanitario = estadoSanitario;
	}

	public EstadoAnimal getEstado() {
		return estado;
	}

	public void setEstado(EstadoAnimal estado) {
		this.estado = estado;
	}

	public LocalDateTime getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(LocalDateTime fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public List<ImagenAnimalEntity> getImagenes() {
		return imagenes;
	}

	public void setImagenes(List<ImagenAnimalEntity> imagenes) {
		this.imagenes = imagenes;
	}

	public List<SolicitudAdopcionEntity> getSolicitudes() {
		return solicitudes;
	}

	public void setSolicitudes(List<SolicitudAdopcionEntity> solicitudes) {
		this.solicitudes = solicitudes;
	}
}
