package com.pagoseguro.controller;

import com.pagoseguro.dto.RegistroForm;
import com.pagoseguro.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {
    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String login(Authentication authentication) {
        if (estaLogueado(authentication)) {
            return "redirect:/transacciones";
        }
        return "auth/login";
    }

    @GetMapping("/registro")
    public String registro(Authentication authentication, Model model) {
        if (estaLogueado(authentication)) {
            return "redirect:/transacciones";
        }
        model.addAttribute("form", new RegistroForm());
        return "auth/registro";
    }

    @PostMapping("/registro")
    public String registrar(@Valid @ModelAttribute("form") RegistroForm form, BindingResult result,
                            RedirectAttributes flash) {
        if (form.getPassword() != null && !form.getPassword().equals(form.getConfirmarPassword())) {
            result.rejectValue("confirmarPassword", "noCoincide", "Las contraseñas no coinciden");
        }
        if (form.getUsername() != null && usuarioService.existeUsername(form.getUsername().trim())) {
            result.rejectValue("username", "duplicado", "Ese usuario ya está registrado");
        }
        if (result.hasErrors()) {
            return "auth/registro";
        }
        usuarioService.registrar(form);
        flash.addFlashAttribute("exito", "Cuenta creada. Ahora inicia sesión.");
        return "redirect:/login";
    }

    private boolean estaLogueado(Authentication authentication) {
        return authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
    }
}
