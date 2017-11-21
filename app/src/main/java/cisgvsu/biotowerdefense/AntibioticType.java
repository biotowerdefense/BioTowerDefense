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
                 return 20;
             case vancomycin:
                 return 30;
             case linezolid:
                 return 40;
             default:
                 return 0;
         }
     }

     public static String toString(AntibioticType type) {
        switch (type) {
            case penicillin:
                return "penicillin";
            case vancomycin:
                return "vancomycin";
            case linezolid:
                return "linezolid";
            default:
                return null;
        }
     }

     public static AntibioticType stringToEnum(String str) {
        if (str.equalsIgnoreCase("penicillin")) {
            return penicillin;
        } else if (str.equalsIgnoreCase("vancomycin")) {
            return vancomycin;
        } else if (str.equalsIgnoreCase("linezolid")) {
            return linezolid;
        } else {
            return null;
        }
     }

     public static int getImage(AntibioticType type) {
         switch (type) {
             case penicillin:
                 return R.drawable.tower_penicillin;
             case vancomycin:
                 return R.drawable.tower_vancomycin;
             case linezolid:
                 return R.drawable.tower_linezolid;
             default:
                 return R.drawable.tower_placeholder;
         }
     }
}