package Datos;

public class Pista {
	
	private static String Nombre;
	private static String Ubicacion;
	private static double distancia;
	private String imagen;
	
	
	public static String getNombre() {
		return Nombre;
	}
	public void setNombre(String nombre) {
		Nombre = nombre;
	}
	public static String getUbicacion() {
		return Ubicacion;
	}
	public void setUbicacion(String ubicacion) {
		Ubicacion = ubicacion;
	}
	public static double getDistancia() {
		return distancia;
	}
	public void setDistancia(double distancia) {
		this.distancia = distancia;
	}
	public String getImagen() {
		return imagen;
	}
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	
	public Pista(String nombre, String ubicacion, double distancia, String imagen) {
		super();
		Nombre = nombre;
		Ubicacion = ubicacion;
		this.distancia = distancia;
		this.imagen = imagen;
	}
	
	public Pista(String nombre, String ubicacion, double distancia) {
		super();
		Nombre = nombre;
		Ubicacion = ubicacion;
		this.distancia = distancia;
	}

}
