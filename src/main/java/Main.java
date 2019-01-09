public class Main {
    public static void main(String[] args) {
        try{
            InformationRetrieval informationRetrieval = new InformationRetrieval();
            informationRetrieval.run();
        }catch (Exception e){
            System.out.println("Exception happened, closing program.");
            System.out.println(e.getMessage());
        }
    }

}
