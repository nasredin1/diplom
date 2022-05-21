import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class clientSearch {


    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        String buffer;
        System.out.println("Введите слово для поиска или stop для остановки сервера");
        String word = scanner.nextLine();

        try (
                Socket clientSocket = new Socket("localhost", 8989);
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
        ) {

            out.println(word);

            while (true) {
                buffer = in.readLine();
                if (buffer == null)
                    break;
                System.out.println(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

