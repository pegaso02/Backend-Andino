package com.andino.backend_pos.Security;

import com.andino.backend_pos.DTOs.LoginRequest;
import com.andino.backend_pos.DTOs.ResponseDTOs.JwtResponse;
import com.andino.backend_pos.DTOs.ResponseDTOs.LoginResponse;
import com.andino.backend_pos.Model.Usuario;
import com.andino.backend_pos.Repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private JwtUtil jwtUtil;
    private AuthService authService;
    private PasswordEncoder passwordEncoder;
    private UsuarioRepository usuarioRepository;

    public AuthController(JwtUtil jwtUtil, AuthService authService, PasswordEncoder passwordEncoder, UsuarioRepository usuarioRepository) {
        this.jwtUtil = jwtUtil;
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Usuario usuario) {
        usuario.setContraseña(passwordEncoder.encode(usuario.getContraseña()));
        usuarioRepository.save(usuario);
        return ResponseEntity.ok("Registro exitoso");
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        var user = usuarioRepository.findByUsername(request.getUsername());

        if (user.isEmpty() || !passwordEncoder.matches(request.getPassword(), user.get().getContraseña())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
        System.out.println("aqui ");
        String token = jwtUtil.generateToken(request.getUsername());

        return ResponseEntity.ok(new JwtResponse(token));
    }
}
