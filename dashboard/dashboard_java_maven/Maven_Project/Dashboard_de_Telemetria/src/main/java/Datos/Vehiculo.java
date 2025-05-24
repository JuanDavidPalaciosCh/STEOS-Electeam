package Datos;

public class Vehiculo {
	
	private String Nombre;
	private String Categoria;
	private int NumDeRuedas;
	private Telemetria telemetria;
	private String Imagen;
	private String Piloto;
	private double peso;
	
	public String getNombre() {
		return Nombre;
	}
	public void setNombre(String nombre) {
		Nombre = nombre;
	}
	public String getCategoria() {
		return Categoria;
	}
	public void setCategoria(String categoria) {
		Categoria = categoria;
	}
	public int getNumDeRuedas() {
		return NumDeRuedas;
	}
	public void setNumDeRuedas(int numDeRuedas) {
		NumDeRuedas = numDeRuedas;
	}
	public Telemetria getTelemetria() {
		return telemetria;
	}
	public void setTelemetria(Telemetria telemetria) {
		this.telemetria = telemetria;
	}
	public String getImagen() {
		return Imagen;
	}
	public void setImagen(String imagen) {
		Imagen = imagen;
	}
	
	public String getPiloto() {
		return Piloto;
	}
	public void setPiloto(String piloto) {
		Piloto = piloto;
	}
	public double getPeso() {
		return peso;
	}
	public void setPeso(double peso) {
		this.peso = peso;
	}
	
	
	public Vehiculo(String nombre, String categoria, int numDeRuedas, Telemetria telemetria, String imagen,String piloto,double peso) {
		super();
		Nombre = nombre;
		Categoria = categoria;
		NumDeRuedas = numDeRuedas;
		this.telemetria = telemetria;
		Imagen = imagen;
		Piloto = piloto;
		this.peso = peso;
	}

	
	
	

}
