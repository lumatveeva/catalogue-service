package ru.lumat.catalogueservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.lumat.catalogueservice.entity.Product;
import ru.lumat.catalogueservice.payload.UpdateProductPayload;
import ru.lumat.catalogueservice.service.ProductService;

import java.util.Locale;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("catalogue-api/products/{productId:\\d+}")
public class ProductRestController {

    private final ProductService productService;
    private final MessageSource messageSource;

    @ModelAttribute("product")
    Product product(@PathVariable("productId") int productId) {
        return productService.findProduct(productId).orElseThrow(
                () -> new NoSuchElementException("catalogue.errors.product.not_found")
        );
    }

    @GetMapping
    public Product findProduct(@ModelAttribute("product") Product product) {
        return product;
    }

    @PatchMapping("/update")
    public ResponseEntity<Void> updateProduct(@ModelAttribute("product") Product product,
                                              @Valid @RequestBody UpdateProductPayload payload,
                                              BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            productService.updateProduct(product.getId(),
                    payload.title(),
                    payload.details());
            return ResponseEntity.noContent().build();
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteProduct(@PathVariable("productId") int productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ProblemDetail> noSuchElementException(NoSuchElementException exception, Locale locale) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ProblemDetail.forStatusAndDetail(
                        HttpStatus.NOT_FOUND,
                        messageSource.getMessage(
                                exception.getMessage(),
                                new Object[0],
                                exception.getMessage(),
                                locale)
                ));
    }
}
