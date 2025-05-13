package ru.lumat.catalogueservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.lumat.catalogueservice.entity.Product;
import ru.lumat.catalogueservice.payload.NewProductPayload;
import ru.lumat.catalogueservice.service.ProductService;

import java.security.Principal;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("catalogue-api/products")
public class ProductsRestController {

    private final ProductService productService;

    @GetMapping()
    public Iterable<Product> findProducts(@RequestParam(name = "filter", required = false) String filter,
                                          Principal principal) {
        LoggerFactory.getLogger(ProductsRestController.class).info("Principal: {}",
                ((JwtAuthenticationToken) principal)
                        .getToken()
                        .getClaimAsString("email"));
        return productService.findAllProducts(filter);
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody NewProductPayload payload,
                                           BindingResult bindingResult,
                                           UriComponentsBuilder uriComponentsBuilder)
            throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            Product product = productService.createProduct(payload.title(), payload.details());
            return ResponseEntity
                    .created(uriComponentsBuilder
                            .replacePath("catalogue-api/products/{productId}")
                            .build(Map.of("productId", product.getId())))
                    .body(product);
        }

    }
}
