package com.andino.backend_pos.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/vendedor")
public class VendedorDashboardController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "vendedor/dashboard";
    }
}
