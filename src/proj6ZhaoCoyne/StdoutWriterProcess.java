package proj6ZhaoCoyne;

import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class StdoutWriterProcess implements Runnable {
    private AtomicBoolean running = new AtomicBoolean(false);
    private Thread worker;
    private InputStream input;
    private IOConsole console;

    /**
     * Constructor
     * @param input
     * @param console
     */
    StdoutWriterProcess(InputStream input, IOConsole console) {
        this.input = input;
        this.console = console;
    }

    /**
     * This will find the path of the selected file at the moment the compile button
     * was pressed. The buildProcess function is then called to build a compile process
     * using the javac.
     */
    public void run() {
        running.set(true);
        try {
            byte[] buffer = new byte[1024];
            int length;
            while (running.get() && (length = input.read(buffer)) != -1) {
                String result = new String(buffer, 0, length);
                Platform.runLater(() -> console.appendText(result + "\n"));
                //BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                //String textToWrite = reader.lines().collect(Collectors.joining("\n"));
                //Platform.runLater(() -> console.appendText(textToWrite));
                //Platform.runLater(() -> console.appendText("\n"));
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        worker = new Thread(this);
        worker.start();
    }

    public void stop() {
        running.set(false);
    }
}
