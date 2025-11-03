package com.okuylu_back.controller;


import com.okuylu_back.model.Faq;
import com.okuylu_back.service.impl.FaqServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public/faq")
public class FaqController {

    private final FaqServiceImpl faqService;

    public FaqController(FaqServiceImpl faqService) {
        this.faqService = faqService;
    }


    @GetMapping
    public List<Faq> getFaqs() {
        return faqService.getAllFaqs();
    }
}