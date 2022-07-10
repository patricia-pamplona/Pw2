package com.example.prova2_web.controllers;

import com.example.prova2_web.model.Almoco;
import com.example.prova2_web.model.Usuario;
import com.example.prova2_web.service.AlmocoService;
import com.example.prova2_web.service.FileStorageService;
import com.fasterxml.jackson.databind.DatabindException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MainController {
    @Autowired
    private AlmocoService service;
    @Autowired
    private FileStorageService fileStorageService;

    private int contCarrinho = 0;

    @GetMapping(value = {"/","/index"})
    public String doMain(Model model){
        Usuario usuario = getUsuarioLogado();

        List<Almoco> list = service.findAllNotDeleteds();

        model.addAttribute("almocoList", list);
        model.addAttribute("numero", contCarrinho);

        getMenu(usuario, model);

        return "index";
    }

    @GetMapping("/admin")
    public String doAdmin(Model model, RedirectAttributes redirectAttributes){
        Usuario usuario = getUsuarioLogado();

        getMenu(usuario, model);

        if(usuario.isAdmin()){
            List<Almoco> list = service.findAllNotDeleteds();
            model.addAttribute("listaAdm", list);

            //retornando o nome do template/pagina htnl que tem que ser criado
            return "admin";

        }else{
            redirectAttributes.addAttribute("msg", "Não há permissão de acesso para esse usuário!");
            return "redirect:/index";
        }

    }

    @GetMapping("/cadastro")
    public String doCadastro(Model model,RedirectAttributes redirectAttributes){
        Usuario usuario = getUsuarioLogado();

        getMenu(usuario, model);

        if(usuario.isAdmin()) {

            Almoco almoco = new Almoco();
            model.addAttribute("almoco", almoco);

            return "cadastro";
        }else{
            redirectAttributes.addAttribute("msg", "Não há permissão de acesso para esse usuário!");
            return "redirect:/index";
        }
    }

    @PostMapping("/salvar")
    public String doSalvaFilme(@ModelAttribute @Valid Almoco almoco, Errors errors, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes, HttpServletRequest request) {

        Usuario usuario = getUsuarioLogado();

        if(usuario.isAdmin()){
            if (errors.hasErrors()) {

                if(almoco.getId() == null){
                    return "cadastro";
                }else{
                    redirectAttributes.addAttribute("msg", "verifique se os campos estão preenchidos corretamente!");
                    return "redirect:/editar/" + almoco.getId();

                }

            } else {
                if(almoco.getId() != null && !file.isEmpty()){
                    almoco.setImageUri(fileStorageService.save(file));
                }else if(almoco.getId() == null && !file.isEmpty()){
                    almoco.setImageUri(fileStorageService.save(file));
                }

                if(almoco.getId() == null){
                    redirectAttributes.addAttribute("msg", "Cadastro realizado com sucesso");

                }else{
                    redirectAttributes.addAttribute("msg", "Item editado com sucesso");
                }

                service.update(almoco);

                return "redirect:/admin";
        }

        }else{
            redirectAttributes.addAttribute("msg", "Não há permissão de acesso para esse usuário!");
            return "redirect:/index";
        }
    }
    @GetMapping("/editar/{id}")
    public String doEditar(@PathVariable (name = "id") Long id, Model model, RedirectAttributes redirectAttributes){
        Usuario usuario = getUsuarioLogado();
        getMenu(usuario, model);

        if(usuario.isAdmin()) {
            Almoco almoco = service.findById(id);
            model.addAttribute("almoco", almoco);
            return "cadastro";
        }else{
            redirectAttributes.addAttribute("msg", "Não há permissão de acesso para esse usuário!");
            return "redirect:/index";
        }
    }

    @GetMapping("/deletar/{id}")
    public String doDeletar(@PathVariable (name = "id") Long id, RedirectAttributes redirectAttributes){
        Usuario usuario = getUsuarioLogado();

        if(usuario.isAdmin()) {
            Almoco almoco = service.findById(id);
            almoco.setDeleted(new Date());
            service.update(almoco);
            redirectAttributes.addAttribute("msg", "A remoção ocorreu com sucesso!");

            return "redirect:/index";
        }else{
            redirectAttributes.addAttribute("msg", "Não há permissão de acesso para esse usuário!");
            return "redirect:/index";
        }
    }

    @GetMapping("/adicionarCarrinho/{id}")
    public String doAdicionarCarrinho(@PathVariable (name = "id") Long id, HttpServletRequest request, RedirectAttributes redirectAttributes){

        Usuario usuario = getUsuarioLogado();

        if(!usuario.isAdmin()){
            HttpSession session = request.getSession();
            List<Almoco> carrinho = (List<Almoco>) session.getAttribute("carrinho");
            Almoco almoco = service.findById(id);
            if(carrinho == null){
                carrinho = new ArrayList<>();
            }

            carrinho.add(almoco);
            contCarrinho = carrinho.size();
            session.setAttribute("carrinho", carrinho);

            return "redirect:/index";

        }else{
            redirectAttributes.addAttribute("msg", "Não há permissão de acesso para esse usuário!");
            return "redirect:/index";
        }

    }

    @GetMapping("/verCarrinho")
    public String doVerCarrinho(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes, Model model){
        HttpSession session = request.getSession();
        long creationTime = session.getCreationTime();

        Date data = new Date(creationTime);
        Cookie c = new Cookie("visita", "" + data.getTime());
        c.setMaxAge(60*60*24);

        response.addCookie(c);

        Usuario usuario = getUsuarioLogado();
        getMenu(usuario, model);

        if(!usuario.isAdmin()){
            List<Almoco> list = (List<Almoco>) session.getAttribute("carrinho");
            if(list == null){
                list = new ArrayList<>();
            }

            if(list.isEmpty()){
                redirectAttributes.addAttribute("msg", "Carrinho está vazio!");
                return "redirect:/index";
            }else{
                model.addAttribute("listaCarrinho", list);
                return "carrinho";

            }
        }else{
            redirectAttributes.addAttribute("msg", "Não há permissão de acesso para esse usuário!");
            return "redirect:/index";
        }



    }

    @GetMapping("/finalizarCompra")
    public String doFinalizar(HttpServletRequest request, RedirectAttributes redirectAttributes){
        Usuario usuario = getUsuarioLogado();

        if(!usuario.isAdmin()) {
            HttpSession session = request.getSession();
            session.setAttribute("carrinho", null);
            contCarrinho = 0;
            redirectAttributes.addAttribute("msg", "compra Finalizada!");

            return "redirect:/index";
        }else {
            redirectAttributes.addAttribute("msg", "Não há permissão de acesso para esse usuário!");
            return "redirect:/index";
        }
    }

    public Usuario getUsuarioLogado(){
        Usuario usuarioLogado = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if(principal instanceof Usuario){
            usuarioLogado = (Usuario) principal;
        }
        return usuarioLogado;
    }

    public void getMenu(Usuario usuario, Model model){
        if(usuario != null){
            model.addAttribute("isUsuarioLogado", true);
            model.addAttribute("nome", usuario.getUsername());
            if(usuario.isAdmin()){
                model.addAttribute("isAdmin", true);
            } else{
                model.addAttribute("isNotAdmin", true);
            }
        }else{
            model.addAttribute("isUsuarioLogado", false);
            model.addAttribute("nome", "Visitante");
        }
    }



}
