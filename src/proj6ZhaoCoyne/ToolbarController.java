/*
File: ToolbarController.java
CS361 Project 5
Names: Yi Feng, Matt Jones, Danqing Zhao
Date: 10/12/18
 */

package proj6ZhaoCoyne;

import javafx.scene.control.Button;
import java.io.File;
import java.io.IOException;
import java.sql.SQLOutput;


/**
 * This class is the controller for all of the toolbar functionalities.
 * This class handles the actions of the Compile Button,
 * the Compile & Run Button, and the Stop Button.
 *
 * @author Kevin Ahn
 * @author Jackie Hang
 * @author Matt Jones
 * @author Kevin Zhou
 * @author Yi Feng
 * @author Danqing Zhao
 * @version 1.0
 * @since 10-3-2018
 */
public class ToolbarController {
    private WorkingProcess workingProcess;
    private Process process;

    /**
     * compile the current file in a new thread
     * @param curFile the current file
     * @param console
     * @param stopButton
     * @throws InterruptedException
     */
    public void handleCompile(File curFile, IOConsole console, Button stopButton) throws  InterruptedException{

        // enable the stop button
        stopButton.setDisable(false);

        // create a thread to compile the curFile
        workingProcess = new WorkingProcess(curFile, console, stopButton, false);
        Thread compileThread = new Thread(workingProcess);
        compileThread.start();
    }
    /**
     * compile and run the curFile in a new thread
     * @param curFile
     * @param console
     * @param stopButton
     * @throws InterruptedException
     */
    public void handleCompileRun(File curFile, IOConsole console, Button stopButton) throws  InterruptedException{

        // enable the stop button
        stopButton.setDisable(false);

        //create a thread to compile and run the current file
        workingProcess = new WorkingProcess(curFile, console, stopButton, true);
        Thread runThread = new Thread(workingProcess);
        runThread.start();
    }

    /**
     * Will stop any compilation or running processes.
     *
     */
    public void handleStop(IOConsole console) {
        if (this.workingProcess!=null) {
            this.process = this.workingProcess.getProcess();
            if (this.process != null) {

                this.process.destroyForcibly();

            }
        }
    }
}
