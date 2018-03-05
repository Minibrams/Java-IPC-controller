
import java.io.*;
import java.util.ArrayList;

/** Facilitates IPC communication with another process. Assumes that messages consists of new-line-separated strings. */
public class IPCController {
    private BufferedReader _reader;
    private BufferedWriter _writer;
    private String _cmd;
    private Process _process;
    private String _endSequence;

    /** Returns an IPCController instance.
     *  @param file Name of the file (executable)
     *  @param compiler Name of the compiler/interpreter that should be used to execute the given file.
     *  @param endSequence Name of the character sequence used by both processes to signal the end of a message stream. */
    public IPCController(String file, String compiler, String endSequence) {
        String dir = System.getProperty("user.dir");
        _cmd = compiler + " " + dir + "\\" + file;
        if (!endSequence.equals(""))
            _endSequence = endSequence;
        else
            throw new IllegalArgumentException("End-of-message sequence cannot be the empty string.");
    }

    /** Returns an IPCController instance.
     *  @param file Name of the file (executable)
     *  @param compiler Name of the compiler/interpreter that should be used to execute the given file. */
    public IPCController(String file, String compiler) {
        String dir = System.getProperty("user.dir");
        _cmd = compiler + " " + dir + "\\" + file;
    }

    /** Returns an IPCController instance.
     *  @param command The command for starting a process to which IO streams can be established. */
    public IPCController(String command) {
        _cmd = command;
    }

    /** Executes the given executable and opens IO streams for IPC. */
    public void start() throws IOException {
        //Execute and grab the process
        _process = Runtime.getRuntime().exec(_cmd);
        //Establish IO communication streams
        _reader = new BufferedReader(new InputStreamReader(_process.getInputStream()));
        _writer = new BufferedWriter(new OutputStreamWriter(_process.getOutputStream()));
    }

    /** Closes IO streams and stops the running process. */
    public void stop() throws IOException {
        _reader.close();
        _writer.close();
        _process.destroy();
    }

    /** Writes a string followed by a new-line character to a process.
     *  If the input string does not end with a new-line character, one will be added before writing to the output stream.
     *  Can only be called after start() has been called. */
    public void writeLine(String msg) throws IOException {
        if (_process == null) {
            throw new IOException("Unable to write: IPCController is not connected to a running process.");
        } else if (_writer == null) {
            throw new IOException("Unable to write: IPCController does not have an open output stream.");
        }

        String message = msg.endsWith("\n") ? msg : msg + "\n";

        _writer.write(message);
        _writer.flush();
    }

    /** Writes a message to the running process. Can only be called after start() has been called.*/
    public void writeMessage(String[] msg) throws IOException {
        for (String line : msg)
            writeLine(line);
    }

    /** Reads a line from the input stream. */
    public String readLine() throws IOException {
        if (_process == null) {
            throw new IOException("Unable to read: IPCController is not connected to a running process.");
        } else if (_reader == null) {
            throw new IOException("Unable to read: IPCController does not have an open output stream.");
        }

        return _reader.readLine();
    }

    /** Reads an entire message from the input stream.
     *  Can only be called if a end-of-message sequence has been given. */
    public String[] readMessage() throws IOException {
        if (_endSequence == null) {
            throw new IllegalStateException("Cannot read message: IPCController has no end-of-message sequence defined.");
        }

        ArrayList<String> toReturn = new ArrayList();
        String current = "";

        // Read until we see the end sequence
        while (!current.equals(_endSequence)) {
            current = readLine();
            // When the input stream is closed, next line is null. Break the loop.
            if (current == null) break;
            // Otherwise, read the input
            toReturn.add(current);
        }

        String[] arr = new String[toReturn.size()];
        return toReturn.toArray(arr);
    }

    /** Defines the end-of-message sequence.
     *  Allows entire messages to be read through one call of readMessage(). */
    public void setEndOfMessageSequence(String seq) {
        if (!seq.equals(""))
            _endSequence = seq;
        else
            throw new IllegalArgumentException("End-of-message sequence cannot be the empty string.");
    }

    /** Sends a message to the running process and returns the response message. */
    public String[] pipeMessage(String[] msg) throws IOException {
        writeMessage(msg);
        return readMessage();
    }

    /** Sends a line to the running process and returns the response message. */
    public String[] pipeLine(String msg) throws IOException {
        writeLine(msg);
        return readMessage();
    }
}
