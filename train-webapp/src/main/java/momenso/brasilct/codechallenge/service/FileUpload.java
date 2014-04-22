package momenso.brasilct.codechallenge.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

@Path("/files")
public class FileUpload {

	public static final String SERVER_UPLOAD_LOCATION_FOLDER = "data/";

	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public String uploadMapFiles(
			@FormDataParam("lines_file") InputStream linesFileInputStream,
			@FormDataParam("lines_file") FormDataContentDisposition linesContentDispositionHeader,
			@FormDataParam("routes_file") InputStream routesFileInputStream,
			@FormDataParam("routes_file") FormDataContentDisposition routesContentDispositionHeader,
			@FormDataParam("stations_file") InputStream stationsFileInputStream,
			@FormDataParam("stations_file") FormDataContentDisposition stationsContentDispositionHeader) {

		String linesFilePath = SERVER_UPLOAD_LOCATION_FOLDER + linesContentDispositionHeader.getFileName();
		saveFile(linesFileInputStream, linesFilePath);
		String routesFilePath = SERVER_UPLOAD_LOCATION_FOLDER + routesContentDispositionHeader.getFileName();
		saveFile(routesFileInputStream, routesFilePath);
		String stationsFilePath = SERVER_UPLOAD_LOCATION_FOLDER + stationsContentDispositionHeader.getFileName();
		saveFile(stationsFileInputStream, stationsFilePath);
		
		return "Map files saved";
	}

	// save uploaded file to a defined location on the server
	private void saveFile(InputStream uploadedInputStream, String serverLocation) {

		try {
			OutputStream outpuStream = new FileOutputStream(new File(serverLocation));
			int read = 0;
			byte[] bytes = new byte[1024];

			outpuStream = new FileOutputStream(new File(serverLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				outpuStream.write(bytes, 0, read);
			}
			outpuStream.flush();
			outpuStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
