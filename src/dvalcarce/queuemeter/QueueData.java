package dvalcarce.queuemeter;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.util.Log;

enum QueueType {
	ARRIVAL, SERVICE, DEPARTURE;
}

public class QueueData {

	public final static int maxQueues = 4;
	private static int queueNumber;
	private static QueueData instance = null;
	private List<FileWriter> arrivalFiles, serviceFiles, departureFiles;

	private QueueData(int queueNumber) {
		arrivalFiles = new ArrayList<FileWriter>();
		serviceFiles = new ArrayList<FileWriter>();
		departureFiles = new ArrayList<FileWriter>();
		File arrivalFile, serviceFile, departureFile;
		FileWriter arrivalStream, serviceStream, departureStream;
		QueueData.queueNumber = queueNumber;
		try {
			String now = String.valueOf(new Date().getTime());
			File root = QueueMeterActivity.getAppContext().getExternalFilesDir(
					now);

			Log.d("QueueData", "Creating Quedata(" + queueNumber + ") at "
					+ root.getPath());

			for (int i = 0; i < queueNumber; i++) {
				arrivalFile = new File(root, "arrival " + i + ".txt");
				serviceFile = new File(root, "service " + i + ".txt");
				departureFile = new File(root, "departure " + i + ".txt");

				arrivalStream = new FileWriter(arrivalFile, true);
				serviceStream = new FileWriter(serviceFile, true);
				departureStream = new FileWriter(departureFile, true);

				arrivalFiles.add(arrivalStream);
				serviceFiles.add(serviceStream);
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

	private static void safeClose(List<FileWriter> files) {
		Context activity = QueueMeterActivity.getAppContext();

		for (FileWriter file : files) {
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
	}

	public static void deleteInstances() {
		if (instance == null) {
			return;
		}
		safeClose(instance.arrivalFiles);
		safeClose(instance.serviceFiles);
		safeClose(instance.departureFiles);

		instance = null;
	}

	public static QueueData getInstance() {
		return instance;
	}

	public int getNumberInstances() {
		return QueueData.queueNumber;
	}

	public void notice(QueueType type, int i, Date date) {
		FileWriter file;
		switch (type) {
		case ARRIVAL:
			file = arrivalFiles.get(i);
			break;
		case SERVICE:
			file = serviceFiles.get(i);
			break;
		case DEPARTURE:
			file = departureFiles.get(i);
			break;
		default:
			throw new IllegalArgumentException("Bad QueueType");
		}

		try {
			file.write((date.getTime() + "\n"));
			file.flush();
		} catch (Exception e) {
			Log.d("QueueData",
					"Exeception " + type + " at " + i + e.getMessage());
		}
	}

	public void arrival(int i, Date date) {
		FileWriter file = arrivalFiles.get(i);
		try {
			file.write((date.getTime() + "\n"));
			file.flush();
		} catch (Exception e) {
			Log.d("QueueData", "Exeception Arrival at " + i + e.getMessage());
		}
	}

	public void service(int i, Date date) {
		FileWriter file = serviceFiles.get(i);
		try {

			file.write((date.getTime() + "\n"));
			file.flush();
		} catch (Exception e) {
			Log.d("QueueData", "Exception Service at " + i + e.getMessage());
		}
	}

	public void departure(int i, Date date) {
		FileWriter file = departureFiles.get(i);
		try {

			file.write((date.getTime() + "\n"));
			file.flush();
		} catch (Exception e) {
			Log.d("QueueData", "Exception Departure at " + i + e.getMessage());
		}
	}

}
