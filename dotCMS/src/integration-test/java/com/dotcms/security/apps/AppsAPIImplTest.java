package com.dotcms.security.apps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.dotcms.datagen.LayoutDataGen;
import com.dotcms.datagen.PortletDataGen;
import com.dotcms.datagen.RoleDataGen;
import com.dotcms.datagen.SiteDataGen;
import com.dotcms.datagen.TestUserUtils;
import com.dotcms.datagen.UserDataGen;
import com.dotcms.util.IntegrationTestInitService;
import com.dotmarketing.beans.Host;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.business.Layout;
import com.dotmarketing.business.LayoutAPI;
import com.dotmarketing.business.Role;
import com.dotmarketing.business.portal.PortletAPI;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.exception.DotSecurityException;
import com.dotmarketing.util.Logger;
import com.google.common.collect.ImmutableSet;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.model.User;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import io.vavr.Tuple2;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(DataProviderRunner.class)
public class AppsAPIImplTest {

    @BeforeClass
    public static void prepare() throws Exception {
        IntegrationTestInitService.getInstance().init();
        SecretsStore.INSTANCE.get().deleteAll();
    }

    @Test
    public void Test_Store_Json_Then_Recover_Decrypted_Then_Delete_Then_Verify_Its_Gone()
            throws DotDataException, DotSecurityException {
        final User admin = TestUserUtils.getAdminUser();
        AppSecrets.Builder builder = new AppSecrets.Builder();
        final String appKey = "anyAppKey";
        final AppSecrets bean = builder.withKey(appKey)
                .withHiddenSecret("mySecret1", "Once I saw a UFO")
                .withHiddenSecret("mySecret2", "Football soccer sucks!")
                .withSecret("boolSecret1", true)
                .withSecret("boolSecret2", false)
                .build();

        final Host host = new SiteDataGen().nextPersisted();
        final AppsAPI api = APILocator.getAppsAPI();
        api.saveSecrets(bean, host, admin);
        final Optional<AppSecrets> optionalBean = api
                .getSecrets(appKey, host, admin);

        assertTrue(optionalBean.isPresent());

        final AppSecrets recoveredBean = optionalBean.get();
        assertEquals(appKey, recoveredBean.getKey());
        assertTrue(recoveredBean.getSecrets().containsKey("mySecret1"));
        assertTrue(recoveredBean.getSecrets().containsKey("mySecret2"));
        assertTrue(recoveredBean.getSecrets().containsKey("boolSecret1"));
        assertTrue(recoveredBean.getSecrets().containsKey("boolSecret2"));

        final Secret secret1 = recoveredBean.getSecrets().get("mySecret1");
        assertEquals("Once I saw a UFO", secret1.getString());
        assertTrue(secret1.isHidden());

        final Secret secret2 = recoveredBean.getSecrets().get("mySecret2");
        assertEquals("Football soccer sucks!", secret2.getString());
        assertTrue(secret2.isHidden());

        final Secret boolSecret1 = recoveredBean.getSecrets().get("boolSecret1");
        assertTrue(boolSecret1.getBoolean());
        assertFalse(boolSecret1.isHidden());

        final Secret boolSecret2 = recoveredBean.getSecrets().get("boolSecret2");
        assertFalse(boolSecret2.getBoolean());
        assertFalse(boolSecret2.isHidden());
    }

    @Test
    public void Test_Store_Json_On_Different_Sites_Then_Pull_Sites_By_Host_Verify_Match()
            throws DotDataException, DotSecurityException {

        final User admin = TestUserUtils.getAdminUser();
        final Host host1 = new SiteDataGen().nextPersisted();
        final Host host2 = new SiteDataGen().nextPersisted();

        final AppSecrets.Builder builder1 = new AppSecrets.Builder();

        final AppSecrets bean1Host1 = builder1.withKey("appKey-1-Host-1")
                .withHiddenSecret("h1:secret1", "sec1")
                .withHiddenSecret("h1:secret2", "sec2")
                .withSecret("h1:bool1", true)
                .withSecret("h1:bool2", false)
                .build();

        final AppSecrets bean2Host1 = builder1.withKey("appKey-2-Host-1")
                .withHiddenSecret("h1:secret1", "sec1")
                .withHiddenSecret("h1:secret2", "sec2")
                .withSecret("h1:bool1", true)
                .withSecret("h1:bool2", false)
                .build();

        final AppSecrets bean3Host1 = builder1.withKey("appKey-3-Host-1")
                .withHiddenSecret("h1:secret1", "sec1")
                .withHiddenSecret("h1:secret2", "sec2")
                .withSecret("h1:bool1", true)
                .withSecret("h1:bool2", false)
                .build();

        final AppSecrets.Builder builder2 = new AppSecrets.Builder();

        final AppSecrets bean1Host2 = builder2.withKey("appKey-1-Host-2")
                .withHiddenSecret("h2:secret1", "sec1")
                .withHiddenSecret("h2:secret2", "sec2")
                .withSecret("h2:bool1", true)
                .withSecret("h2:bool2", false)
                .build();

        final AppsAPI api = APILocator.getAppsAPI();

        api.saveSecrets(bean1Host1, host1, admin);
        api.saveSecrets(bean2Host1, host1, admin);
        api.saveSecrets(bean3Host1, host1, admin);
        api.saveSecrets(bean1Host2, host2, admin);

        final Map<String, Set<String>> appKeysByHost = api.appKeysByHost();
        assertNotNull(appKeysByHost.get(host1.getIdentifier()));
        assertNotNull(appKeysByHost.get(host2.getIdentifier()));

        assertEquals(3, api.listAppKeys(host1, admin).size());
        assertEquals(1, api.listAppKeys(host2, admin).size());

        assertEquals(3, appKeysByHost.get(host1.getIdentifier()).size());
        assertEquals(1, appKeysByHost.get(host2.getIdentifier()).size());

        assertTrue(appKeysByHost.get(host1.getIdentifier()).contains("appKey-1-Host-1".toLowerCase()));
        assertTrue(appKeysByHost.get(host1.getIdentifier()).contains("appKey-2-Host-1".toLowerCase()));
        assertTrue(appKeysByHost.get(host1.getIdentifier()).contains("appKey-3-Host-1".toLowerCase()));
        assertTrue(appKeysByHost.get(host2.getIdentifier()).contains("appKey-1-Host-2".toLowerCase()));

    }

    @Test
    public void Test_Lookup_Fallback() throws DotDataException, DotSecurityException {
        final AppsAPI api = APILocator.getAppsAPI();
        final User admin = TestUserUtils.getAdminUser();
        final Host systemHost = APILocator.systemHost();
        final Host host1 = new SiteDataGen().nextPersisted();

        final AppSecrets.Builder builder1 = new AppSecrets.Builder();

        final AppSecrets beanSystemHost1 = builder1.withKey("appKey-1-Any-Host")
                .withHiddenSecret("fallback:test:secret1", "sec1")
                .withSecret("fallback:test", true)
                .build();

        api.saveSecrets(beanSystemHost1, systemHost, admin);

        final Optional<AppSecrets> serviceSecretsOptional = api.getSecrets("appKey-1-Any-Host", true, host1, admin);
        assertTrue(serviceSecretsOptional.isPresent());
        final AppSecrets recoveredBean = serviceSecretsOptional.get();
        assertEquals("appKey-1-Any-Host", recoveredBean.getKey());
        assertTrue(recoveredBean.getSecrets().containsKey("fallback:test"));
        assertTrue(recoveredBean.getSecrets().containsKey("fallback:test:secret1"));
    }

    @Test
    public void Test_Save_Secrets_Then_Replace_Secret() throws DotDataException, DotSecurityException {
        final AppsAPI api = APILocator.getAppsAPI();
        final User admin = TestUserUtils.getAdminUser();
        final Host host = new SiteDataGen().nextPersisted();

        final AppSecrets.Builder builder1 = new AppSecrets.Builder();
        final AppSecrets secrets1 = builder1.withKey("appKey-1-Host-1")
                .withHiddenSecret("test:secret1", "sec1")
                .withHiddenSecret("test:secret2", "sec2")
                .build();
        api.saveSecrets(secrets1, host, admin);

        final AppSecrets.Builder builder2 = new AppSecrets.Builder();
        final AppSecrets secrets2 = builder2.withKey("appKey-1-Host-1")
                .withHiddenSecret("test:secret1", "secret1")
                .build();
        api.saveSecrets(secrets2, host, admin);
        final Optional<AppSecrets> serviceSecretsOptional = api.getSecrets("appKey-1-Host-1", host, admin);
        assertTrue(serviceSecretsOptional.isPresent());
        final AppSecrets recoveredBean = serviceSecretsOptional.get();
        assertEquals("appKey-1-Host-1", recoveredBean.getKey());
        assertTrue(recoveredBean.getSecrets().containsKey("test:secret1"));
        assertFalse(recoveredBean.getSecrets().containsKey("test:secret2"));
        assertEquals("secret1", recoveredBean.getSecrets().get("test:secret1").getString());
    }

    @Test
    public void Test_Save_Secrets_Then_Update_Single_Secret_Verify_Updated_Value() throws DotDataException, DotSecurityException {
        final AppsAPI api = APILocator.getAppsAPI();
        final User admin = TestUserUtils.getAdminUser();
        final Host host = new SiteDataGen().nextPersisted();

        //Let's create a set of secrets for a service
        final AppSecrets.Builder builder1 = new AppSecrets.Builder();
        final AppSecrets secrets1 = builder1.withKey("appKey-1-Host-1")
                .withHiddenSecret("test:secret1", "secret-1")
                .withHiddenSecret("test:secret2", "secret-2")
                .withHiddenSecret("test:secret3", "secret3")
                .withHiddenSecret("test:secret4", "secret-4")
                .build();
        //Save it
        api.saveSecrets(secrets1, host, admin);

        //Now we want to update one of the values within the secret.
        //We want to change the value from `secret3` to `secret-3` for the secret named "test:secret3"
        final Secret secret = Secret.newSecret("secret-3".toCharArray(), Type.STRING, false);
        //Update the individual secret
        api.saveSecret("appKey-1-Host-1", new Tuple2<>("test:secret3",secret), host, admin);
        //The other properties of the object should remind the same so lets verify so.
        final Optional<AppSecrets> serviceSecretsOptional = api.getSecrets("appKey-1-Host-1", host, admin);
        assertTrue(serviceSecretsOptional.isPresent());
        final AppSecrets recoveredBean = serviceSecretsOptional.get();
        assertEquals("appKey-1-Host-1", recoveredBean.getKey());

        //We didn't modify the keys just the value associated with `test:secret3`
        assertTrue(recoveredBean.getSecrets().containsKey("test:secret1"));
        assertTrue(recoveredBean.getSecrets().containsKey("test:secret2"));
        assertTrue(recoveredBean.getSecrets().containsKey("test:secret3"));
        assertTrue(recoveredBean.getSecrets().containsKey("test:secret4"));

        //now lets verify the values returned
        assertEquals("secret-1", recoveredBean.getSecrets().get("test:secret1").getString());
        assertEquals("secret-2", recoveredBean.getSecrets().get("test:secret2").getString());
        assertEquals("secret-3", recoveredBean.getSecrets().get("test:secret3").getString());
        assertEquals("secret-4", recoveredBean.getSecrets().get("test:secret4").getString());

    }

    @Test
    public void Test_Save_Secrets_Then_Delete_Single_Secret_Entry_Then_Add_It_Again_With_New_Value_Then_Verify() throws DotDataException, DotSecurityException {
        final AppsAPI api = APILocator.getAppsAPI();
        final User admin = TestUserUtils.getAdminUser();
        final Host host = new SiteDataGen().nextPersisted();

        //Let's create a set of secrets for a service
        final AppSecrets.Builder builder1 = new AppSecrets.Builder();
        final AppSecrets secrets1 = builder1.withKey("appKeyHost-1")
                .withHiddenSecret("test:secret1", "secret-1")
                .withHiddenSecret("test:secret2", "secret-2")
                .withHiddenSecret("test:secret3", "secret-3")
                .withHiddenSecret("test:secret4", "secret-4")
                .build();
        //Save it
        api.saveSecrets(secrets1, host, admin);

        api.deleteSecret("appKeyHost-1",new ImmutableSet.Builder<String>().add("test:secret3").build(), host, admin);

        //The other properties of the object should remind the same so lets verify so.
        final Optional<AppSecrets> serviceSecretsOptional1 = api.getSecrets("appKeyHost-1", host, admin);
        assertTrue(serviceSecretsOptional1.isPresent());
        final AppSecrets recoveredBean1 = serviceSecretsOptional1.get();
        assertEquals("appKeyHost-1", recoveredBean1.getKey());

        assertTrue(recoveredBean1.getSecrets().containsKey("test:secret1"));
        assertTrue(recoveredBean1.getSecrets().containsKey("test:secret2"));
        assertFalse(recoveredBean1.getSecrets().containsKey("test:secret3"));
        assertTrue(recoveredBean1.getSecrets().containsKey("test:secret4"));

        //now lets verify the values returned
        assertEquals("secret-1", recoveredBean1.getSecrets().get("test:secret1").getString());
        assertEquals("secret-2", recoveredBean1.getSecrets().get("test:secret2").getString());
        assertEquals("secret-4", recoveredBean1.getSecrets().get("test:secret4").getString());

        //Now lets re-introduce again the property we just deleted
        final Secret secret = Secret.newSecret("lol".toCharArray(), Type.STRING, false);

        //This should create again the entry we just removed.
        api.saveSecret("appKeyHost-1", new Tuple2<>("test:secret3",secret), host, admin);

        final Optional<AppSecrets> serviceSecretsOptional2 = api.getSecrets("appKeyHost-1", host, admin);
        assertTrue(serviceSecretsOptional2.isPresent());
        final AppSecrets recoveredBean2 = serviceSecretsOptional2.get();
        assertEquals("appKeyHost-1", recoveredBean2.getKey());

        assertTrue(recoveredBean2.getSecrets().containsKey("test:secret1"));
        assertTrue(recoveredBean2.getSecrets().containsKey("test:secret2"));
        assertTrue(recoveredBean2.getSecrets().containsKey("test:secret3"));
        assertTrue(recoveredBean2.getSecrets().containsKey("test:secret4"));

        //now lets verify the values returned
        assertEquals("secret-1", recoveredBean2.getSecrets().get("test:secret1").getString());
        assertEquals("secret-2", recoveredBean2.getSecrets().get("test:secret2").getString());
        assertEquals("lol", recoveredBean2.getSecrets().get("test:secret3").getString()); //<-- Here the updated value.
        assertEquals("secret-4", recoveredBean2.getSecrets().get("test:secret4").getString());
    }

    @Test(expected = DotSecurityException.class)
    public void Test_Non_Admin_User_Read_Attempt() throws DotDataException, DotSecurityException {
        final User nonAdmin = TestUserUtils.getChrisPublisherUser();
        final AppsAPI api = APILocator.getAppsAPI();
        final AppSecrets.Builder builder = new AppSecrets.Builder();
        api.saveSecrets(builder.build(), APILocator.systemHost() , nonAdmin);
    }

    @Test
    public void Test_Non_Admin_User_With_Portlet_Read_Attempt() throws DotDataException, DotSecurityException {
        final String integrationsPortletId = AppsAPIImpl.APPS_PORTLET_ID;
        final LayoutAPI layoutAPI = APILocator.getLayoutAPI();
        final Role backEndUserRole = TestUserUtils.getBackendRole();
        final Portlet portlet = getOrCreateServiceIntegrationPortlet();

        assertNotNull(portlet);
        final LayoutDataGen layoutDataGen = new LayoutDataGen();
        final Layout layout = layoutDataGen
                .portletIds(integrationsPortletId).nextPersisted();

        final RoleDataGen roleDataGen1 = new RoleDataGen();
        final Role role1 = roleDataGen1.layout(layout).nextPersisted();
        final User nonAdminUserWithAccessToPortlet = new UserDataGen().roles(role1,backEndUserRole).nextPersisted();

        assertTrue(layoutAPI.doesUserHaveAccessToPortlet(integrationsPortletId, nonAdminUserWithAccessToPortlet));

        final AppsAPI api = APILocator.getAppsAPI();
        final AppSecrets.Builder builder = new AppSecrets.Builder();
        api.saveSecrets(builder.build(), APILocator.systemHost() , nonAdminUserWithAccessToPortlet);
    }

    @DataProvider
    public static Object[] getTestCases() throws Exception {
        return new Object[]{
                new UTFCharsRangeTestCase(0, 127, "C0 Controls and Basic Latin"),
                new UTFCharsRangeTestCase(128, 255, "C1 Controls and Latin-1 Supplement"),
                new UTFCharsRangeTestCase(256, 383, "Latin Extended-A"),
                new UTFCharsRangeTestCase(384, 591, "Latin Extended-B"),
                new UTFCharsRangeTestCase(688, 767, "Spacing Modifiers"),
                new UTFCharsRangeTestCase(768, 879, "Diacritical Marks"),
                new UTFCharsRangeTestCase(880, 1023, "Greek and Coptic"),
                new UTFCharsRangeTestCase(1024, 1279, "Cyrillic Basic"),
                new UTFCharsRangeTestCase(1280, 1327, "Cyrillic Supplement"),
                new UTFCharsRangeTestCase(8192, 8303, "General Punctuation"),
                new UTFCharsRangeTestCase(8352, 8399, "Currency Symbols"),
                new UTFCharsRangeTestCase(8448, 8527, "Letterlike Symbols"),
                new UTFCharsRangeTestCase(8592, 8703, "Arrows"),
                new UTFCharsRangeTestCase(8704, 8959, "Mathematical Operators"),
                new UTFCharsRangeTestCase(9472, 9599, "Box Drawings"),
                new UTFCharsRangeTestCase(9600, 9631, "Block Elements"),
                new UTFCharsRangeTestCase(9632, 9727, "Geometric Shapes"),
                new UTFCharsRangeTestCase(9728, 9983, "Miscellaneous Symbols"),
                new UTFCharsRangeTestCase(9984, 10175, "Dingbats")
        };
    }

    

    @Test
    @UseDataProvider("getTestCases")
    public void Test_BytesToChars_No_Middle_String_Conversion(final UTFCharsRangeTestCase testCase) throws IOException {
        final AppsAPIImpl impl = new AppsAPIImpl();
        final StringBuilder stringBuilder = new StringBuilder();
        for(int i=  testCase.fromCode; i<= testCase.toCode; i++) {
            final String string = fromCharCode(i);
            stringBuilder.append(string);
        }
        final String input = stringBuilder.toString();
        Logger.info(AppsAPIImplTest.class,()->  String.format(" UTF Charset code from `%d` to `%d`  %s `%s` ",testCase.fromCode, testCase.toCode, testCase.description,input));
        final char [] chars = impl.bytesToCharArrayUTF(input.getBytes(StandardCharsets.UTF_8));
        final byte [] bytes = impl.charsToBytesUTF(chars);
        final String output = new String(bytes, StandardCharsets.UTF_8);
        assertEquals(input, output);
    }

    /**
     * https://www.w3schools.com/charsets/ref_html_utf8.asp
     * @param codePoints char code see utf char codes.
     * @return the utf string representation.
     */
    private static String fromCharCode(final int... codePoints) {
        return new String(codePoints, 0, codePoints.length);
    }

    @Test
    public void Test_Secret_Json_Serialization_No_String_Middle_Man() throws DotDataException {
        final AppsAPIImpl impl = new AppsAPIImpl();
        final AppSecrets secretsIn = new AppSecrets.Builder()
                .withKey("TheKey")
                .withHiddenSecret("hidden1", "I'm hidden")
                .withSecret("non-hidden1", "I'm not hidden")
                .withSecret("non-hidden5", RandomStringUtils.randomAlphanumeric(2337))
                .withSecret("bool1", true)
                .build();
        final char[] toJsonAsChars = impl.toJsonAsChars(secretsIn);
        final AppSecrets secretsOut = impl.readJson(toJsonAsChars);
        assertEquals(secretsIn.getKey(), secretsOut.getKey());

        assertEquals(secretsIn.getSecrets().size(), secretsOut.getSecrets().size());

        final Set<Entry<String, Secret>> secretsInEntries = secretsIn.getSecrets().entrySet();
        for (final Entry<String, Secret> entryIn : secretsInEntries) {
            assertEquals(secretsIn.getKey(), secretsOut.getKey());
            final Secret out = secretsOut.getSecrets().get(entryIn.getKey());
            assertNotNull(out);
            assertTrue(out.equals(entryIn.getValue()));//This does a deepEquals.
        }
    }

    @Test
    public void Test_Secret_Destroy_Method() {
        final AppSecrets secrets = new AppSecrets.Builder()
                .withKey("TheKey")
                .withHiddenSecret("hidden", RandomStringUtils.randomAlphanumeric(60))
                .withSecret("non-hidden1", RandomStringUtils.randomAlphanumeric(27))
                .withSecret("non-hidden5", RandomStringUtils.randomAlphanumeric(100))
                .withSecret("bool1", true)
                .build();
        secrets.destroy();
        for (final Secret secret : secrets.getSecrets().values()) {
            for (final char chr : secret.getValue()) {
                 assertEquals(chr,(char)0);
            }
        }
    }

    private Portlet getOrCreateServiceIntegrationPortlet(){
        final String integrationsPortletId = AppsAPIImpl.APPS_PORTLET_ID;
        final PortletAPI portletAPI = APILocator.getPortletAPI();
        Portlet portlet = portletAPI.findPortlet(integrationsPortletId);
        if(null == portlet) {
            final PortletDataGen portletDataGen = new PortletDataGen();
            portlet = portletDataGen.portletId(integrationsPortletId).nextPersisted();
        }
        return portlet;
    }

    static class UTFCharsRangeTestCase {

        final int fromCode;
        final int toCode;
        final String description;

        UTFCharsRangeTestCase(final int fromCode, final int toCode, final String description) {
            this.fromCode = fromCode;
            this.toCode = toCode;
            this.description = description;
        }
    }

}
