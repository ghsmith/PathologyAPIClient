package dp.pathologyapiclient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dp.pathologyapiclient.data.Slide;
import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GetSlide {

    public static void main(String[] args) throws IOException, InterruptedException {

        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        
        HttpClient client = HttpClient.newBuilder()
            .authenticator(
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("EmoryBatchInterface", args[0].toCharArray());
                    }                    
                })
            .build();

        HttpRequest request = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create("https://tst-sect-eapp1.eushc.org/SectraPathologyImport/lisdata/v1/slides/" + args[1]))
            .header("Accept", "application/json")
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Slide slide = gson.fromJson(response.body(), Slide.class);
        System.out.println(response.statusCode());
        System.out.println(gson.toJson(slide));
        
    }
    
}