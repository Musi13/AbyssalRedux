package rs2.util.tools.account;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class BackupManager implements Runnable {

	/**
	 * Character file directory
	 */
	private static final String INPUT_DIRECTORY = "./Data/characters";

	/**
	 * Archive directory
	 */
	private static final String OUTPUT_DIRECTORY = "./backups/";

	/**
	 * Starts the backup procedure
	 */
	@Override
	public void run() {
		long startTime = System.currentTimeMillis();
		System.out.println("Starting backup service . . .");
		System.out.println("\tInput directory: " + INPUT_DIRECTORY);
		System.out.println("\tOutput directory: " + OUTPUT_DIRECTORY);
		try {
			zipFolder("./data/characters/", OUTPUT_DIRECTORY + "/Characters - "
					+ getTime() + ".zip");
			System.out.println("\tSuccessfully archived " + total + " files");
		} catch (Exception e) {
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;
		System.out.println("\tProcedure took: " + duration + "ms");
		System.out.println();
		total = 0;
	}

	/**
	 * Formats the time and date for use in filename
	 * 
	 * @return Formatted time
	 */
	private static String getTime() {
		Date getDate = new Date();
		String timeFormat = "M\u2215d\u2215yy h\u02D0mma";
		SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
		return sdf.format(getDate);
	}

	/**
	 * Prepares the folder to be archived
	 * 
	 * @param srcFolder
	 *            - Source folder to ZIP
	 * @param destZipFile
	 *            - Destination ZIP archive
	 */
	private static void zipFolder(String srcFolder, String destZipFile)
			throws Exception {
		ZipOutputStream zip = null;
		FileOutputStream fileWriter = null;

		fileWriter = new FileOutputStream(destZipFile);
		zip = new ZipOutputStream(fileWriter);

		addFolderToZip("", srcFolder, zip);
		zip.flush();
		zip.close();
	}

	/**
	 * Adds file to ZIP archive
	 * 
	 * @param path
	 *            - File directory
	 * @param srcFolder
	 *            - Source folder
	 * @param ZipOutputSream
	 *            - ZIP archive
	 */
	private static void addFileToZip(String path, String srcFile,
			ZipOutputStream zip) throws Exception {

		File folder = new File(srcFile);
		if (folder.isDirectory()) {
			addFolderToZip(path, srcFile, zip);
		} else {
			byte[] buf = new byte[1024];
			int len;
			FileInputStream in = new FileInputStream(srcFile);
			zip.putNextEntry(new ZipEntry(path + "/" + folder.getName()));
			while ((len = in.read(buf)) > 0) {
				zip.write(buf, 0, len);
			}
		}
		total++;
	}

	/**
	 * Adds folder to ZIP archive
	 * 
	 * @param path
	 *            - File directory
	 * @param srcFolder
	 *            - Source folder
	 * @param ZipOutputSream
	 *            - ZIP archive
	 */
	private static void addFolderToZip(String path, String srcFolder,
			ZipOutputStream zip) throws Exception {
		File folder = new File(srcFolder);

		for (String fileName : folder.list()) {
			if (path.equals("")) {
				addFileToZip(folder.getName(), srcFolder + "/" + fileName, zip);
			} else {
				addFileToZip(path + "/" + folder.getName(), srcFolder + "/"
						+ fileName, zip);
			}
		}
	}

	public static int total = 0;

}
