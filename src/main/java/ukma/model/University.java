package ukma.model;

public class University {
    private String name;
    private String shortName;
    private String city;
    private String address;

    public University(String name, String shortName, String city, String address) {
        setName(name);
        setShortName(shortName);
        setAddress(address);
        setCity(city);
    }

    public void setName(String n) {
        if (n == null ||n.isEmpty()) throw new IllegalArgumentException();
        name = n;
    }
    public void setShortName(String n) {
        if (n == null ||n.isEmpty()) throw new IllegalArgumentException();
        shortName = n;
    }

    public void setCity(String c) {
        if (c == null ||c.isEmpty()) throw new IllegalArgumentException();
        city = c;
    }

    public void setAddress(String address) {
        if (address == null ||address.isEmpty()) throw new IllegalArgumentException();
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "University{" +
                "name='" + name + '\'' +
                ", shortName='" + shortName + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
