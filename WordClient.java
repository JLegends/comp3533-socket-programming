import java.io.*;
import java.net.*;
import java.util.Scanner;

public class WordClient {

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 3533);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        Scanner scanner = new Scanner(System.in);

        System.out.println("Connected to server");
        System.out.println("Type a word prefix to search, or \"exit\" to quit\n");

        while (true) {
            System.out.print("Enter prefix: ");
            String userInput = scanner.nextLine().trim();

            if (userInput.equalsIgnoreCase("exit")) {
                out.println("exit");
                System.out.println("Goodbye!");
                break;
            }

            out.println(userInput);
            String response = in.readLine();

            if (response.equals("NO_MATCHES")) {
                System.out.println("No words found starting with \"" + userInput + "\"\n");
            } else {
                System.out.println("The following words start with " + userInput + ": " + response + "\n");
            }
        }

        socket.close();
    }
}
