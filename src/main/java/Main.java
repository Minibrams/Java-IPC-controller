import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        IPCController ipc = new IPCController("IPCDemo.py", "python", "quit");

        String[] message = new String[] {
                "please",
                "make",
                "this",
                "upper",
                "case",
                "quit"
        };

        try {

            ipc.start();

            System.out.println("Sending message from Java process:");
            for (String line : message)
                System.out.println(line);

            String[] returned = ipc.pipeMessage(message);
            System.out.println("Received response from Python process:");
            for (String line : returned)
                System.out.println(line);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
