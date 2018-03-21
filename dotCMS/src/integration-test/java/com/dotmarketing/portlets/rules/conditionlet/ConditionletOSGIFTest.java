package com.dotmarketing.portlets.rules.conditionlet;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.dotcms.LicenseTestUtil;
import com.dotcms.util.IntegrationTestInitService;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.osgi.GenericBundleActivator;
import com.dotmarketing.osgi.HostActivator;
import com.dotmarketing.portlets.rules.RuleComponentInstance;
import com.dotmarketing.portlets.rules.model.ParameterModel;
import com.dotmarketing.util.Config;
import com.dotmarketing.util.Logger;
import com.dotmarketing.util.OSGIUtil;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.osgi.framework.BundleContext;

public class ConditionletOSGIFTest {

    @BeforeClass
    public static void prepare () throws Exception {
        //Setting web app environment
        IntegrationTestInitService.getInstance().init();
        LicenseTestUtil.getLicense();
        Mockito.when(Config.CONTEXT.getRealPath("/WEB-INF/felix")).thenReturn(Config.getStringProperty("context.path.felix","/WEB-INF/felix"));
        OSGIUtil.getInstance().initializeFramework(Config.CONTEXT);
    }

    @AfterClass
    public static void cleanUp() throws Exception {
        //Stopping the OSGI framework
        OSGIUtil.getInstance().stopFramework();
    }

    @Test
    public void registerRuleConditionlet_validConditionlet_success() throws Exception{
        BundleContext context = HostActivator.instance().getBundleContext();

        ConditionletActivator conditionletActivator = new ConditionletActivator();

        try {

            conditionletActivator.start(context);

            assertNotNull(APILocator.getRulesAPI().findConditionlet(UsersContinentConditionlet.class.getSimpleName()));

            conditionletActivator.stop(context);

            assertNull(APILocator.getRulesAPI().findConditionlet(UsersContinentConditionlet.class.getSimpleName()));

        } catch(Exception e) {
            Logger.error(ConditionletOSGIFTest.class, "Error starting/stopping ConditionletActivator", e);
            throw e;
        }

    }

    private static class ConditionletActivator extends GenericBundleActivator {

        @Override
        public void start(BundleContext bundleContext) throws Exception {

            //Initializing services...
            initializeServices(bundleContext);

            //Registering the Conditionlet
            registerRuleConditionlet(bundleContext, new UsersContinentConditionlet());
        }

        @Override
        public void stop(BundleContext bundleContext) throws Exception {
            unregisterConditionlets();
        }
    }


    public static class UsersContinentConditionlet extends Conditionlet<Instance> {

        private static final long serialVersionUID = 1L;

        public UsersContinentConditionlet() {
            super("User's Continent");
        }

        @Override
        public boolean evaluate(HttpServletRequest request, HttpServletResponse response, Instance instance) {
            return false;
        }

        @Override
        public Instance instanceFrom( Map<String, ParameterModel> values) {
            return null;
        }

    }

    protected static class Instance implements RuleComponentInstance{}

}
