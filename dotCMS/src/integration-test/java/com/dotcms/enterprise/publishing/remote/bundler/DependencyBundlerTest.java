package com.dotcms.enterprise.publishing.remote.bundler;

import com.dotcms.contenttype.model.type.ContentType;
import com.dotcms.contenttype.transform.contenttype.StructureTransformer;
import com.dotcms.datagen.*;
import com.dotcms.enterprise.rules.RulesAPIImpl;
import com.dotcms.publisher.bundle.bean.Bundle;
import com.dotcms.publisher.pusher.PushPublisherConfig;
import com.dotcms.publisher.util.DependencyManagerTest;
import com.dotcms.publishing.BundlerStatus;
import com.dotcms.publishing.DotBundleException;
import com.dotcms.publishing.FilterDescriptor;
import com.dotcms.util.IntegrationTestInitService;
import com.dotmarketing.beans.Host;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.business.CacheLocator;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.exception.DotSecurityException;
import com.dotmarketing.portlets.categories.model.Category;
import com.dotmarketing.portlets.containers.model.Container;
import com.dotmarketing.portlets.containers.model.FileAssetContainer;
import com.dotmarketing.portlets.contentlet.model.Contentlet;
import com.dotmarketing.portlets.folders.model.Folder;
import com.dotmarketing.portlets.links.model.Link;
import com.dotmarketing.portlets.rules.RuleDataGen;
import com.dotmarketing.portlets.rules.model.Condition;
import com.dotmarketing.portlets.rules.model.ConditionGroup;
import com.dotmarketing.portlets.rules.model.Rule;
import com.dotmarketing.portlets.structure.model.Structure;
import com.dotmarketing.portlets.templates.design.bean.ContainerUUID;
import com.dotmarketing.portlets.templates.design.bean.TemplateLayout;
import com.dotmarketing.portlets.templates.model.Template;
import com.dotmarketing.portlets.workflows.model.WorkflowScheme;
import com.dotmarketing.util.FileUtil;
import com.dotmarketing.util.Logger;
import com.google.common.collect.ImmutableMap;
import com.liferay.portal.model.User;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.dotcms.util.CollectionsUtils.add;
import static com.dotcms.util.CollectionsUtils.list;
import static org.jgroups.util.Util.assertEquals;
import static org.jgroups.util.Util.assertTrue;
import static org.mockito.Mockito.mock;

@RunWith(DataProviderRunner.class)
public class DependencyBundlerTest {

    private File bundleRoot = null;
    private BundlerStatus status = null;

    private DependencyBundler bundler = null;

    private static FilterDescriptor filterDescriptorAllDependencies;
    private static FilterDescriptor filterDescriptorNotDependencies;
    private static FilterDescriptor filterDescriptorNotRelationship;
    private static FilterDescriptor filterDescriptorNotDependenciesRelationship;

    public static void prepare() throws Exception {

        //Setting web app environment
        IntegrationTestInitService.getInstance().init();

        filterDescriptorAllDependencies = new FileDescriptorDataGen().nextPersisted();
        filterDescriptorNotDependencies = new FileDescriptorDataGen()
                .dependencies(false)
                .nextPersisted();

        filterDescriptorNotRelationship = new FileDescriptorDataGen()
                .relationships(false)
                .nextPersisted();

        filterDescriptorNotDependenciesRelationship = new FileDescriptorDataGen()
                .relationships(false)
                .dependencies(false)
                .nextPersisted();
    }

    @Before
    public void initTest() throws IOException {
        bundleRoot = FileUtil.createTemporaryDirectory("DependencyBundlerTest_");
        status = mock(BundlerStatus.class);

        bundler = new DependencyBundler();
    }

    @DataProvider
    public static Object[] assets() throws Exception {
        prepare();

        final ArrayList<Object> all = new ArrayList<>();
        all.addAll(createContentTypeTestCase());
        all.addAll(createTemplatesTestCase());
        all.addAll(createContainerTestCase());
        all.addAll(createFolderTestCase());
        all.addAll(createHostTestCase());

        return all.toArray();
    }

    private static Collection<TestData> createHostTestCase() throws DotDataException {
        final Host host = new SiteDataGen().nextPersisted();

        final Host hostWithTemplate = new SiteDataGen().nextPersisted();
        final Template template = new TemplateDataGen().host(hostWithTemplate).nextPersisted();

        final Host hostWithContainer = new SiteDataGen().nextPersisted();
        final Container container = new ContainerDataGen()
                .site(hostWithContainer)
                .clearContentTypes()
                .nextPersisted();

        final Host hostWithFolder = new SiteDataGen().nextPersisted();
        final Folder folder = new FolderDataGen().site(hostWithFolder).nextPersisted();

        final Host hostWithContent = new SiteDataGen().nextPersisted();
        final ContentType contentType = new ContentTypeDataGen().host(hostWithContent).nextPersisted();
        final Contentlet contentlet = new ContentletDataGen(contentType.id()).host(hostWithContent).nextPersisted();

        final Host hostWithRule = new SiteDataGen().nextPersisted();
        final Rule rule = new RuleDataGen().host(hostWithRule).nextPersisted();

        final WorkflowScheme systemWorkflowScheme = APILocator.getWorkflowAPI().findSystemWorkflowScheme();
        final Folder systemFolder = APILocator.getFolderAPI().findSystemFolder();

        final Structure folderStructure = CacheLocator.getContentTypeCache().getStructureByInode(systemFolder.getDefaultFileType());
        final ContentType folderContentType = new StructureTransformer(folderStructure).from();

        final Host systemHost = APILocator.getHostAPI().findSystemHost();

        return list(
                new TestData(list(host), list(), filterDescriptorAllDependencies),
                new TestData(list(host), list(), filterDescriptorNotDependencies),
                new TestData(list(host), list(), filterDescriptorNotRelationship),
                new TestData(list(host), list(), filterDescriptorNotDependenciesRelationship),

                new TestData(list(hostWithTemplate), list(template), filterDescriptorAllDependencies),
                new TestData(list(hostWithTemplate), list(), filterDescriptorNotDependencies),
                new TestData(list(hostWithTemplate), list(), filterDescriptorNotRelationship),
                new TestData(list(hostWithTemplate), list(), filterDescriptorNotDependenciesRelationship),

                new TestData(list(hostWithContainer), list(container), filterDescriptorAllDependencies),
                new TestData(list(hostWithContainer), list(), filterDescriptorNotDependencies),
                new TestData(list(hostWithContainer), list(), filterDescriptorNotRelationship),
                new TestData(list(hostWithContainer), list(), filterDescriptorNotDependenciesRelationship),

                new TestData(list(hostWithContent), list(contentType, contentlet, systemWorkflowScheme,systemFolder),
                        filterDescriptorAllDependencies),
                new TestData(list(hostWithContent), list(), filterDescriptorNotDependencies),
                new TestData(list(hostWithContent), list(), filterDescriptorNotRelationship),
                new TestData(list(hostWithContent), list(), filterDescriptorNotDependenciesRelationship),

                new TestData(list(hostWithFolder), list(folder, folderContentType, systemHost, systemFolder, systemWorkflowScheme),
                        filterDescriptorAllDependencies),
                new TestData(list(hostWithFolder), list(), filterDescriptorNotDependencies),
                new TestData(list(hostWithFolder), list(), filterDescriptorNotRelationship),
                new TestData(list(hostWithFolder), list(), filterDescriptorNotDependenciesRelationship),

                new TestData(list(hostWithRule), list(rule), filterDescriptorAllDependencies),
                new TestData(list(hostWithFolder), list(), filterDescriptorNotDependencies),
                new TestData(list(hostWithFolder), list(), filterDescriptorNotRelationship),
                new TestData(list(hostWithFolder), list(), filterDescriptorNotDependenciesRelationship)
        );
    }

    private static Collection<TestData> createFolderTestCase() throws DotDataException, DotSecurityException {
        final Host host = new SiteDataGen().nextPersisted();
        final Folder folder = new FolderDataGen().site(host).nextPersisted();

        final Folder parentFolder = new FolderDataGen().site(host).nextPersisted();
        final Folder folderWithParent = new FolderDataGen()
                .parent(parentFolder)
                .site(host)
                .nextPersisted();

        final Folder folderWithContentType = new FolderDataGen().site(host).nextPersisted();
        final ContentType contentType = new ContentTypeDataGen()
                .folder(folderWithContentType)
                .nextPersisted();

        final Folder folderWithContent = new FolderDataGen().site(host).nextPersisted();

        final File image = new File(Thread.currentThread().getContextClassLoader().getResource("images/test.jpg").getFile());
        final Contentlet contentlet = new FileAssetDataGen(folderWithContent, image)
                .host(host)
                .nextPersisted();

        final Folder folderWithLink = new FolderDataGen().site(host).nextPersisted();
        final Link link = new LinkDataGen(folderWithLink).nextPersisted();

        final Folder folderWithSubFolder = new FolderDataGen().site(host).nextPersisted();
        final Folder subFolder = new FolderDataGen()
                .parent(folderWithSubFolder)
                .nextPersisted();

        final Contentlet contentlet_2 = new FileAssetDataGen(subFolder, image)
                .host(host)
                .nextPersisted();

        final Host systemHost = APILocator.getHostAPI().findSystemHost();
        final Structure folderStructure = CacheLocator.getContentTypeCache()
                .getStructureByInode(folder.getDefaultFileType());

        final ContentType folderContentType = new StructureTransformer(folderStructure).from();

        final Folder systemFolder = APILocator.getFolderAPI()
                .find(folderContentType.folder(), APILocator.systemUser(), false);

        final WorkflowScheme systemWorkflowScheme = APILocator.getWorkflowAPI().findSystemWorkflowScheme();

        //Folder with sub folder
        return list(
                new TestData(list(folder), list(host, folderContentType, systemHost, systemFolder, systemWorkflowScheme),
                        filterDescriptorAllDependencies),
                new TestData(list(folder), list(), filterDescriptorNotDependencies),
                new TestData(list(folder), list(), filterDescriptorNotRelationship),
                new TestData(list(folder), list(), filterDescriptorNotDependenciesRelationship),

                //Dependency manager not add Parent Folder, the Parent Folder is added as dependency in FolderBundle
                new TestData(list(folderWithParent), list(host, folderContentType, systemHost, systemFolder, systemWorkflowScheme),
                        filterDescriptorAllDependencies),
                new TestData(list(folderWithParent), list(), filterDescriptorNotDependencies),
                new TestData(list(folderWithParent), list(), filterDescriptorNotRelationship),
                new TestData(list(folderWithParent), list(), filterDescriptorNotDependenciesRelationship),

                new TestData(list(folderWithContentType), list(host, folderContentType, systemHost, systemFolder, contentType, systemWorkflowScheme),
                        filterDescriptorAllDependencies),
                new TestData(list(folderWithContentType), list(), filterDescriptorNotDependencies),
                new TestData(list(folderWithContentType), list(), filterDescriptorNotRelationship),
                new TestData(list(folderWithContentType), list(), filterDescriptorNotDependenciesRelationship),

                new TestData(list(folderWithContent), list(host, folderContentType, systemHost, systemFolder, contentlet, systemWorkflowScheme),
                        filterDescriptorAllDependencies),
                new TestData(list(folderWithContent), list(), filterDescriptorNotDependencies),
                new TestData(list(folderWithContent), list(), filterDescriptorNotRelationship),
                new TestData(list(folderWithContent), list(), filterDescriptorNotDependenciesRelationship),

                new TestData(list(folderWithLink), list(host, folderContentType, systemHost, systemFolder, link, systemWorkflowScheme),
                        filterDescriptorAllDependencies),
                new TestData(list(folderWithLink), list(), filterDescriptorNotDependencies),
                new TestData(list(folderWithLink), list(), filterDescriptorNotRelationship),
                new TestData(list(folderWithLink), list(), filterDescriptorNotDependenciesRelationship),

                new TestData(list(folderWithSubFolder), list(host, folderContentType, systemHost, systemFolder, subFolder, contentlet_2, systemWorkflowScheme),
                        filterDescriptorAllDependencies),
                new TestData(list(folderWithSubFolder), list(), filterDescriptorNotDependencies),
                new TestData(list(folderWithSubFolder), list(), filterDescriptorNotRelationship),
                new TestData(list(folderWithSubFolder), list(), filterDescriptorNotDependenciesRelationship)
        );
    }

    private static Collection<TestData> createContainerTestCase() throws DotDataException, DotSecurityException {
        final Host host = new SiteDataGen().nextPersisted();

        final ContentType contentType = new ContentTypeDataGen().host(host).nextPersisted();
        final Container containerWithoutContentType = new ContainerDataGen()
                .site(host)
                .clearContentTypes()
                .nextPersisted();

        final Container containerWithContentType = new ContainerDataGen()
                .site(host)
                .withContentType(contentType, "")
                .nextPersisted();

        final Folder systemFolder = APILocator.getFolderAPI().findSystemFolder();
        final WorkflowScheme systemWorkflowScheme = APILocator.getWorkflowAPI().findSystemWorkflowScheme();

        return list(
                new TestData(list(containerWithoutContentType), list(host), filterDescriptorAllDependencies),
                new TestData(list(containerWithoutContentType), list(), filterDescriptorNotDependencies),
                new TestData(list(containerWithoutContentType), list(), filterDescriptorNotRelationship),
                new TestData(list(containerWithoutContentType), list(), filterDescriptorNotDependenciesRelationship),

                new TestData(list(containerWithContentType), list(host, contentType, systemWorkflowScheme, systemFolder), filterDescriptorAllDependencies),
                new TestData(list(containerWithContentType), list(), filterDescriptorNotDependencies),
                new TestData(list(containerWithContentType), list(), filterDescriptorNotRelationship),
                new TestData(list(containerWithContentType), list(), filterDescriptorNotDependenciesRelationship)
        );
    }

    private static List<TestData> createTemplatesTestCase() throws DotDataException, DotSecurityException {
        final Host host = new SiteDataGen().nextPersisted();
        final Host systemHost = APILocator.getHostAPI().findSystemHost();
        final Template advancedTemplateWithContainer = new TemplateDataGen().host(host).nextPersisted();

        final ContentType contentType = new ContentTypeDataGen().host(host).nextPersisted();

        final Container container_1 = new ContainerDataGen()
                .site(host)
                .withContentType(contentType, "")
                .nextPersisted();

        final Container container_2 = new ContainerDataGen()
                .site(host)
                .withContentType(contentType, "")
                .nextPersisted();

        final TemplateLayout templateLayout = new TemplateLayoutDataGen()
                .withContainer(container_1)
                .withContainer(container_2)
                .next();

        final Template templateWithTemplateLayout = new TemplateDataGen()
                .host(host)
                .drawedBody(templateLayout)
                .nextPersisted();

        final WorkflowScheme systemWorkflowScheme = APILocator.getWorkflowAPI().findSystemWorkflowScheme();
        final Folder systemFolder = APILocator.getFolderAPI().findSystemFolder();

        return list(
                new TestData(list(advancedTemplateWithContainer), list(host), filterDescriptorAllDependencies),
                new TestData(list(advancedTemplateWithContainer), list(), filterDescriptorNotDependencies),
                new TestData(list(advancedTemplateWithContainer), list(), filterDescriptorNotRelationship),
                new TestData(list(advancedTemplateWithContainer), list(), filterDescriptorNotDependenciesRelationship),

                new TestData(list(templateWithTemplateLayout), list(host, container_1, container_2, contentType, systemWorkflowScheme, systemFolder),
                        filterDescriptorAllDependencies),
                new TestData(list(templateWithTemplateLayout), list(), filterDescriptorNotDependencies),
                new TestData(list(templateWithTemplateLayout), list(), filterDescriptorNotRelationship),
                new TestData(list(templateWithTemplateLayout), list(), filterDescriptorNotDependenciesRelationship)
        );
    }

    @NotNull
    private static List<TestData> createContentTypeTestCase() throws DotDataException {

        final Host host = new SiteDataGen().nextPersisted();
        final ContentType contentType = new ContentTypeDataGen()
                .host(host)
                .nextPersisted();

        final Host folderHost = new SiteDataGen().nextPersisted();
        final Folder folder = new FolderDataGen().site(folderHost).nextPersisted();
        final ContentType contentTypeWithFolder = new ContentTypeDataGen()
                .folder(folder)
                .nextPersisted();

        final WorkflowScheme workflowScheme = new WorkflowDataGen().nextPersisted();
        final ContentType contentTypeWithWorkflow = new ContentTypeDataGen()
                .host(host)
                .workflowId(workflowScheme.getId())
                .nextPersisted();

        final Category category = new CategoryDataGen().nextPersisted();
        final ContentType contentTypeWithCategory=  new ContentTypeDataGen()
                .host(host)
                .addCategory(category)
                .nextPersisted();

        final ContentType contentTypeParent =  new ContentTypeDataGen()
                .host(host)
                .nextPersisted();

        final ContentType contentTypeChild =  new ContentTypeDataGen()
                .host(host)
                .nextPersisted();

        new FieldRelationshipDataGen()
                .child(contentTypeChild)
                .parent(contentTypeParent)
                .nextPersisted();

        final WorkflowScheme systemWorkflowScheme = APILocator.getWorkflowAPI().findSystemWorkflowScheme();
        final Folder systemFolder = APILocator.getFolderAPI().findSystemFolder();

        return list(
                new TestData(list(contentType), list(host, systemWorkflowScheme, systemFolder), filterDescriptorAllDependencies),
                new TestData(list(contentType), list(), filterDescriptorNotDependencies),
                new TestData(list(contentType), list(), filterDescriptorNotRelationship),
                new TestData(list(contentType), list(), filterDescriptorNotDependenciesRelationship),

                new TestData(list(contentTypeWithFolder), list(folder, systemWorkflowScheme, folderHost), filterDescriptorAllDependencies),
                new TestData(list(contentTypeWithFolder), list(), filterDescriptorNotDependencies),
                new TestData(list(contentTypeWithFolder), list(), filterDescriptorNotRelationship),
                new TestData(list(contentTypeWithFolder), list(), filterDescriptorNotDependenciesRelationship),

                new TestData(list(contentTypeWithWorkflow), list(host, systemWorkflowScheme, workflowScheme, systemFolder), filterDescriptorAllDependencies),
                new TestData(list(contentTypeWithWorkflow), list(), filterDescriptorNotDependencies),
                new TestData(list(contentTypeWithWorkflow), list(), filterDescriptorNotRelationship),
                new TestData(list(contentTypeWithWorkflow), list(), filterDescriptorNotDependenciesRelationship),

                new TestData(list(contentTypeWithCategory), list(host, systemWorkflowScheme, category, systemFolder), filterDescriptorAllDependencies),
                new TestData(list(contentTypeWithCategory), list(), filterDescriptorNotDependencies),
                new TestData(list(contentTypeWithCategory), list(), filterDescriptorNotRelationship),
                new TestData(list(contentTypeWithCategory), list(), filterDescriptorNotDependenciesRelationship),

                new TestData(list(contentTypeParent), list(host, systemWorkflowScheme, contentTypeChild, systemFolder), filterDescriptorAllDependencies),
                new TestData(list(contentTypeParent), list(), filterDescriptorNotDependencies),
                new TestData(list(contentTypeParent), list(), filterDescriptorNotRelationship),
                new TestData(list(contentTypeParent), list(), filterDescriptorNotDependenciesRelationship)
        );
    }

    /**
     * Method to Test: {@link DependencyBundler#generate(File, BundlerStatus)}
     * When: Add a ContentType without dependency
     * Should: 
     * - Add the {@link com.dotcms.contenttype.model.type.ContentType} in {@link PushPublisherConfig#setStructures(Set)}
     * - Add the ContentType's {@link com.dotmarketing.beans.Host} in {@link PushPublisherConfig#setHostSet(Set)} (List)}
     *
     */
    @Test
    @UseDataProvider("assets")
    public void addAssetInBundle(final TestData testData)
            throws IOException, DotBundleException, DotDataException {


        final PushPublisherConfig config = new PushPublisherConfig();
        new BundleDataGen()
                .pushPublisherConfig(config)
                .addAssets(testData.assetsToAddInBundle)
                .filter(testData.filterDescriptor)
                .nextPersisted();

        bundler.setConfig(config);
        bundler.generate(bundleRoot, status);

        assertAll(config, testData.dependenciesToAssert);
    }

    private void assertAll(final PushPublisherConfig config, final List<Object> dependenciesToAssert) {
        AssignableFromMap<Integer> counts = new AssignableFromMap<>();

        for (Object asset : dependenciesToAssert) {
            final BundleDataGen.MetaData metaData = BundleDataGen.howAddInBundle.get(asset.getClass());

            final String assetId = metaData.dataToAdd.apply(asset);
           assertTrue(String.format("Not Contain %s in %s", assetId, asset.getClass()),
                    metaData.collection.apply(config).contains(assetId));

            final Class key = BundleDataGen.howAddInBundle.getKey(asset.getClass());
            counts.addOrUpdate(key, 1, (Integer value) -> value + 1);
        }

       for (Class clazz : BundleDataGen.howAddInBundle.keySet()) {
            final Integer expectedCount = counts.get(clazz, 0);

            final BundleDataGen.MetaData metaData = BundleDataGen.howAddInBundle.get(clazz);
            final int count = metaData.collection.apply(config).size();

            assertEquals(String.format("Expected %d not %d to %s: %s", expectedCount, count, clazz, counts),
                    expectedCount, count);
        }
    }

    private static class TestData {
        List<Object> assetsToAddInBundle;
        List<Object> dependenciesToAssert;
        FilterDescriptor filterDescriptor;

        public TestData(
                final List<Object> assetsToAddInBundle,
                final List<Object> dependenciesToAssert,
                final FilterDescriptor filterDescriptor) {
            this.assetsToAddInBundle = assetsToAddInBundle;
            this.dependenciesToAssert = new ArrayList<>(dependenciesToAssert);
            this.dependenciesToAssert.addAll(assetsToAddInBundle);
            this.filterDescriptor = filterDescriptor;
        }
    }
}
