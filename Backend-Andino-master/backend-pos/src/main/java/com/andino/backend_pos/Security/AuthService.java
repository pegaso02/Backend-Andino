package com.andino.backend_pos.Security;

import com.andino.backend_pos.Model.Usuario;
import com.andino.backend_pos.Repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    UsuarioRepository usuarioRepository;

    public AuthService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario login(String username, String password) {
        return usuarioRepository.findByUsername(username)
                .filter(u -> u.getContraseña().equals(password)) // ⚠ Usa BCrypt en producción
                .orElseThrow(() -> new RuntimeException("Usuario o contraseña inválidos"));

    }
}
