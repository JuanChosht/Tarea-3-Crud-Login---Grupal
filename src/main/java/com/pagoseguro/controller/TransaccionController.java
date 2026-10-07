package com.pagoseguro.controller;

import com.pagoseguro.model.Transaccion;
import com.pagoseguro.service.TransaccionService;
import jakarta.validation.Valid;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/transacciones")
public class TransaccionController {
    private static final String NO_MODIFICABLE =
            "La transacción %s ya no se puede modificar porque está en estado \"%s\".";

    private final TransaccionService transaccionService;

    public TransaccionController(TransaccionService transaccionService) {
        this.transaccionService = transaccionService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
        binder.setDisallowedFields("estado", "fechaCreacion");
    }

    @GetMapping
    public String listar(@RequestParam(name = "q", required = false) String q, Model model) {
        model.addAttribute("transacciones", transaccionService.listar(q));
        model.addAttribute("q", q);
        model.addAttribute("total", transaccionService.contar());
        model.addAttribute("retenido", transaccionService.montoRetenido());
        model.addAttribute("liberado", transaccionService.montoLiberado());
        return "transacciones/lista";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        Transaccion transaccion = new Transaccion();
        transaccion.setDiasEntrega(5);
        model.addAttribute("transaccion", transaccion);
        return "transacciones/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes flash) {
        Transaccion transaccion = transaccionService.buscarPorId(id).orElse(null);
        if (transaccion == null) {
            flash.addFlashAttribute("error", "La transacción #" + id + " no existe.");
            return "redirect:/transacciones";
        }
        if (!transaccion.isModificable()) {
            flash.addFlashAttribute("error", NO_MODIFICABLE.formatted(transaccion.getCodigo(), transaccion.getEstado().getEtiqueta()));
            return "redirect:/transacciones";
        }
        model.addAttribute("transaccion", transaccion);
        return "transacciones/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("transaccion") Transaccion datos, BindingResult result,
                          RedirectAttributes flash) {
        if (datos.getId() == null) {
            if (result.hasErrors()) {
                return "transacciones/formulario";
            }
            Transaccion creada = transaccionService.crear(datos);
            flash.addFlashAttribute("exito", "Transacción " + creada.getCodigo() + " creada. Comparte el código con el comprador.");
            return "redirect:/transacciones";
        }

        Transaccion actual = transaccionService.buscarPorId(datos.getId()).orElse(null);
        if (actual == null) {
            flash.addFlashAttribute("error", "La transacción #" + datos.getId() + " ya no existe.");
            return "redirect:/transacciones";
        }
        if (!actual.isModificable()) {
            flash.addFlashAttribute("error", NO_MODIFICABLE.formatted(actual.getCodigo(), actual.getEstado().getEtiqueta()));
            return "redirect:/transacciones";
        }
        if (result.hasErrors()) {
            return "transacciones/formulario";
        }
        transaccionService.actualizar(actual, datos);
        flash.addFlashAttribute("exito", "Transacción " + actual.getCodigo() + " actualizada.");
        return "redirect:/transacciones";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
        Transaccion transaccion = transaccionService.buscarPorId(id).orElse(null);
        if (transaccion == null) {
            flash.addFlashAttribute("error", "La transacción #" + id + " no existe.");
        } else if (!transaccion.isModificable()) {
            flash.addFlashAttribute("error", NO_MODIFICABLE.formatted(transaccion.getCodigo(), transaccion.getEstado().getEtiqueta()));
        } else {
            transaccionService.eliminar(transaccion);
            flash.addFlashAttribute("exito", "Transacción " + transaccion.getCodigo() + " eliminada.");
        }
        return "redirect:/transacciones";
    }
}
