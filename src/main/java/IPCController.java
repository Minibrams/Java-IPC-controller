
import java.io.*;
import java.util.ArrayList;

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
        if (endSequence != "")
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

    /** Writes a message followed by a newline character to a process. Can only be called after start() has been called. */
    public void writeLine(String msg) throws IOException {
        write(msg + "\n");
    }

    /** Writes a message to a process. Can only be called after start() has been called. */
    public void write(String msg) throws IOException {
        if (_process == null) {
            throw new IOException("Unable to write: IPCController is not connected to a running process.");
        } else if (_writer == null) {
            throw new IOException("Unable to write: IPCController does not have an open output stream.");
        }

        _writer.write(msg);
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
    public ArrayList<String> readMessage() throws IOException {
        if (_endSequence == null) {
            throw new IllegalStateException("Cannot read message: IPCController has no end-of-message sequence defined.");
        }

        ArrayList<String> toReturn = new ArrayList();
        String current = "";

        while (current != _endSequence) {
            current = _reader.readLine();
            toReturn.add(current);
        }

        return toReturn;
    }

    public void setEndOfMessageSequence(String seq) {
        if (seq != "")
            _endSequence = seq;
        else
            throw new IllegalArgumentException("End-of-message sequence cannot be the empty string.");
    }

    
}
