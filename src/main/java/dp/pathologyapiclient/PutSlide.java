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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PutSlide {

    public static void main(String[] args) throws IOException, InterruptedException {

        String p = args[0];
        String labId = args[1];
        String slideId = args[2];
        String stain = args[3];
       
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        
        Pattern pat = Pattern.compile("^(.*)-(.*)-(.*)-(.*)$");
        Matcher m = pat.matcher(slideId);
        if(!m.matches()) { throw new RuntimeException("bad slideId format"); }
        
        Slide newSlide = new Slide();
        newSlide.requestId = m.group(1) + "-" + m.group(2);
        newSlide.block = new Slide.Block();
        newSlide.block.name = m.group(3);
        newSlide.staining = new Slide.Staining();
        newSlide.staining.name = m.group(4) + "-" + stain;
        
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
            .PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(newSlide)))
            .uri(URI.create("https://tst-sect-eapp1.eushc.org/SectraPathologyImport/lisdata/v1/slides/" + slideId
                + (labId != null && labId.length() > 0 && "EmoryQC".equals(labId) ? "?labId=" + labId : "")))
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .build();

        System.out.println("PUT " + request.uri());
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        if(response.statusCode() == 201) {
            Slide slide = gson.fromJson(response.body(), Slide.class);
            System.out.println(gson.toJson(slide));
        }
        else {
            System.out.println(response.body());
        }
        
    }
    
}