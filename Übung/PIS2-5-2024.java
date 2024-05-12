interface Calc {
    int add(int x, int y);
    int neg(int x);
    default int sub(int x, int y) {
        return add(x, neg(y));
    }
}

class CalcImpl implements Calc {
    public int add(int x, int y) {
        return x + y;
    }
    public int neg(int x) {
        return -x;
    }
}

Calc c = new CalcImpl(){
    public int add(int x, int y) {
        return x + y;
    }
    public int neg(int x) {
        return -x - 1;
    }
};

class Calculator{
    int add(int x, int y){
        return x + y;
    }
    int sub(int x, int y){
        return x - y;
    }
} 

var calcul = new Calculator(){
     int neg(int x){
         return -x;
     }
};