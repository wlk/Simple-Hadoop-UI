package zpi.hadoopModel;

public class QueueUtil {

	public static String getStatus(final String status) {
		String resultStatus = "Not started";
		if (status != null) {
			if (status.equals("-1")) {
				resultStatus = "Failed";
			} else if (status.equals("0")) {
				resultStatus = "Running";
			} else if (status.equals("1")) {
				resultStatus = "Succeed";
			}
		}
		return resultStatus;
	}
}
