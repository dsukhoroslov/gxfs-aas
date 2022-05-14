package eu.gaiax.difs.aas.controller;

import eu.gaiax.difs.aas.generated.controller.IatControllerApiDelegate;
import eu.gaiax.difs.aas.generated.model.AccessRequestDto;
import eu.gaiax.difs.aas.generated.model.AccessResponseDto;
import eu.gaiax.difs.aas.service.SsiIatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IatController implements IatControllerApiDelegate {

    private final SsiIatService iatService;

    @Override
    public ResponseEntity<AccessResponseDto> postAccessRequest(AccessRequestDto accessRequestDto) {

        return ResponseEntity.ok(iatService.evaluateIatProofInvitation(accessRequestDto));

    }

    @Override
    public ResponseEntity<AccessResponseDto> getAccessRequest(String requestId) {

        return ResponseEntity.ok(iatService.evaluateIatProofResult(requestId));

    }

}

