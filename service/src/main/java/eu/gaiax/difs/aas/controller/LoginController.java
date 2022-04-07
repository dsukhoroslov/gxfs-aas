package eu.gaiax.difs.aas.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import eu.gaiax.difs.aas.service.SsiBrokerService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ssi")
public class LoginController {

    private final SsiBrokerService ssiBrokerService;

    @GetMapping(value = "/login")
    public String loginPage(HttpServletRequest request, Model model) {

        DefaultSavedRequest auth = (DefaultSavedRequest) request.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST");
        model.addAttribute("scope", auth.getParameterValues("scope"));
        String[] age = auth.getParameterValues("not_older_than");
        if (age != null && age.length > 0) {
            model.addAttribute("not_older_than", age[0]);
        }
        age = auth.getParameterValues("max_age");
        if (age != null && age.length > 0) {
            model.addAttribute("max_age", age[0]);
        }
        
        return ssiBrokerService.authorize(model);
    }

//    @PostMapping(value = "/login") // TODO: Viktor overrides Spring security POST auth endpoint
//    public String login(HttpServletRequest request, Model model) {
//
//        DefaultSavedRequest auth = (DefaultSavedRequest) request.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST");
//        model.addAttribute("scope", auth.getParameterValues("scope"));
//        String[] age = auth.getParameterValues("not_older_than");
//        if (age != null && age.length > 0) {
//            model.addAttribute("not_older_than", age[0]);
//        }
//        age = auth.getParameterValues("max_age");
//        if (age != null && age.length > 0) {
//            model.addAttribute("max_age", age[0]);
//        }
//
//        return ssiBrokerService.authorize(model);
//    }

    @GetMapping(value = "/qr/{qrid}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getQR(@PathVariable String qrid) {

        return ResponseEntity.ok(ssiBrokerService.getQR(qrid));

    }

    @GetMapping("/error")
    public String loginError(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        String errorMessage = null;
        if (session != null) {
            OAuth2AuthenticationException ex = (OAuth2AuthenticationException) session
                    .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (ex != null) {
                errorMessage = ex.getError().getErrorCode();
            }
        }
        model.addAttribute("errorMessage", errorMessage);
        return ssiBrokerService.authorize(model);
    }
}
