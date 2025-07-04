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

public class GetAndPutSlide {

    public static void main(String[] args) throws IOException, InterruptedException {

        String p = args[0];
        String labId = args[1];
        String slideId = args[2];
        String partName = args[3];
        String partDescr = args[4];

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

        Slide slide = null;
        {
            HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://prdsectuv.eushc.org/SectraPathologyImport/lisdata/v1/slides/" + slideId
                    + (labId != null && labId.length() > 0 && "EmoryQC".equals(labId) ? "?labId=" + labId : "")))
                .header("Accept", "application/json")
                .build();

            System.out.println("GET " + request.uri());
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.statusCode());
            if(response.statusCode() == 200) {
                slide = gson.fromJson(response.body(), Slide.class);
                System.out.println(gson.toJson(slide));
            }
            else {
                System.out.println(response.body());
            }
        }

        //slide.spec.name = partName;
        //slide.specimenBox.description = partDescr;
        slide.block.description = "[" + partName + "] " + partDescr;
        
        {
            HttpRequest request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(slide)))
                .uri(URI.create("https://prdsectuv.eushc.org/SectraPathologyImport/lisdata/v1/slides/" + slideId
                    + (labId != null && labId.length() > 0 && "EmoryQC".equals(labId) ? "?labId=" + labId : "")))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .build();

            System.out.println("PUT " + request.uri());
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.statusCode());
            if(response.statusCode() == 201) {
                slide = gson.fromJson(response.body(), Slide.class);
                System.out.println(gson.toJson(slide));
            }
            else {
                System.out.println(response.body());
            }
        }
        
    }
    
}