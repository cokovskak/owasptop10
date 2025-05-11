package com.example.owasp.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class CustomErrorController implements ErrorController {

    private final ErrorAttributes errorAttributes;

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        WebRequest webRequest = new ServletWebRequest(request);

        Map<String, Object> errorDetails = errorAttributes.getErrorAttributes(
                webRequest,
                ErrorAttributeOptions.of(ErrorAttributeOptions.Include.STACK_TRACE)
        );

        model.addAttribute("errorDetails", errorDetails);
        return "errorPage";
    }
}
