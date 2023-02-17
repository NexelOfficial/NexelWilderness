package nexel.wilderness.tools;

public class TimeConverter {
	public static String formatTime(int seconds) {
		String time = "";

		int finalhours = seconds / 3600;
		int finalminutes = (seconds - (finalhours * 3600)) / 60;
		int finalseconds = seconds - (finalminutes * 60) - (finalhours * 3600);
		if (finalhours != 0) {
			time += finalhours + " hours, ";
		}

		if (finalminutes != 0) {
			time += finalminutes + " minutes, ";
		}

		if (finalseconds != 0) {
			time += finalseconds + " seconds";
		}

		return time;
	}
}