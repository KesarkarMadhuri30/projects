package d.newnavigationapp.other;




import android.content.Context;
import d.newnavigationapp.R;



import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
public class ExpandableListDataSource {

    public static Map<String, List<String>> getData(Context context) {
        Map<String, List<String>> expandableListData = new TreeMap<>();

        List<String> mainmenu = Arrays.asList(context.getResources().getStringArray(R.array.MainMenu));

        List<String> fruit = Arrays.asList(context.getResources().getStringArray(R.array.fruit));
        List<String> personalCare = Arrays.asList(context.getResources().getStringArray(R.array.personalCare));
        List<String> homeneed = Arrays.asList(context.getResources().getStringArray(R.array.homeneed));
        List<String> beverage = Arrays.asList(context.getResources().getStringArray(R.array.Beverage));
        List<String> kids = Arrays.asList(context.getResources().getStringArray(R.array.Kids));


        expandableListData.put(mainmenu.get(0), fruit);
        expandableListData.put(mainmenu.get(1), personalCare);
        expandableListData.put(mainmenu.get(2), homeneed);
        expandableListData.put(mainmenu.get(3), beverage);
        expandableListData.put(mainmenu.get(4), kids);

        return expandableListData;
    }
}
