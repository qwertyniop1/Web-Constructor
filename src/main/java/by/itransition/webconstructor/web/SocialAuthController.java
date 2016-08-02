package by.itransition.webconstructor.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/social")
public class SocialAuthController {

    @Autowired
    private Facebook facebook;



}



