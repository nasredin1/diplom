import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.imageio.IIOException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws Exception {
        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
        ServerSocket serverSocket = new ServerSocket(8989);

        while (true) {
            try (
                    Socket clientSocket = serverSocket.accept();
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
            ) {

                String request = in.readLine();
                if (request.equals("stop")) {
                    out.println("Сервер остановлен!");
                    break;
                }
                GsonBuilder builder = new GsonBuilder();
                builder.setPrettyPrinting();
                Gson gson = builder.create();

                out.println("[ Статистика использования слова" + request + " ] ->");

                for (PageEntry entry : engine.search(request)) {
                    out.println(gson.toJson(entry));
                }

            } catch (IIOException e) {
                System.out.println("Не могу стартовать сервер");
                e.printStackTrace();
            }
        }
        serverSocket.close();
    }
}