package org.ei.opensrp.view;

import android.content.res.Resources;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class InternationalizationContext {

    private final Resources resources;

    public InternationalizationContext(Resources resources) {
        this.resources = resources;
    }

    public String getInternationalizedLabels() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("home_ec_label", resources.getString(org.ei.opensrp.R.string.home_ec_label));
        map.put("home_fp_label", resources.getString(org.ei.opensrp.R.string.home_fp_label));
        map.put("home_anc_label", resources.getString(org.ei.opensrp.R.string.home_anc_label));
        map.put("home_pnc_label", resources.getString(org.ei.opensrp.R.string.home_pnc_label));
        map.put("home_child_label", resources.getString(org.ei.opensrp.R.string.home_child_label));
        map.put("home_report_label", resources.getString(org.ei.opensrp.R.string.home_report_label));
        map.put("register_label", resources.getString(org.ei.opensrp.R.string.register_label));
        map.put("home_videos_label", resources.getString(org.ei.opensrp.R.string.home_videos_label));

        return new Gson().toJson(map);
    }
}
