package org.ei.opensrp.view.viewHolder;

import android.graphics.drawable.Drawable;
import org.ei.opensrp.view.contract.ChildSmartRegisterClient;
import org.ei.opensrp.view.contract.SmartRegisterClient;
import org.ei.opensrp.AllConstants;

public class ChildRegisterProfilePhotoLoader implements ProfilePhotoLoader {
    private final Drawable maleInfantDrawable;
    private final Drawable femaleInfantDrawable;

    public ChildRegisterProfilePhotoLoader(Drawable maleInfantDrawable, Drawable femaleInfantDrawable) {
        this.maleInfantDrawable = maleInfantDrawable;
        this.femaleInfantDrawable = femaleInfantDrawable;
    }

    public Drawable get(SmartRegisterClient client) {
        return AllConstants.FEMALE_GENDER.equalsIgnoreCase(((ChildSmartRegisterClient) client).gender())
                ? femaleInfantDrawable
                : maleInfantDrawable;
    }
}
