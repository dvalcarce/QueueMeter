package dvalcarce.queuemeter;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

public class QueueData {

	public final static int maxQueues = 5;
	private static int queueNumber;
	private static QueueData instance = null;
	private List<OutputStreamWriter> inFiles;
	private List<OutputStreamWriter> outFiles;

	@SuppressLint("WorldReadableFiles")
	private QueueData(int queueNumber) {
		QueueData.queueNumber = queueNumber;
		for (int i = 0; i < queueNumber; i++) {
			try {
				FileOutputStream fOut = QueueMeterActivity.getAppContext()
						.openFileOutput("samplefile.txt", Context.MODE_PRIVATE);
				OutputStreamWriter inOsw = new OutputStreamWriter(fOut);
				OutputStreamWriter outOsw = new OutputStreamWriter(fOut);
				inFiles = new ArrayList<OutputStreamWriter>();
				outFiles = new ArrayList<OutputStreamWriter>();
				inFiles.add(inOsw);
				outFiles.add(outOsw);

			} catch (Exception e) {
			}
		}
	}

	public static boolean createInstances(int queueNumber) {
		if (queueNumber > maxQueues) {
			return false;
		}
		if (instance == null) {
			return false;
		}
		instance = new QueueData(queueNumber);
		return true;
	}

	public static void deleteInstances() {

		for (OutputStreamWriter file : instance.inFiles) {
			try {
				file.flush();
				file.close();
			} catch (Exception e) {
			}
		}
		for (OutputStreamWriter file : instance.outFiles) {
			try {
				file.flush();
				file.close();
			} catch (Exception e) {
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
		OutputStreamWriter osw = inFiles.get(i);
		try {
			osw.write("Arrival " + i);
			osw.flush();
		} catch (Exception e) {
			Toast.makeText(QueueMeterActivity.getAppContext(),
					"Error: Arrival at " + (i + 1), Toast.LENGTH_SHORT).show();
		}
	}

	public void departure(int i, Date date) {
		Toast.makeText(QueueMeterActivity.getAppContext(),
				"Departure at " + (i + 1), Toast.LENGTH_SHORT).show();
		OutputStreamWriter osw = outFiles.get(i);
		try {
			osw.write("Departure " + i);
			osw.flush();
		} catch (Exception e) {
			Toast.makeText(QueueMeterActivity.getAppContext(),
					"Error: Departure at " + (i + 1), Toast.LENGTH_SHORT)
					.show();
		}
	}

}
