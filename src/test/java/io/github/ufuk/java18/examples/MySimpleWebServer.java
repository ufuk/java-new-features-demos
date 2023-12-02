package io.github.ufuk.java18.examples;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.SimpleFileServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Paths;

public class MySimpleWebServer {

    public static void main(String[] args) throws IOException {
        MySimpleWebServer.initializeServer(8080);
    }

    public static void initializeServer(int port) throws IOException {
        var simpleFileServerPath = Paths.get("src/test/resources/simple-file-server/").toAbsolutePath();

        var handler = SimpleFileServer.createFileHandler(simpleFileServerPath);

        var server = HttpServer.create(new InetSocketAddress(port), 10, "/", handler);

        server.start();
    }

}
