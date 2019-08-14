package entities.enums;

public enum Sex {
    MALE ('M'),
    FEMALE('F');

    private char gender;
    Sex(char c) {
        this.gender = c;
    }

    @Override
    public String toString() {
        return String.valueOf(gender);
    }

    public char getGender() {
        return gender;
    }
}
