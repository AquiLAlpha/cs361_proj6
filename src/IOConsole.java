/*
File: IOConsole.java
CS361 Project 5
Names: Yi Feng, Matt Jones, Danqing Zhao
Date: 10/12/18
 */

package proj5JonesFengZhao;

import javafx.scene.input.KeyEvent;
import org.fxmisc.richtext.StyleClassedTextArea;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;

/**
 * This IOConsole class extends the StyleCalssedTextArea.
 * It contains a String userInput to store the user's input,
 * and an outputStream for writing the user input to a process.
 * It can read from an inputstream and write to an outputstream.
 *
 * @author Yi Feng
 * @author Matt Jones
 * @author Danqing Zhao
 */
public class IOConsole extends StyleClassedTextArea {
    private String userInput;
    private OutputStream outputStream;


    /**
     * Constructor
     * When a key is typed, call handleKeyPress method
     */
    IOConsole() {
        this.userInput = "";
        this.setOnKeyTyped(event -> handleKeyPress(event));
    }

    /**
     * set the console's output stream to the input OutpusStream
     * @param outputStream
     */
    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    /**
     * Read from the inputStream of process and write to the styleClassedTextArea
     *
     * @param input inputStream got from the process
     */
    public void readFrom(InputStream input) {
        Thread readFrom = new Thread(new ReadFromProcess(input, this));
        readFrom.start();
    }

    /**
     * write the user input the
     * if failed, print out the exception message in terminal
     */
    public void writeTo() {
        try {
            OutputStreamWriter writer = new OutputStreamWriter(this.outputStream);
            writer.write(this.userInput);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * get the user's input to the userInput field
     * if the user presses ENTER, write the input to the process
     * @param keyEvent the key(s) that user typed
     */
    private void handleKeyPress(KeyEvent keyEvent) {
        this.userInput += keyEvent.getCharacter();
        if (keyEvent.getCharacter().equals("\r")) {
            this.userInput += "\n";
            this.writeTo();
            this.userInput = "";
        }
    }
}
