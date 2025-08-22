package name.iavael.xposed.enablecontactsgroups;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented tests for EnableContactsGroups Xposed module.
 * Tests app configuration, manifest metadata, and Android-specific functionality.
 */
@RunWith(AndroidJUnit4.class)
public class EnableContactsGroupsInstrumentedTest {

    @Test
    public void app_context_package_name_is_correct() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("name.iavael.xposed.enablecontactsgroups", appContext.getPackageName());
    }

    @Test
    public void app_has_correct_application_id() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        ApplicationInfo appInfo = appContext.getApplicationInfo();
        assertEquals("name.iavael.xposed.enablecontactsgroups", appInfo.packageName);
    }

    @Test
    public void app_has_xposed_module_metadata() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        PackageManager pm = appContext.getPackageManager();
        ApplicationInfo appInfo = pm.getApplicationInfo(
                appContext.getPackageName(), 
                PackageManager.GET_META_DATA
        );

        assertNotNull("App should have metadata", appInfo.metaData);
        
        // Test Xposed module flag
        assertTrue("App should be marked as Xposed module",
                appInfo.metaData.getBoolean("xposedmodule", false));
    }

    @Test
    public void app_has_xposed_description_metadata() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        PackageManager pm = appContext.getPackageManager();
        ApplicationInfo appInfo = pm.getApplicationInfo(
                appContext.getPackageName(), 
                PackageManager.GET_META_DATA
        );

        assertNotNull("App should have metadata", appInfo.metaData);
        
        String description = appInfo.metaData.getString("xposeddescription");
        assertNotNull("App should have Xposed description", description);
        assertTrue("Description should mention groups support", 
                description.toLowerCase().contains("groups"));
        assertEquals("Enable groups support for all contacts providers", description);
    }

    @Test
    public void app_has_xposed_minimum_version() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        PackageManager pm = appContext.getPackageManager();
        ApplicationInfo appInfo = pm.getApplicationInfo(
                appContext.getPackageName(), 
                PackageManager.GET_META_DATA
        );

        assertNotNull("App should have metadata", appInfo.metaData);
        
        int minVersion = appInfo.metaData.getInt("xposedminversion", -1);
        assertTrue("App should specify minimum Xposed version", minVersion > 0);
        assertEquals("Minimum Xposed version should be 36", 36, minVersion);
    }

    @Test
    public void app_version_info_is_correct() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        PackageManager pm = appContext.getPackageManager();
        PackageInfo packageInfo = pm.getPackageInfo(appContext.getPackageName(), 0);

        assertEquals("Version name should be 0.2", "0.2", packageInfo.versionName);
        assertEquals("Version code should be 2", 2, packageInfo.versionCode);
    }

    @Test
    public void app_has_required_attributes() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        PackageManager pm = appContext.getPackageManager();
        ApplicationInfo appInfo = pm.getApplicationInfo(
                appContext.getPackageName(), 
                PackageManager.GET_META_DATA
        );

        // Test app allows backup (should be true for user convenience)
        assertTrue("App should allow backup", 
                (appInfo.flags & ApplicationInfo.FLAG_ALLOW_BACKUP) != 0);

        // Test app supports RTL (should be true for internationalization)  
        assertTrue("App should support RTL",
                (appInfo.flags & ApplicationInfo.FLAG_SUPPORTS_RTL) != 0);
    }

    @Test
    public void app_has_app_name_resource() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        
        // Test that app name resource exists and is not empty
        try {
            String appName = appContext.getString(
                appContext.getApplicationInfo().labelRes
            );
            assertNotNull("App name should not be null", appName);
            assertFalse("App name should not be empty", appName.trim().isEmpty());
        } catch (Exception e) {
            fail("App should have a valid app name resource: " + e.getMessage());
        }
    }

    @Test
    public void app_targets_correct_sdk_version() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        PackageManager pm = appContext.getPackageManager();
        ApplicationInfo appInfo = pm.getApplicationInfo(
                appContext.getPackageName(), 
                PackageManager.GET_META_DATA
        );

        // Test target SDK version
        assertTrue("App should target SDK 18 or higher", appInfo.targetSdkVersion >= 18);
        assertEquals("App should target SDK 28", 28, appInfo.targetSdkVersion);
    }
}
