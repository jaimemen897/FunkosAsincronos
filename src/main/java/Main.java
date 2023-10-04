import controllers.FunkoController;

public class Main {
    public static void main(String[] args) {
        FunkoController funkoController = FunkoController.getInstance();
        System.out.println(funkoController.loadCsv());


    }
}
