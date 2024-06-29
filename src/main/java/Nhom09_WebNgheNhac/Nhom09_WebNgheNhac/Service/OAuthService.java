package Nhom09_WebNgheNhac.Nhom09_WebNgheNhac.Service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
@Service
public class OAuthService extends DefaultOAuth2UserService {
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        // Lấy thông tin người dùng từ oauth2User
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");
        String img = oauth2User.getAttribute("images");
        // Lưu thông tin người dùng vào cơ sở dữ liệu
        UserService.saveOauthUser(email, name, img);

        return oauth2User;
        //return super.loadUser(userRequest);
    }
}