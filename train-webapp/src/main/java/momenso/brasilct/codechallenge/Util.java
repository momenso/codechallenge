package momenso.brasilct.codechallenge;

import java.io.File;

/***
 * Utility methods
 * 
 * @author momenso
 *
 */
public abstract class Util {

	/***
	 * Remove a file or directory recursively.
	 * 
	 * @param file
	 */
	public static void deleteFileOrDirectory(final File file) {
	    if (file.exists()) {
	        if (file.isDirectory()) {
	            for (File child : file.listFiles()) {
	                deleteFileOrDirectory(child);
	            }
	        }
	        file.delete();
	    }
	}
}
