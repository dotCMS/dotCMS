package com.dotmarketing.viewtools;

import static com.dotcms.contenttype.model.type.KeyValueContentType.MULTILINGUABLE_FALLBACK_KEY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.dotcms.IntegrationTestBase;
import com.dotcms.contenttype.business.ContentTypeAPI;
import com.dotcms.contenttype.model.type.ContentType;
import com.dotcms.contenttype.model.type.ContentTypeBuilder;
import com.dotcms.contenttype.model.type.KeyValueContentType;
import com.dotcms.integrationtestutil.content.ContentUtils;
import com.dotcms.keyvalue.model.KeyValue;
import com.dotcms.languagevariable.business.LanguageVariableAPI;
import com.dotcms.util.IntegrationTestInitService;
import com.dotmarketing.beans.Host;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.portlets.languagesmanager.model.Language;
import com.dotmarketing.util.Config;
import com.dotmarketing.util.UtilMethods;
import com.liferay.portal.model.User;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.velocity.tools.view.context.ViewContext;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Unit test for {@link UtilMethods}
 */
public class LanguageWebAPITest extends IntegrationTestBase {

	private static Language spanishNoCountryLanguage;
	private static Language spanishCostaRicaLanguage;

	private static String LANGUAGE_KEY_TEST_EXAMPLE = "test.example";
	private static final String LANGUAGE_VALUE_TEST_EXAMPLE_NO_COUNTRY = "Value for NO country language";
	private static final String LANGUAGE_VALUE_TEST_EXAMPLE_COUNTRY = "Value for COUNTRY language";
	private static final String LANGUAGE_VALUE_TEST_EXAMPLE_DEFAULT_LANGUAGE = "Value for DEFAULT language";

	@BeforeClass
	public static void prepare() throws Exception {

		LANGUAGE_KEY_TEST_EXAMPLE += "_" + System.currentTimeMillis();

		//Setting web app environment
		IntegrationTestInitService.getInstance().init();
		final User systemUser = APILocator.systemUser();
		final String contentTypeVelocityVarName = LanguageVariableAPI.LANGUAGEVARIABLE;

		ContentType languageVariableContentType;
		try {
			// Using the provided Language Variable Content Type
			languageVariableContentType = APILocator.getContentTypeAPI(systemUser).find(contentTypeVelocityVarName);
		} catch (Exception e) {
			// Content Type not found, then create it
			languageVariableContentType = createLanguageVariable(systemUser, contentTypeVelocityVarName);
		}
		Assert.assertNotNull("The Language Variable Content Type MUST EXIST in order to run this Integration Test.",
				languageVariableContentType);

		//Create the test languages
		createSpanishNoCountryLanguage ();
		createSpanishCostaRicaLanguage ();

		//Create the test language keys

		//DEFAULT LANGUAGE
		//Search for the default language
		Long englishLanguageId = APILocator.getLanguageAPI().getDefaultLanguage().getId();
		KeyValue keyValue = APILocator.getKeyValueAPI()
				.get(LANGUAGE_KEY_TEST_EXAMPLE, englishLanguageId,
						languageVariableContentType, systemUser, false);
		String defaultLanguageVariable = (null != keyValue) ? keyValue.getValue() : null;
		if (null == defaultLanguageVariable || defaultLanguageVariable
				.equals(LANGUAGE_KEY_TEST_EXAMPLE)) {
			ContentUtils.createTestKeyValueContent(LANGUAGE_KEY_TEST_EXAMPLE,
					LANGUAGE_VALUE_TEST_EXAMPLE_DEFAULT_LANGUAGE, englishLanguageId,
					languageVariableContentType, systemUser);
		}

		//NO COUNTRY
		keyValue = APILocator.getKeyValueAPI()
				.get(LANGUAGE_KEY_TEST_EXAMPLE, spanishNoCountryLanguage.getId(),
						languageVariableContentType, systemUser, false);
		String noCountryLanguageVariable = (null != keyValue) ? keyValue.getValue() : null;
		if (null == noCountryLanguageVariable || noCountryLanguageVariable
				.equals(LANGUAGE_KEY_TEST_EXAMPLE)) {
			ContentUtils.createTestKeyValueContent(LANGUAGE_KEY_TEST_EXAMPLE,
					LANGUAGE_VALUE_TEST_EXAMPLE_NO_COUNTRY, spanishNoCountryLanguage.getId(),
					languageVariableContentType, systemUser);
		}

		//COUNTRY
		keyValue = APILocator.getKeyValueAPI()
				.get(LANGUAGE_KEY_TEST_EXAMPLE, spanishCostaRicaLanguage.getId(),
						languageVariableContentType, systemUser, false);
		String countryLanguageVariable = (null != keyValue) ? keyValue.getValue() : null;
		if (null == countryLanguageVariable || countryLanguageVariable
				.equals(LANGUAGE_KEY_TEST_EXAMPLE)) {
			ContentUtils.createTestKeyValueContent(LANGUAGE_KEY_TEST_EXAMPLE,
					LANGUAGE_VALUE_TEST_EXAMPLE_COUNTRY, spanishCostaRicaLanguage.getId(),
					languageVariableContentType, systemUser);
		}

		//Validate the just created Language Variables
		defaultLanguageVariable = APILocator.getLanguageVariableAPI()
				.get(LANGUAGE_KEY_TEST_EXAMPLE, englishLanguageId, systemUser,
						false);
		assertNotNull(defaultLanguageVariable);
		assertNotEquals(defaultLanguageVariable, LANGUAGE_KEY_TEST_EXAMPLE);
		assertEquals(defaultLanguageVariable, LANGUAGE_VALUE_TEST_EXAMPLE_DEFAULT_LANGUAGE);

		noCountryLanguageVariable = APILocator.getLanguageVariableAPI()
				.get(LANGUAGE_KEY_TEST_EXAMPLE, spanishNoCountryLanguage.getId(), systemUser,
						false);
		assertNotNull(noCountryLanguageVariable);
		assertNotEquals(noCountryLanguageVariable, LANGUAGE_KEY_TEST_EXAMPLE);
		assertEquals(noCountryLanguageVariable, LANGUAGE_VALUE_TEST_EXAMPLE_NO_COUNTRY);

		countryLanguageVariable = APILocator.getLanguageVariableAPI()
				.get(LANGUAGE_KEY_TEST_EXAMPLE, spanishCostaRicaLanguage.getId(), systemUser,
						false);
		assertNotNull(countryLanguageVariable);
		assertNotEquals(countryLanguageVariable, LANGUAGE_KEY_TEST_EXAMPLE);
		assertEquals(countryLanguageVariable, LANGUAGE_VALUE_TEST_EXAMPLE_COUNTRY);
	}

	private static ContentType createLanguageVariable (final User systemUser,
			final String contentTypeVelocityVarName) throws Exception {

		final String contentTypeName = "Language Variable";
		final Host site = APILocator.getHostAPI().findDefaultHost(systemUser, Boolean.FALSE);
		final ContentTypeAPI contentTypeApi = APILocator.getContentTypeAPI(systemUser);
		ContentType languageVariableContentType = ContentTypeBuilder.builder(KeyValueContentType.class).host(site.getIdentifier())
				.description("Testing the Language Variable API.").name(contentTypeName)
				.variable(contentTypeVelocityVarName).fixed(Boolean.FALSE).owner(systemUser.getUserId()).build();
		return  contentTypeApi.save(languageVariableContentType);
	}

	private static void createSpanishNoCountryLanguage() {

		Language language;

		try {
			language =
					APILocator.getLanguageAPI().getLanguage("es", "");
		} catch (Exception e) {

			language = null;
		}

		if (null == language) {

			language = new Language();
			language.setLanguageCode("es");
			APILocator.getLanguageAPI().saveLanguage(language);
		}
		spanishNoCountryLanguage =
				APILocator.getLanguageAPI().getLanguage("es", "");
	}

	private static void createSpanishCostaRicaLanguage() {

		Language language;

		try {
			language =
					APILocator.getLanguageAPI().getLanguage("es", "CR");
		} catch (Exception e) {

			language = null;
		}

		if (null == language) {

			language = new Language();
			language.setLanguageCode("es");
			language.setCountryCode("CR");
			APILocator.getLanguageAPI().saveLanguage(language);
		}
		spanishCostaRicaLanguage =
				APILocator.getLanguageAPI().getLanguage("es", "CR");
	}

	@Test
	public void testLangNoCountryDefaultRequestLocaleGet() {
		testNoCountryLanguage("es", "CR",
				Boolean.FALSE, LANGUAGE_VALUE_TEST_EXAMPLE_COUNTRY,
				spanishNoCountryLanguage.getId());
	}

	@Test
	public void testLangNoCountryDefaultRequestLocaleGetWithFallback() {
		testNoCountryLanguage("es", "CR",
				Boolean.TRUE, LANGUAGE_VALUE_TEST_EXAMPLE_COUNTRY,
				spanishNoCountryLanguage.getId());
	}

	@Test
	public void testDefaultContentToDefaultLanguage() {
		testNoCountryLanguage("fr", "FR",
				Boolean.TRUE, LANGUAGE_VALUE_TEST_EXAMPLE_DEFAULT_LANGUAGE,
				8985);//Non existing language
	}

	@Test
	public void testDefaultContentToDefaultLanguage2() {
		testNoCountryLanguage("fr", "FR",
				Boolean.FALSE, LANGUAGE_KEY_TEST_EXAMPLE,
				8985);//Non existing language
	}

	@Test
	public void testDefaultContentToDefaultLanguage3() {
		testNoCountryLanguage("fr", "FR",
				Boolean.TRUE, LANGUAGE_VALUE_TEST_EXAMPLE_NO_COUNTRY,
				spanishNoCountryLanguage.getId());
	}

	@Test
	public void testDefaultContentToDefaultLanguage4() {
		testNoCountryLanguage("fr", "FR",
				Boolean.FALSE, LANGUAGE_VALUE_TEST_EXAMPLE_NO_COUNTRY,
				spanishNoCountryLanguage.getId());
	}

	@Test
	public void testSimpleGetLocaleNoCountry() {
		testNoCountryLanguage("es", "",
				Boolean.FALSE, LANGUAGE_VALUE_TEST_EXAMPLE_NO_COUNTRY,
				spanishNoCountryLanguage.getId());
	}

	@Test
	public void testSimpleGetLocaleNoCountryWithFallback() {
		testNoCountryLanguage("es", "",
				Boolean.TRUE, LANGUAGE_VALUE_TEST_EXAMPLE_NO_COUNTRY,
				spanishNoCountryLanguage.getId());
	}

	private void testNoCountryLanguage(String languageCode, String countryCode, Boolean fallback,
			String value, long requestLanguageId) {
		try {
			final LanguageWebAPI languageWebAPI = new LanguageWebAPI();
			final HttpServletRequest request = mock(HttpServletRequest.class);
			final HttpSession session = mock(HttpSession.class);
			final ViewContext viewContext = mock(ViewContext.class);

			when(viewContext.getRequest()).thenReturn(request);
			when(request.getSession()).thenReturn(session);
			when(request.getLocale()).thenReturn(new Locale(languageCode, countryCode));
			when(session.getAttribute(com.dotmarketing.util.WebKeys.HTMLPAGE_LANGUAGE))
					.thenReturn(String.valueOf(requestLanguageId));

			Config.setProperty(MULTILINGUABLE_FALLBACK_KEY, fallback);

			languageWebAPI.init(viewContext);
			final String text = languageWebAPI.get(LANGUAGE_KEY_TEST_EXAMPLE);

			assertEquals(value, text);
		} finally {
			//Clean up
			Config.setProperty(MULTILINGUABLE_FALLBACK_KEY, Boolean.FALSE);
		}
	}

}