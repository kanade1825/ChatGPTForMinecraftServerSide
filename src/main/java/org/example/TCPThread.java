package org.example;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;

public class TCPThread extends Thread {
    private Socket socket;
    private ExecutorService executorService;

    public TCPThread(Socket socket, ExecutorService executorService) {
        this.socket = socket;
        this.executorService = executorService;
        System.out.println(socket.getRemoteSocketAddress() + "と接続中");
    }

    public void run() {
        try (OutputStream output = socket.getOutputStream();
             InputStream input = socket.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            String line;
            // クライアントからの受信内容をコンソールに出力
            while ((line = reader.readLine()) != null) {
                System.out.println("クライアントから受信: " + line);
                if (line.equals("REQUEST_JSON")) {
                    sendJsonFile(output);
                    socket.close();
                    System.out.println("接続が切断されました");
                } else if (line.equals("SEND_JSON")) {
                    boolean success = false;
                    int retryCount = 0;

                    while (!success && retryCount < 5) {
                        success = receiveJsonFile(input, output);

                        if (!success) {
                            System.out.println("再送を試みます...");
                            retryCount++;
                            Thread.sleep(3000);
                        }
                    }

                    if (success) {
                        System.out.println("JSONファイルの受信に成功しました");
                    } else {
                        System.out.println("JSONファイルの受信に失敗しました");
                    }

                    socket.close();
                    System.out.println("接続が切断されました");
                }
                break;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
            }
        }
    }

    private void sendJsonFile(OutputStream output) {
        String fileName = "H:\\ChatGPTForMinecraftServerSide\\src\\main\\resources\\example.json";
        File file = new File(fileName);
        byte[] buffer = new byte[1024];
        int bytesRead;

        try (FileInputStream fis = new FileInputStream(file)) {
            while ((bytesRead = fis.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
            output.flush();
            System.out.println("クライアントにJSONファイルを送信: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean receiveJsonFile(InputStream input, OutputStream output) {
        String fileName = "H:\\ChatGPTForMinecraftServerSide\\src\\main\\resources\\received.json";
        File file = new File(fileName);
        byte[] buffer = new byte[102400000];
        int bytesRead;

        try (FileOutputStream fos = new FileOutputStream(file);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            int totalBytesRead = 0;
            while ((bytesRead = input.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;
            }

            baos.flush();
            byte[] receivedData = baos.toByteArray();

            if (totalBytesRead > 0) {
                fos.write(receivedData);
                fos.flush();
                System.out.println("クライアントからJSONファイルを受信: " + fileName);

                String jsonString = new String(receivedData);
                JSONArray jsonArray = new JSONArray(jsonString);

                // JSONArray内の各要素をデータベースに書き込むタスクをスレッドプールに投げる
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    executorService.submit(() -> {
                        DBHandler.insertJsonObject(jsonObject);
                    });

                }

                // Send success message to the client
                try {
                    output.write("SUCCESS\n".getBytes());
                    output.flush();
                } catch (SocketException se) {
                    System.err.println("SocketException occurred while sending success message to the client: " + se.getMessage());
                }
                return true;
            } else {
                System.out.println("クライアントから空のデータが受信されました");

                // Send empty data received message to the client
                try {
                    output.write("EMPTY_DATA_RECEIVED\n".getBytes());
                    output.flush();
                } catch (SocketException se) {
                    System.err.println("SocketException occurred while sending empty data received message to the client: " + se.getMessage());
                }
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }
}
