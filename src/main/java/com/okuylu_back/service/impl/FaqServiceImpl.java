package com.okuylu_back.service.impl;

import com.okuylu_back.model.Faq;
import com.okuylu_back.repository.FaqRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FaqServiceImpl {

    private final FaqRepository faqRepository;

    public FaqServiceImpl(FaqRepository faqRepository) {
        this.faqRepository = faqRepository;
    }

    public Faq addFaq(Faq faq) {
        return faqRepository.save(faq);
    }

    public void deleteFaq(Long id) {
        faqRepository.deleteById(id);
    }

    public List<Faq> getAllFaqs() {
        return faqRepository.findAll();
    }

    public Faq updateFaq(Long id, Faq updatedFaq) {
        return faqRepository.findById(id)
                .map(existingFaq -> {
                    existingFaq.setQuestion(updatedFaq.getQuestion());
                    existingFaq.setAnswer(updatedFaq.getAnswer());
                    return faqRepository.save(existingFaq);
                })
                .orElseThrow(() -> new RuntimeException("FAQ с id " + id + " не найден"));
    }
}
