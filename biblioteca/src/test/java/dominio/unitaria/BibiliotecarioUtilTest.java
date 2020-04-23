package dominio.unitaria;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Assert;
import org.junit.Test;

import utilidad.BibliotecarioUtil;

public class BibiliotecarioUtilTest {
	private static String ISBN_SUMA_DIGITOS_MAYOR_30 = "A1B2C3D4E5F6G7H8I9";
	private static String ISBN_SUMA_DIGITOS_MENOR_30 = "A1A2A3A4";
	private static String ISBN_VACIO = "";
	private static String ISBN_PALINDROMO = "1221";
	
	
	@Test
	public void sumaMayorDe30Test() {
		// act
		boolean esMayorDe30= BibliotecarioUtil.sumaMayorDe(ISBN_SUMA_DIGITOS_MAYOR_30, 30);
		// assert
		assertTrue(esMayorDe30);
	}
	@Test
	public void noSumaMayorDe30Test() {
		// act
		boolean esMayorDe30 = BibliotecarioUtil.sumaMayorDe(ISBN_SUMA_DIGITOS_MENOR_30, 30);
		// assert
		assertFalse(esMayorDe30);
	}
	
	@Test
	public void isbnVacioSumaMayorDe30Test() {
		// act
		boolean esMayorDe30 = BibliotecarioUtil.sumaMayorDe(ISBN_VACIO, 30);
		// assert
		assertFalse(esMayorDe30);
	}
	
	@Test
	public void sumaQuinceDiasFechaTest() {
		// arrange
		Date fechaInicial = new GregorianCalendar(2017, Calendar.MAY, 24).getTime();
		Date fechaFinalCorrecta = new GregorianCalendar(2017, Calendar.JUNE, 9).getTime();
		// act
		Date fechaEntrega = BibliotecarioUtil.obtenerFechaLuegoDe(fechaInicial, 15);
		// assert
		Assert.assertEquals(fechaFinalCorrecta, fechaEntrega);
	}
	
	@Test
	public void sumaQuinceDiasFecha2Test() {
		// arrange
		Date fechaInicial = new GregorianCalendar(2017, Calendar.MAY, 26).getTime();
		Date fechaFinalCorrecta = new GregorianCalendar(2017, Calendar.JUNE, 12).getTime();
		// act
		Date fechaEntrega = BibliotecarioUtil.obtenerFechaLuegoDe(fechaInicial, 15);
		// assert
		Assert.assertEquals(fechaFinalCorrecta, fechaEntrega);
	}
	
	@Test
	public void esISBNPalindromoTest() {
		// act
		boolean esPalindromo = BibliotecarioUtil.esISBNPalindromo(ISBN_PALINDROMO);
		// assert
		Assert.assertTrue(esPalindromo);
	}
	
	@Test
	public void noEsisbnPalindromoTest() {
		// act
		boolean esPalindromo = BibliotecarioUtil.esISBNPalindromo(ISBN_SUMA_DIGITOS_MAYOR_30);
		// assert
		Assert.assertFalse(esPalindromo);
	}
}
