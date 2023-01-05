package eu.gaiax.difs.aas.client;

import java.io.IOException;
import java.net.ProtocolException;
import java.nio.charset.MalformedInputException;

public interface InvitationServiceClient {

    String getMobileInvitationUrl(String url);
}
