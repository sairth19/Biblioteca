package utilidad;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

public class BibliotecarioUtil {
	/**
	 * Metodo encargado de verificar si un ISBN es palindromo.
	 * 
	 * @param isbn. ISBN a verificar
	 * @return true En caso de que el ISBN, false en caso contrario.
	 */
	public static boolean esISBNPalindromo(String isbn) {
		String isbnAlReves = new StringBuilder(isbn).reverse().toString();
		return isbn.equalsIgnoreCase(isbnAlReves);
	}

	/**
	 * Metodo encarcado de verificar que la suma de los digitos de un isbn no sea
	 * mayor a n
	 * 
	 * @param isbn       ISBN a verificar
	 * @param sumaMaxima valor maximo que puede ser la suma de los digitos de un
	 *                   ISBN
	 * @return true si la suma de los digitos del ISBN es mayor a sumaMaxima, false
	 *         en caso contrario
	 */
	public static boolean sumaMayorDe(String isbn, int sumaMaxima) {
		String digitos = isbn.replaceAll("[^0-9]", "");
		if (digitos.isEmpty())
			return false;

		int suma = 0;
		for (int i = 0; i < digitos.length(); i++) {
			int aux = Integer.parseInt(digitos.charAt(i) + "");
			suma += aux;
		}

		if (suma > sumaMaxima)
			return true;

		return false;
	}

	public static LocalDate obtenerFechaLuegoDe(LocalDate fechaInicial, long numeroDias) {
		if (fechaInicial.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
			fechaInicial = fechaInicial.plusDays(1);
		}
		if (numeroDias > 6) {
			// Calculamos el numero de domingos que trancurriran en el numero de dias.
			long domingos = numeroDias / 7;
			// Calculamos el numero de dias para que se cumplan esos domingos.
			long diasPorDomingos = domingos * 7;
			/**
			 * Calculamos los dias restantes, serian el numero de dias mas los domingos ya
			 * que estos no se cuentan Restamos los dias recorridos y restamos uno para
			 * incluir el dia inicial.
			 */
			long diasRestantes = numeroDias + domingos - diasPorDomingos - 1;
			// Nos vamos a la fecha de trancurridos los domingos.
			LocalDate fechaFinal = fechaInicial.plusDays(diasPorDomingos);
			/**
			 * Calculamos el numero de dias restantes para el proximo domingo Si el el valor
			 * de proximoDomingoEn es menor o igual que los dias restantes sumamos 1 domingo
			 * mas
			 */
			long proximoDomingoEn = ChronoUnit.DAYS.between(fechaFinal,
					fechaFinal.with(TemporalAdjusters.next(DayOfWeek.SUNDAY)));
			if (proximoDomingoEn <= diasRestantes)
				diasRestantes += 1;
			// Sumamos los dias restantes a la fecha para obtener la fecha definitiva.
			fechaFinal = fechaFinal.plusDays(diasRestantes);
			// Si la fecha final es un domingo, se debe ir a la fecha siguiente.
			if (fechaFinal.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
				fechaFinal = fechaFinal.plusDays(1);
			}
			return fechaFinal;
		} else {
			/**
			 * En caso de que sean menos de 7 dias
			 * se resta un dia porque la fecha inicial cuenta
			 */
			numeroDias -=1;
			/**
			 * Calculamos el numero de dias restantes para el proximo domingo. Si el el valor
			 * de proximoDomingoEn es menor o igual al numero de dias sumamos 1.
			 */
			long proximoDomingoEn = ChronoUnit.DAYS.between(fechaInicial,
					fechaInicial.with(TemporalAdjusters.next(DayOfWeek.SUNDAY)));
			if (proximoDomingoEn <= numeroDias)
				numeroDias += 1;
			LocalDate fechaFinal = fechaInicial.plusDays(numeroDias);
			//Si la fecha final es un domingo, se debe ir a la fecha siguiente.
			if (fechaFinal.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
				fechaFinal = fechaFinal.plusDays(1);
			}

			return fechaFinal;
		}
	}

	public static Date obtenerFechaLuegoDe(Date fechaInicial, long numeroDias) {
		LocalDate fechaInicialAux = fechaInicial.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate fechaFinal = obtenerFechaLuegoDe(fechaInicialAux, numeroDias);
		return java.sql.Date.valueOf(fechaFinal);
	}

}
