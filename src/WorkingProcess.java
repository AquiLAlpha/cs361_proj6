/*
File: WorkingProcess.java
CS361 Project 5
Names: Yi Feng, Matt Jones, Danqing Zhao
Date: 10/12/18
 */


package proj5JonesFengZhao;

import javafx.scene.control.Button;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This class implements the Runnable class and is used to build a process that can be
 * given to a Thread and run inside that Thread.
 * Specifically, this class can build a compile process and/or a run process.
 *
 * @author Matt Jones
 * @author Yi Feng
 * @author Danqing Zhao
 * @version 1.0
 * @since 10-10-2018
 */

public class WorkingProcess implements Runnable {
    private File curFile;
    private IOConsole console;
    private Button stopButton;
    private Process process;
    private boolean ifRun;

    /**
     * Constructor
     * @param curFile the file to be compiled (and run)
     * @param console the console
     * @param stopButton the stopButton
     * @param ifRun if ifRun is false, it only compiles the file
     *              if ifRun is true, it both compiles and runs the file
     */
    WorkingProcess(File curFile, IOConsole console, Button stopButton, boolean ifRun) {
        this.curFile = curFile;
        this.console = console;
        this.ifRun = ifRun;
        this.stopButton = stopButton;
    }

    /**
     * This method will find the path of the selected file at the moment the compile button
     * was pressed and construct the commands for compiling and running the file.
     * Then it will call buildProcess to build the compilation process.
     * If ifRun is true and compilation was sucessful, call buildProcess to run the file.
     */
    public void run() {

        String path = curFile.getAbsolutePath();
        String[] command = {"javac", path};

        //compile the file
        this.process = buildProcess(console, command);


        if(ifRun){
            if (this.process!=null) {
                path = curFile.getAbsoluteFile().getParent();
                String fileName = curFile.getName();
                String[] runCommand = {"java", "-cp", path, fileName.substring(0,
                        fileName.length() - 5)};
                this.process = buildProcess(console, runCommand);
            }
        }
        // disable the stopButton when process finishes
        stopButton.setDisable(true);
    }

    /**
     * Getter of process
     * @return Process
     */
    public Process getProcess(){
        return(this.process);
    }

    /**
     * Builds a process using a ProcessBuilder. Starts the process and passes the
     * ErrorStream, InputStream and OutputStream to an IOConsole given by the console parameter.
     *
     * @param console Reference to an IOConsole that will be used for user input to the
     *                process. All output from the process will also be directed to the
     *                console
     * @param command Reference to a command that will be passed to the ProcessBuilder
     * @return returns the process if there was no exception, return null if there is
     * exception and print out the exception in terminal
     */
    public Process buildProcess(IOConsole console, String[] command) {
        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            this.process = pb.start();

            // let the console read from the process's error stream
            InputStream processErrorStream = this.process.getErrorStream();
            console.readFrom(processErrorStream);

            // let the console read from the process's output stream
            InputStream processOutput = this.process.getInputStream();
            console.readFrom(processOutput);

            // set the process's input stream to the console's output stream
            OutputStream processInput = this.process.getOutputStream();
            console.setOutputStream(processInput);

            //wait for the process to complete
            this.process.waitFor();

            return(this.process);
        } catch (Exception e) {
            e.printStackTrace();
            return(null);
        }
    }
}
