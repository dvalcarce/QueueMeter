package dvalcarce.queuemeter;

import java.util.Date;

import android.widget.Toast;

public class QueueData {

	public final static int maxQueues = 5;
	private static int queueNumber;
	private static QueueData instance = null;

	private QueueData(int queueNumber) {
		QueueData.queueNumber = queueNumber;
		for (int i = 0; i < queueNumber; i++) {

		}
	}

	public static boolean createInstances(int queueNumber) {
		if (queueNumber > maxQueues) {
			return false;
		}
		instance = new QueueData(queueNumber);
		return true;
	}

	public static QueueData getInstance() {
		return instance;
	}

	public int getNumberInstances() {
		return QueueData.queueNumber;
	}

	public void arrival(int i, Date date) {
		Toast.makeText(QueueMeterActivity.getAppContext(), "Arrival at " + (i+1),
				Toast.LENGTH_SHORT).show();
	}

	public void departure(int i, Date date) {
		Toast.makeText(QueueMeterActivity.getAppContext(), "Departure at " + (i+1),
				Toast.LENGTH_SHORT).show();
	}

}
