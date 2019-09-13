public class Faktorijel{

    public static void main(String[] args) {
      //  System.out.println(faktorijel(3));
        System.out.println(faktorijelRekurzija(4));

    }

    public static int faktorijel(int n) {
        int faktorijel = 1;
        for (int i = 1; i <= n; i++) {
            faktorijel *= i;
            System.out.println("faktorijel: " + faktorijel);//preko niza
        }
        return faktorijel;
    }

    public static int faktorijelRekurzija(int n) {
        if (n <= 1) {
            return n;
        }
        
        return (n * faktorijelRekurzija(n - 1));

    }

}
