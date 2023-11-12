package Main;

interface Iterator {
    enum TYPE{IMPLICIT, EXPLICIT};
    abstract TYPE getTYPE();
    abstract double method();
}
