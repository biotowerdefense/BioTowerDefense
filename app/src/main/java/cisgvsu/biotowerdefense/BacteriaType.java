package cisgvsu.biotowerdefense;


/**
 * Created by Kelsey on 9/8/2017.
 */

public enum BacteriaType {
    staph, pneumonia, strep;

    /**
     * Return the short common name for the bacteria.
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
     * Return the long scientific name for the bacteria.
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
}
