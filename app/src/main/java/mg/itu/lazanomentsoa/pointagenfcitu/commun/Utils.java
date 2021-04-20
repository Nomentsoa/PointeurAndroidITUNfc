package mg.itu.lazanomentsoa.pointagenfcitu.commun;

import java.util.Date;

public class Utils {
    public static String createUniqueTag(){
        return "t"+(new Date().getTime());
    }
}
