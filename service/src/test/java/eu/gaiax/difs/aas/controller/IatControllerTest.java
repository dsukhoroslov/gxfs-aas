package eu.gaiax.difs.aas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.gaiax.difs.aas.client.LocalTrustServiceClientImpl;
import eu.gaiax.difs.aas.generated.model.AccessRequestDto;
import eu.gaiax.difs.aas.generated.model.AccessResponseDto;
import eu.gaiax.difs.aas.generated.model.ServiceAccessScopeDto;
import eu.gaiax.difs.aas.properties.LocalTrustServiceClientProperties;
import eu.gaiax.difs.aas.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
public class IatControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    AuthService service;

    @MockBean
    LocalTrustServiceClientImpl client;

    @MockBean
    LocalTrustServiceClientProperties config;

    @Test
    void getRequest_missingRequestId_404() throws Exception {
        mockMvc.perform(
                        get("/clients/iat/request/"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getRequest_correctRequest_200() throws Exception {
        Map<String, Object> serviceResponse = Map.of(
                "iss", "responseSubject",
                "object", Map.of("scope", "responseScope", "sub", "responseDid"));
        AccessResponseDto expectedResponse = new AccessResponseDto()
                .subject("responseSubject")
                ._object(new ServiceAccessScopeDto().scope("responseScope").did("responseDid"));

        when(service.evaluate(eq("GetIatProofResult"), any())).thenReturn(serviceResponse);

        mockMvc.perform(
                        get("/clients/iat/requests/testRequestId")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse), false));
    }

    @Test
    void postRequest_missingAccessRequest_400() throws Exception {
        mockMvc.perform(
                        post("/clients/iat/requests")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(new AccessRequestDto())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postRequest_missingServiceAccessScope_400() throws Exception {
        mockMvc.perform(
                        post("/clients/iat/requests")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(new AccessRequestDto().subject("testSubject"))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postRequest_missingSubject_400() throws Exception {
        mockMvc.perform(
                        post("/clients/iat/requests")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(new AccessRequestDto()
                                        ._object(
                                                new ServiceAccessScopeDto().scope("testScope").did("testDid")
                                        )
                                )))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postRequest_missingSubjectDid_400() throws Exception {
        mockMvc.perform(
                        post("/clients/iat/requests")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(new AccessRequestDto()
                                        .subject("testSubject")
                                        ._object(
                                                new ServiceAccessScopeDto().scope("testScope")
                                        )
                                )))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postRequest_missingSubjectScope_400() throws Exception {
        mockMvc.perform(
                        post("/clients/iat/requests")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(new AccessRequestDto()
                                        .subject("testSubject")
                                        ._object(
                                                new ServiceAccessScopeDto().did("testDid")
                                        )
                                )))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postRequest_correctRequest_200() throws Exception {
        AccessRequestDto requestDto = new AccessRequestDto()
                .subject("testSubject")
                ._object(new ServiceAccessScopeDto().scope("testScope").did("testDid"));
        Map<String, Object> serviceResponse = Map.of("requestId", "responseRequestId");
        AccessResponseDto expectedResponse = new AccessResponseDto()
                .requestId("responseRequestId")
                ._object(new ServiceAccessScopeDto());

        when(service.evaluate(eq("GetIatProofInvitation"), any())).thenReturn(serviceResponse);

        mockMvc.perform(
                        post("/clients/iat/requests")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse), false));
    }

}
