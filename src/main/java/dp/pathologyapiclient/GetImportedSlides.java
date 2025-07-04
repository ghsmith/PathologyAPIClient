package dp.pathologyapiclient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GetImportedSlides {

    public static void main(String[] args) throws IOException, InterruptedException {

        String p = args[0];
        String labId = args[1];
        String slideId = args[2];

        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        
        HttpClient client = HttpClient.newBuilder()
            .authenticator(
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("EmoryBatchInterface", p.toCharArray());
                    }                    
                })
            .build();

        HttpRequest request = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create(String.format("https://prdsectuv.eushc.org/SectraPathologyImport/lisdata/v1/requests/%s/importedslides", slideId)
                + (labId != null && labId.length() > 0 && "EmoryQC".equals(labId) ? "?labId=" + labId : "")))
            .header("Accept", "application/json")
            .build();

        System.out.println("GET " + request.uri());
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        if(response.statusCode() == 200) {
            System.out.println(response.body());
            //Slide slide = gson.fromJson(response.body(), Slide.class);
            //System.out.println(gson.toJson(slide));
        }
        else {
            System.out.println(response.body());
        }
        
    }
    
}