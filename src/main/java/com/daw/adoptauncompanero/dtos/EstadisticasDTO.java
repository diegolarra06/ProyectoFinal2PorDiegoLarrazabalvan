package com.daw.adoptauncompanero.dtos;

public class EstadisticasDTO {

	private Long totalAnimales;
	private Long animalesAdoptados;
	private Long animalesDisponibles;
	private Double porcentajeAdoptados;
	private Long totalSolicitudes;
	private Long solicitudesAprobadas;
	private Long solicitudesRechazadas;
	private String especieMasAdoptada;

	public EstadisticasDTO() {
	}

	public EstadisticasDTO(Long totalAnimales, Long animalesAdoptados, Long animalesDisponibles,
			Double porcentajeAdoptados, Long totalSolicitudes, Long solicitudesAprobadas, Long solicitudesRechazadas,
			String especieMasAdoptada) {
		this.totalAnimales = totalAnimales;
		this.animalesAdoptados = animalesAdoptados;
		this.animalesDisponibles = animalesDisponibles;
		this.porcentajeAdoptados = porcentajeAdoptados;
		this.totalSolicitudes = totalSolicitudes;
		this.solicitudesAprobadas = solicitudesAprobadas;
		this.solicitudesRechazadas = solicitudesRechazadas;
		this.especieMasAdoptada = especieMasAdoptada;
	}

	public Long getTotalAnimales() {
		return totalAnimales;
	}

	public void setTotalAnimales(Long totalAnimales) {
		this.totalAnimales = totalAnimales;
	}

	public Long getAnimalesAdoptados() {
		return animalesAdoptados;
	}

	public void setAnimalesAdoptados(Long animalesAdoptados) {
		this.animalesAdoptados = animalesAdoptados;
	}

	public Long getAnimalesDisponibles() {
		return animalesDisponibles;
	}

	public void setAnimalesDisponibles(Long animalesDisponibles) {
		this.animalesDisponibles = animalesDisponibles;
	}

	public Double getPorcentajeAdoptados() {
		return porcentajeAdoptados;
	}

	public void setPorcentajeAdoptados(Double porcentajeAdoptados) {
		this.porcentajeAdoptados = porcentajeAdoptados;
	}

	public Long getTotalSolicitudes() {
		return totalSolicitudes;
	}

	public void setTotalSolicitudes(Long totalSolicitudes) {
		this.totalSolicitudes = totalSolicitudes;
	}

	public Long getSolicitudesAprobadas() {
		return solicitudesAprobadas;
	}

	public void setSolicitudesAprobadas(Long solicitudesAprobadas) {
		this.solicitudesAprobadas = solicitudesAprobadas;
	}

	public Long getSolicitudesRechazadas() {
		return solicitudesRechazadas;
	}

	public void setSolicitudesRechazadas(Long solicitudesRechazadas) {
		this.solicitudesRechazadas = solicitudesRechazadas;
	}

	public String getEspecieMasAdoptada() {
		return especieMasAdoptada;
	}

	public void setEspecieMasAdoptada(String especieMasAdoptada) {
		this.especieMasAdoptada = especieMasAdoptada;
	}
}