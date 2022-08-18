package com.hubspot.challenge.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Configuration
public class SwaggerConfig {
    @Controller
    public static class SwaggerRedirectController {
        @RequestMapping("/")
        public ModelAndView redirectToSwaggerUI(ModelMap model) {
            model.addAttribute("attribute", "redirectWithRedirectPrefix");
            return new ModelAndView("redirect:/swagger-ui.html", model);
        }
    }
}
