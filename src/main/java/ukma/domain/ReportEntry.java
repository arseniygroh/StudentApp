package ukma.domain;

public record ReportEntry(String category, Number result) {
    @Override
    public String toString() {
        return category + ": " + result;
    }
}