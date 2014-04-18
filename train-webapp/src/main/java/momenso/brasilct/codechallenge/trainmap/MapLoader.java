package momenso.brasilct.codechallenge.trainmap;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import momenso.brasilct.codechallenge.FileUpload;

import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

/***
 * Loads map files uploaded to the server and parse them to provide usable data
 * to the application.
 * 
 * @author momenso
 * 
 */
public class MapLoader {
	private List<Line> lines;
	private List<Route> routes;
	private List<Station> stations;

	private static MapLoader instance = null;

	public static MapLoader getInstance() {
		if (instance == null) {
			instance = new MapLoader();
		}

		return instance;
	}

	private MapLoader() {
		try {
			loadLines();
			loadRoutes();
			loadStations();
		} catch (Exception e) {
			throw new RuntimeException("Failed to load map.", e);
		}
	}

	private static CellProcessor[] getRouteProcessors() {

		final CellProcessor[] processors = new CellProcessor[] {
				new NotNull(new ParseInt()), // Line
				new NotNull(), // Name
				new NotNull(), // Colour
				new NotNull() // Stripe
		};
		return processors;
	}

	private static CellProcessor[] getLineProcessors() {

		final CellProcessor[] processors = new CellProcessor[] {
				new NotNull(new ParseInt()), // Station1
				new NotNull(new ParseInt()), // Station2
				new NotNull(new ParseInt()) // Line
		};
		return processors;
	}

	private static CellProcessor[] getStationProcessors() {

		final CellProcessor[] processors = new CellProcessor[] {
				new NotNull(new ParseInt()), // ID
				new NotNull(new ParseDouble()), // Latitude
				new NotNull(new ParseDouble()), // Longitude
				new NotNull(), // Name
				new NotNull(), // Display Name
				new NotNull(new ParseDouble()), // Zone
				new NotNull(new ParseInt()), // Total Lines
				new NotNull(new ParseInt()) // Rail
		};
		return processors;
	}

	private void loadStations() throws Exception {
		ICsvBeanReader beanReader = null;
		try {
			beanReader = new CsvBeanReader(new FileReader(
					FileUpload.SERVER_UPLOAD_LOCATION_FOLDER + "stations.csv"),
					CsvPreference.STANDARD_PREFERENCE);

			final String[] header = beanReader.getHeader(true);
			final CellProcessor[] processors = getStationProcessors();

			Station station;
			stations = new ArrayList<Station>();
			while ((station = beanReader
					.read(Station.class, header, processors)) != null) {
				stations.add(station);
			}
		} finally {
			if (beanReader != null) {
				beanReader.close();
			}
		}
	}

	private void loadRoutes() throws Exception {
		ICsvBeanReader beanReader = null;
		try {
			beanReader = new CsvBeanReader(new FileReader(
					FileUpload.SERVER_UPLOAD_LOCATION_FOLDER + "routes.csv"),
					CsvPreference.STANDARD_PREFERENCE);

			final String[] header = beanReader.getHeader(true);
			final CellProcessor[] processors = getRouteProcessors();

			Route route;
			routes = new ArrayList<Route>();
			while ((route = beanReader.read(Route.class, header, processors)) != null) {
				routes.add(route);
			}
		} finally {
			if (beanReader != null) {
				beanReader.close();
			}
		}
	}

	private void loadLines() throws Exception {
		ICsvBeanReader beanReader = null;
		try {
			beanReader = new CsvBeanReader(new FileReader(
					FileUpload.SERVER_UPLOAD_LOCATION_FOLDER + "lines.csv"),
					CsvPreference.STANDARD_PREFERENCE);

			final String[] header = beanReader.getHeader(true);
			final CellProcessor[] processors = getLineProcessors();

			Line line;
			lines = new ArrayList<Line>();
			while ((line = beanReader.read(Line.class, header, processors)) != null) {
				lines.add(line);
			}
		} finally {
			if (beanReader != null) {
				beanReader.close();
			}
		}
	}

	public List<Line> getLines() {
		return lines;
	}

	public List<Route> getRoutes() {
		return routes;
	}

	public List<Station> getStations() {
		return stations;
	}
}
