package dvalcarce.queuemeter;

import java.util.Date;

public class QueueData {

	private final static int maxQueues = 3;
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
		// TODO Auto-generated method stub

	}

	public void departure(int i, Date date) {
		// TODO Auto-generated method stub

	}

}
