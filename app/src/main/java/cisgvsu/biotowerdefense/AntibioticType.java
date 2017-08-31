package cisgvsu.biotowerdefense;

/**
 * Created by Ella on 8/31/2017.
 */

public enum AntibioticType {
    penicillin, amoxicillin, cephalexin, erythromycin;

    public static int getPower(AntibioticType type) {
        switch (type) {
            case penicillin:
                return 1;
            case amoxicillin:
                return 2;
            case cephalexin:
                return 3;
            case erythromycin:
                return 4;
            default:
                return 0;
        }
    }
     public static int getCost(AntibioticType type) {
         switch (type) {
             case penicillin:
                 return 1;
             case amoxicillin:
                 return 2;
             case cephalexin:
                 return 3;
             case erythromycin:
                 return 4;
             default:
                 return 0;
         }
     }
}
