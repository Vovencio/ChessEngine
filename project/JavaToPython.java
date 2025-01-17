import java.io.*;
import java.util.Arrays;

public class JavaToPython {
    public static JavaToPython comm;
    private Process pythonProcess;
    private BufferedWriter writer;
    private BufferedReader reader;

    public void init() throws IOException {
        System.out.println("Loading python");

        // Start the Python process
        ProcessBuilder processBuilder = new ProcessBuilder("python", "training\\runner.py");
        pythonProcess = processBuilder.start();

        // Setup streams for communication
        writer = new BufferedWriter(new OutputStreamWriter(pythonProcess.getOutputStream()));
        reader = new BufferedReader(new InputStreamReader(pythonProcess.getInputStream()));

        // Wait for the "Python AI is ready" message
        String responseLine;
        while ((responseLine = reader.readLine()) != null) {
            if (responseLine.contains("Python AI is ready")) {
                System.out.println("Python is ready for communication.");
                break;
            }
        }
    }

    public double eval(int[] inputArray) throws IOException {
        // Create the JSON string
        String jsonRequest = String.format("{\"type\": \"predict\", \"data\": %s}",
                Arrays.toString(inputArray));

        // Send the request to Python
        writer.write(jsonRequest);
        writer.newLine();
        writer.flush();

        // Read the response from Python
        String responseLine = reader.readLine();
        if (responseLine != null) {
            // Parse the JSON response
            if (responseLine.contains("\"result\"")) {
                int startIndex = responseLine.indexOf("\"result\":") + 9;
                int endIndex = responseLine.indexOf("}", startIndex);
                return Double.parseDouble(responseLine.substring(startIndex, endIndex));
            } else {
                throw new IOException("Unexpected response: " + responseLine);
            }
        }
        throw new IOException("No response from Python.");
    }

    public void exit() throws IOException {
        // Send the exit command to Python
        writer.write("{\"type\": \"exit\"}");
        writer.newLine();
        writer.flush();

        // Wait for Python to terminate
        try {
            pythonProcess.waitFor();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        writer.close();
        reader.close();
    }

    public static void setUp() {
        try {
            comm = new JavaToPython();
            comm.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
