package org.ei.opensrp.view.controller;

import android.content.Intent;
import org.ei.opensrp.view.activity.ANCDetailActivity;
import org.ei.opensrp.view.activity.ChildDetailActivity;
import org.ei.opensrp.view.activity.EligibleCoupleDetailActivity;
import org.ei.opensrp.view.activity.PNCDetailActivity;
import org.ei.opensrp.AllConstants;

public class ProfileNavigationController {

    public static void navigateToECProfile(android.content.Context context, String caseId) {
        Intent intent = new Intent(context.getApplicationContext(), EligibleCoupleDetailActivity.class);
        intent.putExtra(AllConstants.CASE_ID, caseId);
        context.startActivity(intent);
    }

    public static void navigateToANCProfile(android.content.Context context, String caseId) {
        Intent intent = new Intent(context.getApplicationContext(), ANCDetailActivity.class);
        intent.putExtra(AllConstants.CASE_ID, caseId);
        context.startActivity(intent);
    }

    public static void navigateToPNCProfile(android.content.Context context, String caseId) {
        Intent intent = new Intent(context.getApplicationContext(), PNCDetailActivity.class);
        intent.putExtra(AllConstants.CASE_ID, caseId);
        context.startActivity(intent);
    }

    public static void navigateToChildProfile(android.content.Context context, String caseId) {
        Intent intent = new Intent(context.getApplicationContext(), ChildDetailActivity.class);
        intent.putExtra(AllConstants.CASE_ID, caseId);
        context.startActivity(intent);
    }
}
