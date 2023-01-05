package eu.gaiax.difs.aas.client;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.MalformedInputException;

public class RestInvitationServiceClientImpl implements InvitationServiceClient {

    @Override
    public String getMobileInvitationUrl(String uri){

        try {
        URL url = new URL(uri);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setInstanceFollowRedirects(false);
    
        int status = con.getResponseCode();

        if (status == HttpURLConnection.HTTP_MOVED_TEMP
            || status == HttpURLConnection.HTTP_MOVED_PERM) {
                String location = con.getHeaderField("Location");
                URL newUrl = new URL(location);             
                return "gxfspcm://aries_connection_invitation?"+newUrl.getQuery();
            }          
        }
        catch (Exception e) {   
        }
        return null;
    }  
}
