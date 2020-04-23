package dominio.integracion;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import dominio.Bibliotecario;
import dominio.Libro;
import dominio.Prestamo;
import dominio.excepcion.PrestamoException;
import dominio.repositorio.RepositorioLibro;
import dominio.repositorio.RepositorioPrestamo;
import persistencia.sistema.SistemaDePersistencia;
import testdatabuilder.LibroTestDataBuilder;

public class BibliotecarioTest {

	private static final String CRONICA_DE_UNA_MUERTA_ANUNCIADA = "Cronica de una muerta anunciada";
	private static final String NOMBRE_USURIO = "Sair Martinez";
	private static final String ISBN_INEXISTENTE = "123456789";
	private static final String ISBN_PALINDROMO = "1221";
	private static final String ISBN_SUMA_MAYOR_30 = "A1b2c3d4e5f6gg789";

	private SistemaDePersistencia sistemaPersistencia;

	private RepositorioLibro repositorioLibros;
	private RepositorioPrestamo repositorioPrestamo;

	@Before
	public void setUp() {

		sistemaPersistencia = new SistemaDePersistencia();

		repositorioLibros = sistemaPersistencia.obtenerRepositorioLibros();
		repositorioPrestamo = sistemaPersistencia.obtenerRepositorioPrestamos();

		sistemaPersistencia.iniciar();
	}

	@After
	public void tearDown() {
		sistemaPersistencia.terminar();
	}

	@Test
	public void prestarLibroTest() {

		// arrange
		Libro libro = new LibroTestDataBuilder().conTitulo(CRONICA_DE_UNA_MUERTA_ANUNCIADA).build();
		repositorioLibros.agregar(libro);
		Bibliotecario blibliotecario = new Bibliotecario(repositorioLibros, repositorioPrestamo);

		// act
		blibliotecario.prestar(libro.getIsbn(), NOMBRE_USURIO);
		Libro libroPrestado = repositorioPrestamo.obtenerLibroPrestadoPorIsbn(libro.getIsbn());
		Prestamo prestamo = repositorioPrestamo.obtener(libro.getIsbn());

		// assert
		Assert.assertTrue(blibliotecario.esPrestado(libro.getIsbn()));
		Assert.assertNotNull(libroPrestado);
		Assert.assertNotNull(prestamo.getFechaSolicitud());
		Assert.assertEquals(NOMBRE_USURIO, prestamo.getNombreUsuario());
		Assert.assertNull(prestamo.getFechaEntregaMaxima());
	}

	@Test
	public void prestarLibroNoDisponibleTest() {

		// arrange
		Libro libro = new LibroTestDataBuilder().conTitulo(CRONICA_DE_UNA_MUERTA_ANUNCIADA).build();

		repositorioLibros.agregar(libro);

		Bibliotecario blibliotecario = new Bibliotecario(repositorioLibros, repositorioPrestamo);

		// act
		blibliotecario.prestar(libro.getIsbn(), NOMBRE_USURIO);
		try {

			blibliotecario.prestar(libro.getIsbn(), NOMBRE_USURIO);
			fail();

		} catch (PrestamoException e) {
			// assert
			Assert.assertEquals(Bibliotecario.EL_LIBRO_NO_SE_ENCUENTRA_DISPONIBLE, e.getMessage());
		}
	}

	@Test
	public void prestarLibroConISBNPalindromoTest() {
		// arrange
		Libro libro = new LibroTestDataBuilder().conIsbn(ISBN_PALINDROMO).build();

		repositorioLibros.agregar(libro);

		Bibliotecario blibliotecario = new Bibliotecario(repositorioLibros, repositorioPrestamo);
		// act
		try {

			blibliotecario.prestar(libro.getIsbn(), NOMBRE_USURIO);
			fail();

		} catch (PrestamoException e) {
			// assert
			Assert.assertEquals(Bibliotecario.LIBRO_SOLO_DISPONIBLE_EN_BIBLIOTECA, e.getMessage());
			Assert.assertFalse(blibliotecario.esPrestado(libro.getIsbn()));
		}
	}
	
	@Test
	public void prestarLibroSumaISBNMayorDe30Test() {
		// arrange
		Libro libro = new LibroTestDataBuilder().conIsbn(ISBN_SUMA_MAYOR_30).build();
		repositorioLibros.agregar(libro);
		Bibliotecario blibliotecario = new Bibliotecario(repositorioLibros, repositorioPrestamo);

		// act
		blibliotecario.prestar(libro.getIsbn(), NOMBRE_USURIO);
		Libro libroPrestado = repositorioPrestamo.obtenerLibroPrestadoPorIsbn(libro.getIsbn());
		Prestamo prestamo = repositorioPrestamo.obtener(libro.getIsbn());

		// assert
		Assert.assertTrue(blibliotecario.esPrestado(libro.getIsbn()));
		Assert.assertNotNull(libroPrestado);
		Assert.assertNotNull(prestamo.getFechaSolicitud());
		Assert.assertEquals(NOMBRE_USURIO, prestamo.getNombreUsuario());
		Assert.assertNotNull(prestamo.getFechaEntregaMaxima());
	}

	@Test
	public void prestarLibroISBNInvalidoTest() {
		// arrange
		Bibliotecario blibliotecario = new Bibliotecario(repositorioLibros, repositorioPrestamo);
		// act
		try {
			blibliotecario.prestar(null, NOMBRE_USURIO);
			fail();

		} catch (PrestamoException e) {
			// assert
			Assert.assertEquals(Bibliotecario.ISBN_INVALIDO, e.getMessage());
		}
	}

	@Test
	public void prestarLibroNoExistenteTest() {
		// Arrange
		Bibliotecario blibliotecario = new Bibliotecario(repositorioLibros, repositorioPrestamo);
		// act
		try {
			blibliotecario.prestar(ISBN_INEXISTENTE, NOMBRE_USURIO);
			fail();

		} catch (PrestamoException e) {
			// assert
			Assert.assertEquals(Bibliotecario.LIBRO_NO_EXISTE, e.getMessage());
		}
	}

}
