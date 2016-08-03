package by.itransition.webconstructor.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/social")
public class SocialAuthController {

    @Autowired
    private Facebook facebook;

    @Autowired
    private Twitter twitter;

    @Autowired
    private ConnectionRepository connectionRepository;

    @GetMapping("/facebook")
    public @ResponseBody
    String facebookProfile(Model model) {
        if (connectionRepository.findPrimaryConnection(Facebook.class) == null) {
            return "No connection";
        }
        String res = facebook.userOperations().getUserProfile().getName();
        res += "<br/>\n" + facebook.userOperations().getUserProfile().getLastName();
        res += "<br/>\n" + facebook.userOperations().getUserProfile().getFirstName();
        res += "<br/>\n" + facebook.userOperations().getUserProfile().getId();

        return res;
    }

    @GetMapping("/twitter")
    public @ResponseBody
    String twitterProfile(Model model) {
        if (connectionRepository.findPrimaryConnection(Twitter.class) == null) {
            return "No connection";
        }
        String res = twitter.userOperations().getUserProfile().getName();
        res += "<br/>\n" + twitter.userOperations().getScreenName();
        return res;
    }


}



