import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class WordServer {

    private static ArrayList<String> wordList = new ArrayList<>();
    private static final int PORT = 3533;

    public static void main(String[] args) throws Exception {
        loadWords("words.txt");
        System.out.println("Loaded " + wordList.size() + " words");

        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server running on port " + PORT);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket.getInetAddress());
            handleClient(clientSocket);
        }
    }

    private static void loadWords(String filename) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = br.readLine()) != null) {
            wordList.add(line.trim());
        }
        br.close();
    }

    private static void handleClient(Socket socket) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        String input;
        while ((input = in.readLine()) != null) {
            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Client disconnected");
                break;
            }
            String result = findMatches(input);
            out.println(result);
        }
        socket.close();
    }

    // grab words matching the prefix
    private static String findMatches(String prefix) {
        ArrayList<String> matches = new ArrayList<>();
        String lower = prefix.toLowerCase();

        for (String word : wordList) {
            if (word.toLowerCase().startsWith(lower) && !word.equalsIgnoreCase(prefix)) {
                matches.add(word);
            }
        }

        if (matches.isEmpty())
            return "NO_MATCHES";

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < matches.size(); i++) {
            sb.append(matches.get(i));
            if (i < matches.size() - 1)
                sb.append(", ");
        }
        return sb.toString();
    }
}
