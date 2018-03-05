import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        IPCController ipc = new IPCController("IPCDemo.py", "python", "quit");

        ArrayList<String> message = new ArrayList<String>(Arrays.asList(
                "please",
                "make",
                "this",
                "upper",
                "case",
                "quit"
        ));

        try {

            ipc.start();

            System.out.println("Sending message from Java process:");
            for (String line : message)
                System.out.println(line);

            ArrayList<String> returned = ipc.pipeMessage(message);
            System.out.println("Received response from Python process:");
            for (String line : returned)
                System.out.println(line);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
