package com.example.client.controller;

import com.example.client.model.BaseModel;
import com.example.client.service.BaseApiService;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller CRUD dùng chung cho giao diện. Controller con chỉ cần @Controller + @RequestMapping("/{listName}")
 * và cung cấp các template {listName}/list.html, {listName}/form.html.
 * Model trong template: danh sách = ${listName}, 1 đối tượng = ${itemName}, từ khóa tìm kiếm = ${keyword}.
 */
public abstract class BaseMvcController<T extends BaseModel> {

    protected final BaseApiService<T> service;
    private final String itemName;   // vd: "student"  (tên biến trong template form)
    private final String listName;   // vd: "students" (tên biến danh sách, thư mục template, đường dẫn URL)
    private final String label;      // vd: "sinh viên" (dùng trong thông báo)

    protected BaseMvcController(BaseApiService<T> service, String itemName, String listName, String label) {
        this.service = service;
        this.itemName = itemName;
        this.listName = listName;
        this.label = label;
    }

    // Tạo đối tượng rỗng cho form thêm mới
    protected abstract T newItem();

    @GetMapping
    public String list(@RequestParam(required = false) String keyword, Model model) {
        model.addAttribute(listName, service.findAll(keyword));
        model.addAttribute("keyword", keyword);
        return listName + "/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute(itemName, newItem());
        return listName + "/form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Integer id, Model model) {
        model.addAttribute(itemName, service.findById(id));
        return listName + "/form";
    }

    // Tên model attribute mặc định lấy theo kiểu T (vd: Student -> "student"), khớp với itemName
    @PostMapping("/save")
    public String save(@Valid @ModelAttribute T item, BindingResult result, RedirectAttributes ra) {
        if (result.hasErrors()) {
            return listName + "/form";
        }
        boolean isNew = item.getId() == null;
        service.save(item);
        ra.addFlashAttribute("message", (isNew ? "Thêm " : "Cập nhật ") + label + " thành công");
        return "redirect:/" + listName;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id, RedirectAttributes ra) {
        service.delete(id);
        ra.addFlashAttribute("message", "Đã xóa " + label);
        return "redirect:/" + listName;
    }
}
