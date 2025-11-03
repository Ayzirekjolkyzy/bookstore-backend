package com.okuylu_back.controller.admin;

import com.okuylu_back.model.Faq;
import com.okuylu_back.service.impl.FaqServiceImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/faq")
@PreAuthorize("hasRole('ADMIN')")
public class AdminFaqController {
    private final FaqServiceImpl faqService;

    public AdminFaqController(FaqServiceImpl faqService) {
        this.faqService = faqService;
    }

    @PostMapping
    public Faq addFaq(@RequestBody Faq faq) {
        return faqService.addFaq(faq);
    }

    @DeleteMapping("/{id}")
    public void deleteFaq(@PathVariable Long id) {
        faqService.deleteFaq(id);
    }

    @GetMapping
    public List<Faq> getAllFaqs() {
        return faqService.getAllFaqs();
    }

    @PutMapping("/{id}")
    public Faq updateFaq(@PathVariable Long id, @RequestBody Faq updatedFaq) {
        return faqService.updateFaq(id, updatedFaq);
    }
}