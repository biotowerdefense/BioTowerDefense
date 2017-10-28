package cisgvsu.biotowerdefense;

/**
 * Created by Ella on 8/31/2017.
 */

public enum AntibioticType {
    penicillin, vancomycin, linezolid;

    public static int getPower(AntibioticType type) {
        switch (type) {
            case penicillin:
                return 1;
            case vancomycin:
                return 2;
            case linezolid:
                return 3;
            default:
                return 0;
        }
    }
     public static int getCost(AntibioticType type) {
         switch (type) {
             case penicillin:
                 return 1;
             case vancomycin:
                 return 2;
             case linezolid:
                 return 3;
             default:
                 return 0;
         }
     }
}