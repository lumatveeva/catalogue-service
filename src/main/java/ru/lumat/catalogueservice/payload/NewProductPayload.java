package ru.lumat.catalogueservice.payload;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NewProductPayload(

        @NotNull(message = "{catalogue.products.create.errors.title_is_null}")
        @Size(min = 3, max = 50, message = "{catalogue.products.create.errors.title_size_is_invalid}")
        String title,

        @Size(max = 100, message = "{catalogue.products.create.errors.details_size_are_invalid}")
        String details) {
}
