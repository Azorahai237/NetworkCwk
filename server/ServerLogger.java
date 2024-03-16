import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerLogger {

    public static void logRequest(String ipAddress, String request) {
        // Get the current date and time
        String dateTime = getCurrentDateTime();

        // Format the log entry
        String logEntry = dateTime + "|" + ipAddress + "|" + request;

        // Write the log entry to the log file
        try (FileWriter fileWriter = new FileWriter("log.txt", true); // Overwrite mode
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println(logEntry);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the error (e.g., log it or throw an exception)
        }
    }

    private static String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd|HH:mm:ss");
        return dateFormat.format(new Date());
    }
}
