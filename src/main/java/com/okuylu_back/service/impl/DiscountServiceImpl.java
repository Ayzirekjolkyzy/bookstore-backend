package com.okuylu_back.service.impl;

import com.okuylu_back.dto.request.DiscountRequest;
import com.okuylu_back.dto.responses.DiscountResponse;
import com.okuylu_back.model.Book;
import com.okuylu_back.utils.exceptions.ResourceNotFoundException;
import com.okuylu_back.model.Discount;
import com.okuylu_back.repository.DiscountRepository;
import com.okuylu_back.service.DiscountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.math.BigDecimal;
import java.util.stream.Collectors;


@Service
public class DiscountServiceImpl implements DiscountService {

    private static final Logger logger = LoggerFactory.getLogger(DiscountServiceImpl.class);
    private final DiscountRepository discountRepository;

    public DiscountServiceImpl(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    public List<DiscountResponse> getAllDiscounts() {
        return discountRepository.findAll().stream()
                .map(discount -> new DiscountResponse(
                        discount.getDiscountId(),
                        discount.getDiscountName(),
                        discount.getDiscountPercentage(),
                        discount.getDiscImage(),
                        discount.getStartDate(),
                        discount.getEndDate()))
                .collect(Collectors.toList());
    }

    @Override
    public DiscountResponse getDiscountById(Long id) {
        logger.info("Поиск скидки с ID: {}", id);
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Discount with ID " + id + " not found."));

        return new DiscountResponse(
                discount.getDiscountId(),
                discount.getDiscountName(),
                discount.getDiscountPercentage(),
                discount.getDiscImage(),
                discount.getStartDate(),
                discount.getEndDate());
    }

    @Override
    public DiscountResponse createDiscount(DiscountRequest request) {
        Discount discount = new Discount();
        discount.setDiscountName(request.getDiscountName());
        discount.setDiscountPercentage(request.getDiscountPercentage());
        discount.setDiscImage(request.getDiscImage());
        discount.setStartDate(request.getStartDate());
        discount.setEndDate(request.getEndDate());

        Discount savedDiscount = discountRepository.save(discount);

        return new DiscountResponse(
                savedDiscount.getDiscountId(),
                savedDiscount.getDiscountName(),
                savedDiscount.getDiscountPercentage(),
                savedDiscount.getDiscImage(),
                savedDiscount.getStartDate(),
                savedDiscount.getEndDate()
        );
    }

    @Override
    public DiscountResponse updateDiscount(Long id, DiscountRequest updatedDiscount) {
        logger.info("Обновление скидки с ID: {}", id);
        Discount existingDiscount = discountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Discount with ID " + id + " not found."));

        if (updatedDiscount.getDiscountName() != null) {
            existingDiscount.setDiscountName(updatedDiscount.getDiscountName());
        }
        if (updatedDiscount.getDiscountPercentage() != null) {
            existingDiscount.setDiscountPercentage(updatedDiscount.getDiscountPercentage());
        }
        if (updatedDiscount.getDiscImage() != null) {
            existingDiscount.setDiscImage(updatedDiscount.getDiscImage());
        }
        if (updatedDiscount.getStartDate() != null) {
            existingDiscount.setStartDate(updatedDiscount.getStartDate());
        }
        if (updatedDiscount.getEndDate() != null) {
            existingDiscount.setEndDate(updatedDiscount.getEndDate());
        }

        discountRepository.save(existingDiscount);
        return new DiscountResponse(
                existingDiscount.getDiscountId(),
                existingDiscount.getDiscountName(),
                existingDiscount.getDiscountPercentage(),
                existingDiscount.getDiscImage(),
                existingDiscount.getStartDate(),
                existingDiscount.getEndDate()
        );
    }

    @Override
    public void deleteDiscount(Long id) {
        logger.info("Удаление скидки с ID: {}", id);
        if (!discountRepository.existsById(id)) {
            throw new ResourceNotFoundException("Discount with ID " + id + " not found.");
        }
        discountRepository.deleteById(id);
    }


    @Override
    public boolean isDiscountActive(Discount discount) {
        if (discount == null) return false;
        LocalDate today = LocalDate.now();
        return !discount.getStartDate().isAfter(today) && !discount.getEndDate().isBefore(today);
    }

    @Override
    public BigDecimal calculateDiscountedPrice(Book book) {
        BigDecimal price = book.getPrice();
        Discount discount = book.getDiscount();

        if (isDiscountActive(discount)) {
            BigDecimal discountAmount = price.multiply(discount.getDiscountPercentage().divide(BigDecimal.valueOf(100)));
            return price.subtract(discountAmount).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        return price.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
    @Override
    public Discount getDiscountByIdAsDiscount(Long id) {
        return discountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Discount with ID " + id + " not found."));
    }
    @Override
    public List<DiscountResponse> getActiveDiscounts() {
        LocalDate today = LocalDate.now();
        return discountRepository.findAll().stream()
                .filter(discount -> !discount.getStartDate().isAfter(today) && !discount.getEndDate().isBefore(today))
                .map(discount -> new DiscountResponse(
                        discount.getDiscountId(),
                        discount.getDiscountName(),
                        discount.getDiscountPercentage(),
                        discount.getDiscImage(),
                        discount.getStartDate(),
                        discount.getEndDate()))
                .collect(Collectors.toList());
    }
}
