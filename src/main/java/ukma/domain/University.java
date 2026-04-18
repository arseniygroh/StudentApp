package ukma.domain;

import ukma.domain.annotations.Length;
import ukma.service.validation.AnnotationValidator;

public record University(
        @Length(min = 5, max = 100) String name,
        @Length(min = 2, max = 10) String shortName,
        @Length(min = 2, max = 50) String city,
        @Length(min = 5, max = 100) String address
) {
    public University(String name, String shortName, String city, String address) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name can't be empty");
        if (shortName == null || shortName.isBlank()) throw new IllegalArgumentException("Short name can't be empty");
        if (city == null || city.isBlank()) throw new IllegalArgumentException("City can't be empty");
        if (address == null || address.isBlank()) throw new IllegalArgumentException("Address can't be empty");

        this.name = name.trim();
        this.shortName = shortName.trim();
        this.city = city.trim();
        this.address = address.trim();
        AnnotationValidator.validate(this);
    }
}