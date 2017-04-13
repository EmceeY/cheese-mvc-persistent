package org.launchcode.controllers;

import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CategoryDao;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.launchcode.models.forms.AddMenuItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * Created by mcoreyyares on 4/13/17.
 */
@Controller
@RequestMapping(value = "menu")
public class MenuController {

    @Autowired
    private CheeseDao cheeseDao;

    @Autowired
    private MenuDao menuDao;

    @RequestMapping(value ="")
    public String index(Model model){
        model.addAttribute("menus", menuDao.findAll());
        model.addAttribute("title", "Menu of Cheeses");
        return "menu/index";
    }

    @RequestMapping(value="add", method = RequestMethod.GET)
    public String viewAddMenu(Model model){
        model.addAttribute(new Menu());
        model.addAttribute("title", "Add a Menu");
        return "menu/add";
    }

    @RequestMapping(value ="add", method = RequestMethod.POST)
    public String processAddMenu(Model model, @ModelAttribute @Valid Menu menu, Errors errors){
        if(errors.hasErrors()){
            model.addAttribute("title", "Add a Menu");
            return "menu/add";
        }
        model.addAttribute("menu", menu);
        menuDao.save(menu);
        return "redirect:view/" + menu.getId();
    }

    @RequestMapping(value="view", method = RequestMethod.GET)
    public String viewMenu(Model model, @PathVariable int id){
        model.addAttribute("menu", menuDao.findOne(id));
        return "redirect:view/" + id;
    }

    @RequestMapping(value="add-item", method = RequestMethod.GET)
    public String addItem(Model model, @PathVariable int id){
        Menu menu = menuDao.findOne(id);
        AddMenuItemForm addMenuItemForm = new AddMenuItemForm(menu, cheeseDao.findAll());
        model.addAttribute("form", addMenuItemForm);
        model.addAttribute("title", "Add item to menu: " + menu.getName());

        return"add-item";
    }

    @RequestMapping(value="add-item", method = RequestMethod.POST)
    public String addItem(Model model, @ModelAttribute @Valid AddMenuItemForm addMenuItemForm, Errors errors){
        if(errors.hasErrors()){
            return"add-item";
        }

        int menuId = addMenuItemForm.getMenuId();
        int cheeseId = addMenuItemForm.getCheeseId();

        Cheese cheese = cheeseDao.findOne(cheeseId);
        Menu menu = menuDao.findOne(menuId);

        menu.addIten(cheese);

        menuDao.save(menu);

        model.addAttribute("menu", menu);

        return "redirect:view/" + menu.getId();
    }

}
