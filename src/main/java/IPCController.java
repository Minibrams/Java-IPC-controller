import java.io.*;

public class IPCController {
    private BufferedReader _reader;
    private BufferedWriter _writer;
    private String cmd;

    /** Returns an IPCController instance.
     *  @param file Name of the file (executable)
     *  @param compiler Name of the compiler/interpreter that should be used to execute the given file. */
    public IPCController(String file, String compiler) {
        String dir = System.getProperty("user.dir");
        cmd = compiler + " " + dir + "\\" + file;
    }

    /** Returns an IPCController instance.
     *  @param command The command for starting a process to which IO streams can be established. */
    public IPCController(String command) {
        cmd = command;
    }

    /** Executes the given executable and opens IO streams for IPC. */
    public void start() throws IOException {
        //Execute and grab the process
        Process proc = Runtime.getRuntime().exec(cmd);
        //Establish IO communication streams
        _reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        _writer = new BufferedWriter(new OutputStreamWriter(proc.getOutputStream()));
    }
}
