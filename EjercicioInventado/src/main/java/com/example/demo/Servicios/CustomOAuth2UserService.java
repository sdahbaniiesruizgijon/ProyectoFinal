package com.example.demo.Servicios;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.demo.clases.Usuario;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {

        OAuth2User oauthUser = super.loadUser(userRequest);

        // Obtener identificador válido según proveedor
        String email = oauthUser.getAttribute("email");
        String login = oauthUser.getAttribute("login"); // GitHub
        String name = oauthUser.getAttribute("name");

        String identificador;
        if (email != null) {
            identificador = email;
        } else if (login != null) {
            identificador = login;
        } else if (name != null) {
            identificador = name;
        } else {
            identificador = oauthUser.getName();
        }

        // Buscar usuario en BD
        Usuario usuario = usuarioServicio.obtenerPorNombre(identificador);

        // Si no existe, crearlo
        if (usuario == null) {
            usuario = new Usuario();
            usuario.setUsuario(identificador);
            usuario.setRol("ROLE_CLIENTE");
            usuario.setPassword("OAUTH2_USER");
            usuarioServicio.guardarUsuario(usuario);
        }

        // Authorities con el rol real
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(usuario.getRol()));

        // Obtener el atributo que Spring usa como identificador
        String nameAttributeKey =
                userRequest.getClientRegistration()
                        .getProviderDetails()
                        .getUserInfoEndpoint()
                        .getUserNameAttributeName();

        // Devolver usuario OAuth2 con rol correcto
        return new DefaultOAuth2User(
                authorities,
                oauthUser.getAttributes(),
                nameAttributeKey
        );
    }
}
