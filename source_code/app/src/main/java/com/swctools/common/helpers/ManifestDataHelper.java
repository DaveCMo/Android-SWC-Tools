package com.swctools.common.helpers;

import android.content.Context;
import android.content.Intent;

import com.swctools.config.AppConfig;
import com.swctools.common.enums.BundleKeys;
import com.swctools.swc_server_interactions.services.ManifestUpdateService;

import javax.json.JsonObject;

public class ManifestDataHelper {
    public static void processPlayerContent(JsonObject contentResult, Context mContext) {
        AppConfig appConfig = new AppConfig(mContext);
        int manifestVersionFound = contentResult.getJsonObject("result").getInt("manifestVersion");
        if (manifestVersionFound > appConfig.getKnownSwcManifest()) { // then we have some work to do... gather the data and hand it off to the ManifestUpdateService.

//            appConfig.setKnownSWCManifest(manifestVersionFound); //Update the manifest version
            String baseUrlFound = contentResult.getJsonObject("result").getJsonArray("secureCdnRoots").getString(0);
            if (!baseUrlFound.equalsIgnoreCase(appConfig.getSWCPatchesURL())) {

                appConfig.setSWCPatchesURL(baseUrlFound);
            }

            if (manifestVersionFound != appConfig.getKnownSwcManifest()) {//update the values in config and download the new data
                String manifestPath = contentResult.getJsonObject("result").getString("manifest");
                String manifestUrl = baseUrlFound + manifestPath;
                Intent i = new Intent(mContext, ManifestUpdateService.class);
                i.putExtra(BundleKeys.DIALOG_TITLE.toString(), "Downloading Conflict Data");
                i.putExtra(BundleKeys.MANIFESTURL.toString(), manifestUrl);
                i.putExtra(BundleKeys.PATCHESFILE.toString(), "patches/cae.json");
                i.putExtra(BundleKeys.MANIFESTVERSION.toString(), manifestVersionFound);
                mContext.startService(i);
            }
        }

    }
}
