package ukma.domain;

public record University(String name, String shortName, String city, String address) {
    public University {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name can't be empty");
        if (shortName == null || shortName.isBlank()) throw new IllegalArgumentException("Short name can't be empty");
        if (city == null || city.isBlank()) throw new IllegalArgumentException("City can't be empty");
        if (address == null || address.isBlank()) throw new IllegalArgumentException("Address can't be empty");
    }
}