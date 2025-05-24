package Datos;

public class Telemetria {
	
	public double Velocidad;
	public double DistanciaRecorrida;
	public double Corriente;
	public double Voltaje;
	public double Temperatura;
	public boolean HombreMuerto;
	public double Potencia;
	private double sumaPotencia = 0.0;
	private int contadorLecturas = 0;

	public double getVelocidad() {
		return Velocidad;
	}
	public void setVelocidad(double velocidad) {
		Velocidad = velocidad;
	}
	public double getDistanciaRecorrida() {
		return DistanciaRecorrida;
	}
	public void setDistanciaRecorrida(double distanciaRecorrida) {
		DistanciaRecorrida = distanciaRecorrida;
	}
	public double getCorriente() {
		return Corriente;
	}
	public void setCorriente(double corriente) {
		Corriente = corriente;
	}
	public double getVoltaje() {
		return Voltaje;
	}
	public void setVoltaje(double voltaje) {
		Voltaje = voltaje;
	}
	public double getTemperatura() {
		return Temperatura;
	}
	public void setTemperatura(double temperatura) {
		Temperatura = temperatura;
	}
	public boolean getHombreMuerto() {
		return HombreMuerto;
	}
	public void setHombreMuerto(boolean hombreMuerto) {
		HombreMuerto = hombreMuerto;
	}

	public double getPotencia() {
		return Potencia;
	}
	public void setPotencia(double corriente ,double voltaje) {
		Potencia = corriente*voltaje;
	}
	
	public void actualizarPotencia(double potenciaInstantanea) {
	    sumaPotencia += potenciaInstantanea;
	    contadorLecturas++;
	}

	public double getPotenciaPromedio() {
	    if (contadorLecturas == 0) {
	        return 0.0; // 
	    }
	    return sumaPotencia / contadorLecturas;
	}
	
	public Telemetria(double velocidad, double distanciaRecorrida, double corriente, double voltaje, double temperatura,
			boolean hombreMuerto) {
		super();
		Velocidad = velocidad;
		DistanciaRecorrida = distanciaRecorrida;
		Corriente = corriente;
		Voltaje = voltaje;
		Temperatura = temperatura;
		HombreMuerto = hombreMuerto;
		Potencia = corriente*voltaje;
	}
	
	

}
