package dvalcarce.queuemeter;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.util.Log;
import android.widget.Toast;

public class QueueData {

	public final static int maxQueues = 4;
	private static int queueNumber;
	private static QueueData instance = null;
	private List<FileWriter> arrivalFiles;
	private List<FileWriter> departureFiles;

	private QueueData(int queueNumber) {
		arrivalFiles = new ArrayList<FileWriter>();
		departureFiles = new ArrayList<FileWriter>();
		File arrivalFile, departureFile;
		FileWriter arrivalStream, departureStream;
		QueueData.queueNumber = queueNumber;
		try {
			String now = String.valueOf(new Date().getTime());
			File root = QueueMeterActivity.getAppContext().getExternalFilesDir(
					now);

			Log.d("QueueData", "Creating Quedata(" + queueNumber + ") at "
					+ root.getPath());

			for (int i = 0; i < queueNumber; i++) {
				arrivalFile = new File(root, "arrival " + i + ".txt");
				departureFile = new File(root, "departure " + i + ".txt");

				arrivalStream = new FileWriter(arrivalFile, true);
				departureStream = new FileWriter(departureFile, true);

				arrivalFiles.add(arrivalStream);
				departureFiles.add(departureStream);
			}
		} catch (Exception e) {
			Log.i("QueueData",
					e.getMessage() + e.toString() + e.getStackTrace());
		}
	}

	public static boolean createInstances(int queueNumber) {
		if (queueNumber > maxQueues) {
			return false;
		}
		if (instance != null) {
			return false;
		}
		instance = new QueueData(queueNumber);
		return true;
	}

	public static void deleteInstances() {
		Context activity = QueueMeterActivity.getAppContext();

		if (instance == null) {
			return;
		}
		for (FileWriter file : instance.arrivalFiles) {
			try {
				Log.i("QueueData", "Closing " + file);
				file.flush();
				file.close();

				MediaScannerConnection.scanFile(activity,
						new String[] { file.toString() }, null, null);

			} catch (Exception e) {
				Log.i("QueueData Exception",
						e.getMessage() + e.toString() + e.getStackTrace());
			}
		}
		for (FileWriter file : instance.departureFiles) {
			try {
				Log.i("QueueData", "Closing " + file);
				file.flush();
				file.close();
				MediaScannerConnection.scanFile(activity,
						new String[] { file.toString() }, null, null);
			} catch (Exception e) {
				Log.i("QueueData Exception",
						e.getMessage() + e.toString() + e.getStackTrace());
			}
		}
		instance = null;
	}

	public static QueueData getInstance() {
		return instance;
	}

	public int getNumberInstances() {
		return QueueData.queueNumber;
	}

	public void arrival(int i, Date date) {
		Toast.makeText(QueueMeterActivity.getAppContext(),
				"Arrival at " + (i + 1), Toast.LENGTH_SHORT).show();
		FileWriter file = arrivalFiles.get(i);
		try {
			file.write((date.getTime() + "\n"));
			file.flush();
		} catch (Exception e) {
			Log.d("QueueData", "Exeception Arrival at " + i + e.getMessage());
		}
	}

	public void departure(int i, Date date) {
		Toast.makeText(QueueMeterActivity.getAppContext(),
				"Departure at " + (i + 1), Toast.LENGTH_SHORT).show();
		FileWriter file = departureFiles.get(i);
		try {

			file.write((date.getTime() + "\n"));
			file.flush();
		} catch (Exception e) {
			Log.d("QueueData", "Exception Departure at " + i + e.getMessage());
		}
	}

}
