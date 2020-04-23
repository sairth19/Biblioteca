package dominio;

import java.util.Date;

import dominio.excepcion.PrestamoException;
import dominio.repositorio.RepositorioLibro;
import dominio.repositorio.RepositorioPrestamo;
import utilidad.BibliotecarioUtil;

public class Bibliotecario {

	public static final String EL_LIBRO_NO_SE_ENCUENTRA_DISPONIBLE = "El libro no se encuentra disponible";
	public static final String LIBRO_SOLO_DISPONIBLE_EN_BIBLIOTECA = "Los libros palindromos solo "
			+ "se pueden user en biblioteca";
	public static final String LIBRO_NO_EXISTE = "El libro no existe en nuestro catalogo!";
	public static final String ISBN_INVALIDO = "ISBN no valido!";
	public static final int SUMA_MAXIMA_DIGITOS_ISBN = 30;
	public static final int DIAS_MAXIMO_PRESTAMO = 15; 

	private RepositorioLibro repositorioLibro;
	private RepositorioPrestamo repositorioPrestamo;

	public Bibliotecario(RepositorioLibro repositorioLibro, RepositorioPrestamo repositorioPrestamo) {
		this.repositorioLibro = repositorioLibro;
		this.repositorioPrestamo = repositorioPrestamo;

	}

	public void prestar(String isbn, String nombreUsuario) {
		if(isbn == null) {
			throw new PrestamoException(ISBN_INVALIDO);
		}
		
		if(BibliotecarioUtil.esISBNPalindromo(isbn)) {
			throw new PrestamoException(LIBRO_SOLO_DISPONIBLE_EN_BIBLIOTECA);
		}
		
		Libro libroAprestar = this.repositorioLibro.obtenerPorIsbn(isbn);
		if(libroAprestar == null) {
			throw new PrestamoException(LIBRO_NO_EXISTE);
		}
		
		if(esPrestado(libroAprestar.getIsbn())) {
			throw new PrestamoException(EL_LIBRO_NO_SE_ENCUENTRA_DISPONIBLE);
		}
		
		boolean sumaMayorDe30 = BibliotecarioUtil.sumaMayorDe(isbn, SUMA_MAXIMA_DIGITOS_ISBN);
		Date fechaPrestamo = new Date();
		Date fechaMaximaEntrega = null;
		if(sumaMayorDe30) {
			fechaMaximaEntrega = BibliotecarioUtil.obtenerFechaLuegoDe(fechaPrestamo, DIAS_MAXIMO_PRESTAMO);
		}		
		Prestamo prestamo = new Prestamo(fechaPrestamo, libroAprestar, fechaMaximaEntrega, nombreUsuario);
		repositorioPrestamo.agregar(prestamo);
	}

	public boolean esPrestado(String isbn) {
		Libro libroAprestar = this.repositorioLibro.obtenerPorIsbn(isbn);
		if(libroAprestar == null) {
			throw new PrestamoException(LIBRO_NO_EXISTE);
		}
		Libro libroPrestado = this.repositorioPrestamo.obtenerLibroPrestadoPorIsbn(libroAprestar.getIsbn());
		
		return libroPrestado != null;
		
	}

}
