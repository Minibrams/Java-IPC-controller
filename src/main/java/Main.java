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
            // Start the Python process, open IO streams
            ipc.start();

            System.out.println("----------------------------------");
            System.out.println("Sending message from Java process:");
            System.out.println("----------------------------------");

            for (String line : message)
                System.out.println(line);

            // Send the message to Python process, receive the response
            String[] response = ipc.pipeMessage(message);

            System.out.println("--------------------------------------");
            System.out.println("Received response from Python process:");
            System.out.println("--------------------------------------");

            for (String line : response)
                System.out.println(line);

            // Terminate process if not already dead, close IO streams
            ipc.stop();

        } catch (IOException e) {
            System.out.println("God dammit");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
