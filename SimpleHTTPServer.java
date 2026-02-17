import java.io.*;
import java.net.*;

/**
 * SimpleHTTPServer - A standalone HTTP server for MentalBook web app
 * Works without servlet container by handling requests directly
 */
public class SimpleHTTPServer {
    
    private static final int PORT = 8080;
    private static final String WEB_ROOT = "web/src/main/webapp";
    
    // VirtualXanderCore instance for processing messages
    private static VirtualXanderCore core;
    
    static {
        try {
            core = new VirtualXanderCore();
            System.out.println("VirtualXanderCore initialized successfully");
        } catch (Exception e) {
            System.err.println("Failed to initialize VirtualXanderCore: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("MentalBook Web Server started on port " + PORT);
            System.out.println("Open http://localhost:" + PORT + " in your browser");
            System.out.println("Press Ctrl+C to stop");
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                handleRequest(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void handleRequest(Socket clientSocket) {
        try {
            BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
            
            String requestLine = in.readLine();
            if (requestLine == null) {
                clientSocket.close();
                return;
            }
            
            System.out.println("Request: " + requestLine);
            
            // Parse request
            String[] parts = requestLine.split(" ");
            String method = parts[0];
            String path = parts[1];
            
            // Handle API request (don't skip headers, let handleApiRequest read them)
            if (path.equals("/api/chat") && method.equals("POST")) {
                handleApiRequest(in, clientSocket);
                return;
            }
            
            // Handle logs API request
            if (path.equals("/api/logs") && method.equals("GET")) {
                handleLogsRequest(in, clientSocket);
                return;
            }
            
            // Handle code API request
            if (path.equals("/api/code") && method.equals("GET")) {
                handleCodeRequest(in, clientSocket);
                return;
            }

            // Skip headers for static file requests
            String line;
            while ((line = in.readLine()) != null && !line.isEmpty()) {
                // Skip
            }

            
            // Handle static files
            if (path.equals("/")) {
                path = "/index.html";
            }
            
            String filePath = WEB_ROOT + path;
            File file = new File(filePath);
            
            OutputStream out = clientSocket.getOutputStream();
            
            if (file.exists() && !file.isDirectory()) {
                // Send 200 OK
                String contentType = getContentType(path);
                String response = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: " + contentType + "\r\n" +
                    "Content-Length: " + file.length() + "\r\n" +
                    "Connection: close\r\n" +
                    "\r\n";
                
                out.write(response.getBytes());
                
                // Send file content
                FileInputStream fis = new FileInputStream(file);
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                fis.close();
            } else {
                // Send 404
                String response = "HTTP/1.1 404 Not Found\r\n" +
                    "Content-Type: text/plain\r\n" +
                    "Connection: close\r\n" +
                    "\r\n" +
                    "404 Not Found";
                out.write(response.getBytes());
            }
            
            out.flush();
            clientSocket.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            try {
                clientSocket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private static void handleApiRequest(BufferedReader in, Socket clientSocket) {
        try {
            // Read request body
            StringBuilder body = new StringBuilder();
            String line;
            int contentLength = 0;
            
            // Read headers to get content length
            while ((line = in.readLine()) != null && !line.isEmpty()) {
                if (line.toLowerCase().startsWith("content-length:")) {
                    contentLength = Integer.parseInt(line.split(":")[1].trim());
                }
            }
            
            // Read body
            if (contentLength > 0) {
                char[] buffer = new char[contentLength];
                in.read(buffer, 0, contentLength);
                body.append(buffer);
            }
            
            // Parse message
            String json = body.toString();
            String message = extractMessage(json);
            
            // Get response from VirtualXanderCore
            String botResponse = getBotResponse(message);
            
            // Send response
            OutputStream out = clientSocket.getOutputStream();
            String response = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/plain\r\n" +
                "Content-Length: " + botResponse.length() + "\r\n" +
                "Connection: close\r\n" +
                "\r\n" +
                botResponse;
            
            out.write(response.getBytes());
            out.flush();
            clientSocket.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            try {
                clientSocket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private static String extractMessage(String json) {
        try {
            int start = json.indexOf("\"message\":\"");
            if (start == -1) return "";
            start += 11;
            int end = json.indexOf("\"", start);
            if (end == -1) return "";
            return json.substring(start, end);
        } catch (Exception e) {
            return "";
        }
    }
    
    private static void handleLogsRequest(BufferedReader in, Socket clientSocket) {
        try {
            // Skip headers
            String line;
            while ((line = in.readLine()) != null && !line.isEmpty()) {
                // Skip
            }
            
            // Read log file if it exists
            StringBuilder logs = new StringBuilder();
            File logFile = new File("logs/error.log");
            if (logFile.exists()) {
                BufferedReader logReader = new BufferedReader(new FileReader(logFile));
                String logLine;
                int lineCount = 0;
                while ((logLine = logReader.readLine()) != null && lineCount < 50) {
                    logs.append(logLine).append("\n");
                    lineCount++;
                }
                logReader.close();
            } else {
                logs.append("No error logs found.");
            }
            
            String logContent = logs.toString();
            
            // Send response
            OutputStream out = clientSocket.getOutputStream();
            String response = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/plain\r\n" +
                "Content-Length: " + logContent.length() + "\r\n" +
                "Connection: close\r\n" +
                "\r\n" +
                logContent;
            
            out.write(response.getBytes());
            out.flush();
            clientSocket.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            try {
                clientSocket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private static String getBotResponse(String message) {
        // Use VirtualXanderCore for intelligent responses
        if (message == null || message.trim().isEmpty()) {
            return "I didn't catch that. Could you say something?";
        }
        
        // Check if core is initialized
        if (core == null) {
            return "I'm having trouble connecting to my brain right now. Please try again in a moment.";
        }
        
        try {
            // Process message through VirtualXanderCore
            String response = core.processMessage(message);
            return response;
        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
            e.printStackTrace();
            return "I encountered an error while thinking about that. Could you try rephrasing your message?";
        }
    }
    
    private static void handleCodeRequest(BufferedReader in, Socket clientSocket) {
        try {
            // Skip headers
            String line;
            while ((line = in.readLine()) != null && !line.isEmpty()) {
                // Skip
            }
            
            // Read the server code
            StringBuilder code = new StringBuilder();
            File codeFile = new File("SimpleHTTPServer.java");
            if (codeFile.exists()) {
                BufferedReader codeReader = new BufferedReader(new FileReader(codeFile));
                String codeLine;
                while ((codeLine = codeReader.readLine()) != null) {
                    code.append(codeLine).append("\n");
                }
                codeReader.close();
            } else {
                code.append("// SimpleHTTPServer.java not found");
            }
            
            String codeContent = code.toString();
            
            // Send response
            OutputStream out = clientSocket.getOutputStream();
            String response = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/plain\r\n" +
                "Content-Length: " + codeContent.length() + "\r\n" +
                "Connection: close\r\n" +
                "\r\n" +
                codeContent;
            
            out.write(response.getBytes());
            out.flush();
            clientSocket.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            try {
                clientSocket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private static String getContentType(String path) {
        if (path.endsWith(".html") || path.endsWith(".htm")) {
            return "text/html";
        } else if (path.endsWith(".css")) {
            return "text/css";
        } else if (path.endsWith(".js")) {
            return "application/javascript";
        } else if (path.endsWith(".json")) {
            return "application/json";
        } else if (path.endsWith(".png")) {
            return "image/png";
        } else if (path.endsWith(".jpg") || path.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (path.endsWith(".gif")) {
            return "image/gif";
        } else if (path.endsWith(".svg")) {
            return "image/svg+xml";
        } else {
            return "text/plain";
        }
    }
}
