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

     public static String getDescription(AntibioticType type) {
        switch (type) {
            case penicillin:
                return "Penicillin (PCN or pen) antibiotics were among the first medications to be " +
                        "effective against many bacterial infections caused by staphylococci and " +
                        "streptococci. Penicillins are still widely used today, though many types " +
                        "of bacteria have developed resistance following extensive use.  Penicillin " +
                        "was discovered in 1928 by Scottish scientist Alexander Fleming.  " +
                        "Penicillin targets the cell wall of bacteria.";
            case vancomycin:
                return "Vancomycin is an antibiotic used to treat a number of bacterial infections.  " +
                        "It is recommended intravenously as a treatment for complicated skin " +
                        "infections, bloodstream infections, and meningitis caused by " +
                        "Staphylococcus aureus.  Vancomycin targets the cell wall of bacteria.";
            case linezolid:
                return "Linezolid is an antibiotic used for the treatment of infections caused by " +
                        "Gram-positive bacteria that are resistant to other antibiotics.  " +
                        "Linezolid is active against most Gram-positive bacteria that cause " +
                        "disease, including streptococci, vancomycin-resistant enterococci (VRE), " +
                        "and methicillin-resistant Staphylococcus aureus (MRSA).  Linezolid targets " +
                        "the ribosome of bacteria.";
            default:
                return null;
        }
     }
}