package cisgvsu.biotowerdefense;


/**
 * Created by Kelsey on 9/8/2017.
 */

public enum BacteriaType {
    staph, pneumonia, strep;

    /**
     * Return the short common name for the target.
     * @param type
     * @return
     */
    public static String getShortName(BacteriaType type) {
        switch (type) {
            case staph:
                return "Staph";
            case pneumonia:
                return "Pneumonia";
            case strep:
                return "Strep";
            default:
                return null;
        }
    }

    /**
     * Return the long scientific name for the target.
     * @param type
     * @return
     */
    public static String getLongName(BacteriaType type) {
        switch (type) {
            case staph:
                return "Staphylococcus aureus";
            case pneumonia:
                return "Klebsiella pneumoniae";
            case strep:
                return "Streptococcus pyogenes";
            default:
                return null;
        }
    }

    public static int getImage(BacteriaType type) {
        switch (type) {
            case staph:
                return R.drawable.bacteria;
            case pneumonia:
                return R.drawable.bacteria;
            case strep:
                return R.drawable.bacteria;
            default:
                return 0;
        }
    }

    public static int getDescription(BacteriaType type) {
       switch (type) {
           case staph:
                return R.string.staph_description;
           case pneumonia:
               return R.string.pneumonia_description;
           case strep:
               return R.string.strep_desription;
           default:
               return 0;
       }
    }
}
