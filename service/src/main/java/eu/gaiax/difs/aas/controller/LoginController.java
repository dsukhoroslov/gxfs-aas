package eu.gaiax.difs.aas.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.gaiax.difs.aas.service.SsiBrokerService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ssi")
public class LoginController {

    private final static Logger log = LoggerFactory.getLogger(LoginController.class);

    private final SsiBrokerService ssiBrokerService;

    @GetMapping(value = "/login")
    public String login(HttpServletRequest request, Model model) {

        log.debug("login; got params: {}", request.getParameterMap().size());

        return ssiBrokerService.authorize(model);
    }

    @GetMapping(value = "/qr/{qrid}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getQR(@PathVariable String qrid) {

        return ResponseEntity.ok(ssiBrokerService.getQR(qrid));

    }

    @GetMapping("/userinfo")
    public void userInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ssiBrokerService.userInfo(request, response);
    }

}
