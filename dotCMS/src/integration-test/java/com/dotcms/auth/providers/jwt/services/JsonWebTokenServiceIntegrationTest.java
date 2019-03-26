package com.dotcms.auth.providers.jwt.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.dotcms.auth.providers.jwt.beans.JWToken;
import com.dotcms.auth.providers.jwt.beans.UserToken;
import com.dotcms.auth.providers.jwt.factories.JsonWebTokenFactory;
import com.dotcms.datagen.UserDataGen;
import com.dotcms.enterprise.cluster.ClusterFactory;
import com.dotcms.util.IntegrationTestInitService;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.exception.DotSecurityException;
import com.dotmarketing.util.DateUtil;
import com.google.common.collect.ImmutableMap;
import com.liferay.portal.model.User;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.IncorrectClaimException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class JsonWebTokenServiceIntegrationTest {

  private static JsonWebTokenService jsonWebTokenService;
  private static String userId;
  final String jwtId = "jwt1";
  private static String clusterId;

  @BeforeClass
  public static void prepare() throws Exception {
    // Setting web app environment
    IntegrationTestInitService.getInstance().init();

    // Mocking data
    clusterId = ClusterFactory.getClusterId();

    // Generate the token service
    jsonWebTokenService = JsonWebTokenFactory.getInstance().getJsonWebTokenService();
    assertNotNull(jsonWebTokenService);

    // Create User
    final User newUser = new UserDataGen().nextPersisted();
    APILocator.getRoleAPI().addRoleToUser(APILocator.getRoleAPI().loadCMSAdminRole(), newUser);
    assertTrue(APILocator.getUserAPI().isCMSAdmin(newUser));
    userId = newUser.getUserId();
  }

  /** Testing the generateToken JsonWebTokenServiceTest */
  @Test
  public void generateTokenTest() {

    // Generate a new token
    String jsonWebToken =
        jsonWebTokenService.generateUserToken(
            new UserToken(jwtId, userId, new Date(), DateUtil.daysToMillis(2)));
    System.out.println(jsonWebToken);
    assertNotNull(jsonWebToken);
    assertTrue(jsonWebToken.startsWith("eyJhbGciOiJIUzI1NiJ9"));

    // Parse the generated token
    final JWToken jwtBean = jsonWebTokenService.parseToken(jsonWebToken);
    assertNotNull(jwtBean);
    assertEquals(jwtBean.getId(), jwtId);
    assertEquals(jwtBean.getIssuer(), clusterId);
    final String subject = jwtBean.getSubject();
    assertNotNull(subject);
    assertEquals(subject, userId);
  }

  /** Testing the generateToken JsonWebTokenServiceTest */
  @Test(expected = ExpiredJwtException.class)
  public void generateToken_expired_token_Test()
      throws ParseException, DotSecurityException, DotDataException {

    // Generate a new token
    final UserToken userToken =
        new UserToken(
            jwtId,
            userId,
            clusterId,
            new Date(),
            DateUtil.addDate(new Date(), Calendar.MONTH, -2),
            ImmutableMap.of());

    final String jsonWebToken = jsonWebTokenService.generateUserToken(userToken);

    System.out.println(jsonWebToken);
    assertNotNull(jsonWebToken);
    assertTrue(jsonWebToken.startsWith("eyJhbGciOiJIUzI1NiJ9"));

    // Parse the expired token
    final JWToken jwtBean = jsonWebTokenService.parseToken(jsonWebToken);
  }

  /**
   * Testing the generateToken and parseToken but trying to simulate the use of a token in a
   * different server.
   */
  @Test(expected = IncorrectClaimException.class)
  public void generateToken_incorrect_issuer() {

    // Generate a new token
    final String jsonWebToken =
        jsonWebTokenService.generateUserToken(
            new UserToken(jwtId, userId, new Date(), DateUtil.daysToMillis(2)));
    System.out.println(jsonWebToken);
    assertNotNull(jsonWebToken);
    assertTrue(jsonWebToken.startsWith("eyJhbGciOiJIUzI1NiJ9"));

    /*
    Change the existing cluster id in order to simulate we are using the token in a
    different server.
     */
    set(jsonWebTokenService, "issuerId", "ANOTHER-CLUSTER-456");

    // Parse the generated token
    jsonWebTokenService.parseToken(jsonWebToken);
  }

  private void set(Object object, String fieldName, Object fieldValue) {
    Class<?> clazz = object.getClass();
    try {
      Field field = clazz.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(object, fieldValue);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  @After
  public void cleanUp() {
    set(jsonWebTokenService, "issuerId", clusterId);
  }
}
