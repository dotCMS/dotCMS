package com.dotcms.auth.providers.jwt.factories;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.dotcms.UnitTestBase;
import com.dotcms.auth.providers.jwt.factories.impl.SecretKeySpecFactoryImpl;
import com.dotmarketing.portlets.fileassets.business.FileAssetAPI;
import com.dotmarketing.util.Config;
import javax.servlet.ServletContext;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Jonathan Gamba 5/30/18
 */
public class SecretKeySpecFactoryTest extends UnitTestBase {

    /**
     * Verifies the behaviour generating or using the secret inside the
     * <strong>com.dotcms.auth.providers.jwt.factories.impl.SecretKeySpecFactoryImpl</strong>
     * class.
     */
    @Test
    public void readAndPrepareSecret() {

        //Mocking data
        Config.CONTEXT = mock(ServletContext.class);
        Config.CONTEXT_PATH = "/tmp";
        final FileAssetAPI fileAssetAPI = mock(FileAssetAPI.class);
        when(fileAssetAPI.getRealAssetsRootPath()).thenReturn("/tmp/assets");

        KeyFactoryUtils keyFactoryUtils = KeyFactoryUtils.getInstance(fileAssetAPI);

        //First make sure to delete any existing secret file
        cleanUp(keyFactoryUtils);

        //----------------------------------
        // 1) NO setting a json.web.token.hash.signing.key property
        Assert.assertFalse(keyFactoryUtils.existSecretFile());
        //Creating a new instance of the JWT service
        new JsonWebTokenFactory("").getJsonWebTokenService();
        Assert.assertTrue(keyFactoryUtils.existSecretFile());
        String secret1 = keyFactoryUtils.readSecretFromDisk();
        Assert.assertNotNull(secret1);
        System.out.println(secret1);
        //Clean up
        cleanUp(keyFactoryUtils);

        //----------------------------------
        // 2) Setting a json.web.token.hash.signing.key property
        Assert.assertFalse(keyFactoryUtils.existSecretFile());
        String mySecretKey2 = "MySecretKey2";
        Config.setProperty("json.web.token.hash.signing.key", mySecretKey2);
        //Creating a new instance of the JWT service
        new JsonWebTokenFactory("").getJsonWebTokenService();
        Assert.assertTrue(keyFactoryUtils.existSecretFile());
        //Make sure we wrote properly the secret
        String secret2 = keyFactoryUtils.readSecretFromDisk();
        Assert.assertNotNull(secret2);
        System.out.println(secret2);
        Assert.assertEquals(secret2, mySecretKey2);

        //----------------------------------
        // 3) Setting a new json.web.token.hash.signing.key property with an existing secret file
        Assert.assertTrue(keyFactoryUtils.existSecretFile());
        String oldSecret = keyFactoryUtils.readSecretFromDisk();
        Assert.assertEquals(mySecretKey2, oldSecret);
        String mySecretKey3 = "MySecretKey3";
        Config.setProperty("json.web.token.hash.signing.key", mySecretKey3);
        //Creating a new instance of the JWT service
        new JsonWebTokenFactory("").getJsonWebTokenService();
        Assert.assertTrue(keyFactoryUtils.existSecretFile());
        //Make sure we wrote properly the secret
        String secret3 = keyFactoryUtils.readSecretFromDisk();
        Assert.assertNotNull(secret3);
        System.out.println(secret3);
        Assert.assertEquals(secret3, mySecretKey3);

        //----------------------------------
        // 4) NO Setting a json.web.token.hash.signing.key property with an existing secret file
        Assert.assertTrue(keyFactoryUtils.existSecretFile());
        oldSecret = keyFactoryUtils.readSecretFromDisk();
        Assert.assertEquals(mySecretKey3, oldSecret);
        //Creating a new instance of the JWT service
        new JsonWebTokenFactory("").getJsonWebTokenService();
        Assert.assertTrue(keyFactoryUtils.existSecretFile());
        //Make sure we wrote properly the secret
        String secret4 = keyFactoryUtils.readSecretFromDisk();
        Assert.assertNotNull(secret4);
        System.out.println(secret4);
        Assert.assertEquals(secret4, mySecretKey3);
        //Clean up
        cleanUp(keyFactoryUtils);

        //----------------------------------
        // 5) Again, NO property and NO file
        Assert.assertFalse(keyFactoryUtils.existSecretFile());
        //Creating a new instance of the JWT service
        new JsonWebTokenFactory("").getJsonWebTokenService();
        Assert.assertTrue(keyFactoryUtils.existSecretFile());
        String secret5 = keyFactoryUtils.readSecretFromDisk();
        Assert.assertNotNull(secret5);
        System.out.println(secret5);
        Assert.assertNotEquals(secret5, mySecretKey3);
        //Clean up
        cleanUp(keyFactoryUtils);

        //----------------------------------
        // 6) Setting the default value to the json.web.token.hash.signing.key property and NO file
        Assert.assertFalse(keyFactoryUtils.existSecretFile());
        String mySecretKey6 = SecretKeySpecFactoryImpl.DEFAULT_SECRET;
        Config.setProperty("json.web.token.hash.signing.key", mySecretKey6);
        //Creating a new instance of the JWT service
        new JsonWebTokenFactory("").getJsonWebTokenService();
        Assert.assertTrue(keyFactoryUtils.existSecretFile());
        //Make sure we wrote properly the secret
        String secret6 = keyFactoryUtils.readSecretFromDisk();
        Assert.assertNotNull(secret6);
        System.out.println(secret6);
        Assert.assertNotEquals(secret6, mySecretKey6);
        //Clean up
        cleanUp(keyFactoryUtils);

        //----------------------------------
        // 7) Setting the default value to the file
        Assert.assertFalse(keyFactoryUtils.existSecretFile());
        String mySecretKey7 = SecretKeySpecFactoryImpl.DEFAULT_SECRET;
        keyFactoryUtils.writeSecretToDisk(mySecretKey7);
        //Creating a new instance of the JWT service
        new JsonWebTokenFactory("").getJsonWebTokenService();
        Assert.assertTrue(keyFactoryUtils.existSecretFile());
        //Make sure we wrote properly the secret
        String secret7 = keyFactoryUtils.readSecretFromDisk();
        Assert.assertNotNull(secret7);
        System.out.println(secret7);
        Assert.assertNotEquals(secret7, mySecretKey7);

        //Clean up
        cleanUp(keyFactoryUtils);
    }

    private void cleanUp(KeyFactoryUtils keyFactoryUtils) {

        Config.setProperty("json.web.token.hash.signing.key",
                SecretKeySpecFactoryImpl.DEFAULT_SECRET);

        if (keyFactoryUtils.existSecretFile()) {
            keyFactoryUtils.getSecretFile().delete();
        }
        if (keyFactoryUtils.getSecretFolder().exists()) {
            keyFactoryUtils.getSecretFolder().delete();
        }
    }

}