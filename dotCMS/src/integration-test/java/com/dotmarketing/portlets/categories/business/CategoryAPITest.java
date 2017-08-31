package com.dotmarketing.portlets.categories.business;

import com.dotcms.IntegrationTestBase;
import com.dotcms.util.IntegrationTestInitService;
import com.dotmarketing.beans.Host;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.business.CacheLocator;
import com.dotmarketing.business.PermissionAPI;
import com.dotmarketing.db.HibernateUtil;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.exception.DotSecurityException;
import com.dotmarketing.portlets.categories.model.Category;
import com.dotmarketing.portlets.contentlet.business.ContentletAPI;
import com.dotmarketing.portlets.contentlet.business.HostAPI;
import com.dotmarketing.portlets.contentlet.model.Contentlet;
import com.dotmarketing.portlets.folders.model.Folder;
import com.dotmarketing.portlets.structure.factories.FieldFactory;
import com.dotmarketing.portlets.structure.factories.StructureFactory;
import com.dotmarketing.portlets.structure.model.Field;
import com.dotmarketing.portlets.structure.model.Structure;
import com.liferay.portal.model.User;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Jonathan Gamba
 * Date: 4/8/13
 */
public class CategoryAPITest extends IntegrationTestBase {

    private static User user;
    private static Host defaultHost;

    @BeforeClass
    public static void prepare () throws Exception {

        //Setting web app environment
        IntegrationTestInitService.getInstance().init();

        HostAPI hostAPI = APILocator.getHostAPI();

        //Setting the test user
        user = APILocator.getUserAPI().getSystemUser();
        defaultHost = hostAPI.findDefaultHost( user, false );
    }


    /**
     * Testing {@link CategoryAPI#findTopLevelCategories(User, boolean, int, int, String, String)},
     * {@link CategoryAPI#findTopLevelCategories(User, boolean)} and {@link CategoryAPI#findTopLevelCategories(User, boolean, String)}
     *
     * @throws DotSecurityException
     * @throws DotDataException
     */
    @Test
    public void findTopLevelCategories() throws DotSecurityException, DotDataException {

        CategoryAPI categoryAPI = APILocator.getCategoryAPI();

        //***************************************************************
        int start = 0;
        int count = 10;//TODO: A -1 or 0 wont work in order to request all que records
        String filter = null;
        String sort = null;

        //Test the category API
        PaginatedCategories categories = categoryAPI.findTopLevelCategories(user, false, start, count, filter, sort);

        //Apply some validations
        assertNotNull(categories.getCategories());
        assertFalse(categories.getCategories().isEmpty());
        assertTrue(categories.getTotalCount() > 0);

        //***************************************************************
        filter = "event";
        sort = null;

        //Test the category API
        categories = categoryAPI.findTopLevelCategories(user, false, start, count, filter, sort);

        //Apply some validations
        assertNotNull(categories.getCategories());
        assertFalse(categories.getCategories().isEmpty());
        assertTrue(categories.getTotalCount() > 0);

        //***************************************************************
        filter = null;
        sort = "mod_date";

        //Test the category API
        categories = categoryAPI.findTopLevelCategories(user, false, start, count, filter, sort);

        //Apply some validations
        assertNotNull(categories.getCategories());
        assertFalse(categories.getCategories().isEmpty());
        assertTrue(categories.getTotalCount() > 0);

        //***************************************************************
        filter = null;

        //Test the category API
        List<Category> categoriesList = categoryAPI.findTopLevelCategories(user, false, filter);

        //Apply some validations
        assertNotNull(categoriesList);
        assertFalse(categoriesList.isEmpty());
        assertTrue(categoriesList.size() > 0);

        //***************************************************************
        //Test the category API
        categoriesList = categoryAPI.findTopLevelCategories(user, false);

        //Apply some validations
        assertNotNull(categoriesList);
        assertFalse(categoriesList.isEmpty());
        assertTrue(categoriesList.size() > 0);
    }

    /**
     * Testing {@link CategoryAPI#findChildren(User, String, boolean, int, int, String, String)} and
     * {@link CategoryAPI#findChildren(User, String, boolean, String)}
     *
     * @throws DotSecurityException
     * @throws DotDataException
     */
    @Test
    public void findChildren() throws DotSecurityException, DotDataException {

        CategoryAPI categoryAPI = APILocator.getCategoryAPI();

        //Find a parent category
        PaginatedCategories categories = categoryAPI.findTopLevelCategories(user, false, 0, 10, "event", null);

        //Apply some validations
        assertNotNull(categories.getCategories());
        assertFalse(categories.getCategories().isEmpty());
        assertTrue(categories.getTotalCount() > 0);

        String inode = categories.getCategories().get(0).getInode();

        //***************************************************************
        int start = 0;
        int count = 10;//TODO: A -1 or 0 wont work in order to request all que records
        String filter = null;
        String sort = null;

        //Test the category API
        categories = categoryAPI.findChildren(user, inode, false, start, count, filter, sort);

        //Apply some validations
        assertNotNull(categories.getCategories());
        assertFalse(categories.getCategories().isEmpty());
        assertTrue(categories.getTotalCount() > 0);

        //***************************************************************
        filter = "release";
        sort = null;

        //Test the category API
        categories = categoryAPI.findChildren(user, inode, false, start, count, filter, sort);

        //Apply some validations
        assertNotNull(categories.getCategories());
        assertFalse(categories.getCategories().isEmpty());
        assertTrue(categories.getTotalCount() > 0);

        //***************************************************************
        filter = null;
        sort = "mod_date";

        //Test the category API
        categories = categoryAPI.findChildren(user, inode, false, start, count, filter, sort);

        //Apply some validations
        assertNotNull(categories.getCategories());
        assertFalse(categories.getCategories().isEmpty());
        assertTrue(categories.getTotalCount() > 0);

        //***************************************************************
        filter = "release";

        //Test the category API
        List<Category> categoriesList = categoryAPI.findChildren(user, inode, false, filter);

        //Apply some validations
        assertNotNull(categoriesList);
        assertFalse(categoriesList.isEmpty());
        assertTrue(categoriesList.size() > 0);
    }

    /**
     * Testing {@link CategoryAPI#getParents(Categorizable, com.liferay.portal.model.User, boolean)}
     *
     * @throws Exception
     * @see CategoryAPI
     * @see Category
     */
    @Test
    public void getParents () throws Exception {

        Long time = new Date().getTime();

        CategoryAPI categoryAPI = APILocator.getCategoryAPI();
        PermissionAPI permissionAPI = APILocator.getPermissionAPI();
        ContentletAPI contentletAPI = APILocator.getContentletAPI();

        List<Category> categories = new ArrayList<Category>();

        //***************************************************************
        //Creating new categories

        HibernateUtil.startTransaction();

        //Adding the parent category
        Category parentCategory = new Category();
        parentCategory.setCategoryName( "Movies" + time );
        parentCategory.setKey( "movies" + time );
        parentCategory.setCategoryVelocityVarName( "movies" + time );
        parentCategory.setSortOrder( (String) null );
        parentCategory.setKeywords( null );
        //Saving it
        categoryAPI.save( null, parentCategory, user, false );

        //Creating child categories
        //New Child category
        Category childCategory1 = new Category();
        childCategory1.setCategoryName( "Action" + time );
        childCategory1.setKey( "action" + time );
        childCategory1.setCategoryVelocityVarName( "action" + time );
        childCategory1.setSortOrder( (String) null );
        childCategory1.setKeywords( null );
        //Saving it
        categoryAPI.save( parentCategory, childCategory1, user, false );
        categories.add( childCategory1 );
        //New Child category
        Category childCategory2 = new Category();
        childCategory2.setCategoryName( "Drama" + time );
        childCategory2.setKey( "drama" + time );
        childCategory2.setCategoryVelocityVarName( "drama" + time );
        childCategory2.setSortOrder( (String) null );
        childCategory2.setKeywords( null );
        //Saving it
        categoryAPI.save( parentCategory, childCategory2, user, false );
        categories.add( childCategory2 );

        HibernateUtil.commitTransaction();

        //***************************************************************
        //Verify If we find the parent for the categories we just added categories
        List<Category> parents = categoryAPI.getParents( childCategory1, user, false );
        assertNotNull( parents );
        assertTrue( parents.size() > 0 );
        assertEquals( parents.get( 0 ), parentCategory );

        parents = categoryAPI.getParents( childCategory2, user, false );
        assertNotNull( parents );
        assertTrue( parents.size() > 0 );
        assertEquals( parents.get( 0 ), parentCategory );

        //***************************************************************
        //Set up a new structure with categories

        HibernateUtil.startTransaction();

        //Create the new structure
        Structure testStructure = createStructure( "JUnit Test Categories Structure_" + String.valueOf( new Date().getTime() ), "junit_test_categories_structure_" + String.valueOf( new Date().getTime() ) );
        //Add a Text field
        Field textField = new Field( "JUnit Test Text", Field.FieldType.TEXT, Field.DataType.TEXT, testStructure, false, true, true, 1, false, false, false );
        FieldFactory.saveField( textField );
        //Add a Category field
        Field categoryField = new Field( "JUnit Movies", Field.FieldType.CATEGORY, Field.DataType.TEXT, testStructure, true, true, true, 2, false, false, true );
        categoryField.setValues( parentCategory.getInode() );
        FieldFactory.saveField( categoryField );

        //***************************************************************
        //Set up a content for the categories structure
        Contentlet contentlet = new Contentlet();
        contentlet.setStructureInode( testStructure.getInode() );
        contentlet.setHost( defaultHost.getIdentifier() );
        contentlet.setLanguageId( APILocator.getLanguageAPI().getDefaultLanguage().getId() );

        //Validate if the contenlet is OK
        contentletAPI.validateContentlet( contentlet, categories );

        //Saving the contentlet
        contentlet = APILocator.getContentletAPI().checkin( contentlet, categories, permissionAPI.getPermissions( contentlet, false, true ), user, false );
        APILocator.getContentletAPI().isInodeIndexed( contentlet.getInode() );
        APILocator.getVersionableAPI().setLive( contentlet );

        HibernateUtil.commitTransaction();

        //***************************************************************
        //Verify If we find the parent for these categories
        parents = categoryAPI.getParents( contentlet, user, false );
        assertNotNull( parents );
        assertTrue( parents.size() == 2 );
    }

    /**
     * This method will focus on testing the Category cache
     *
     * @throws Exception
     * @see CategoryAPI
     * @see Category
     * @see CategoryCache
     */
    @Test
    public void verifyCache () throws Exception {

        Long time = new Date().getTime();

        CategoryAPI categoryAPI = APILocator.getCategoryAPI();
        CategoryCache categoryCache = CacheLocator.getCategoryCache();

        List<Category> categories = new ArrayList<Category>();

        //***************************************************************
        //Creating new categories

        HibernateUtil.startTransaction();

        //---------------------------------------------------------------
        //Adding the parent category
        Category parentCategory = new Category();
        parentCategory.setCategoryName( "Movies" + time );
        parentCategory.setKey( "movies" + time );
        parentCategory.setCategoryVelocityVarName( "movies" + time );
        parentCategory.setSortOrder( (String) null );
        parentCategory.setKeywords( null );
        //Saving it
        categoryAPI.save( null, parentCategory, user, false );

        //Verify the cache -> THE SAVE SHOULD ADD NOTHING TO CACHE, JUST THE LOAD
        Category cachedCategory = categoryCache.get( parentCategory.getCategoryId() );
        assertNull( cachedCategory );
        //The find should add the category to the cache
        Category foundCategory = categoryAPI.find( parentCategory.getCategoryId(), user, false );
        assertNotNull( foundCategory );
        //Now it should be in cache
        cachedCategory = categoryCache.get( parentCategory.getCategoryId() );
        assertNotNull( cachedCategory );
        assertEquals( cachedCategory, parentCategory );

        //---------------------------------------------------------------
        //Creating child categories

        //New Child category
        Category childCategory1 = new Category();
        childCategory1.setCategoryName( "Action" + time );
        childCategory1.setKey( "action" + time );
        childCategory1.setCategoryVelocityVarName( "action" + time );
        childCategory1.setSortOrder( (String) null );
        childCategory1.setKeywords( null );
        //Saving it
        categoryAPI.save( parentCategory, childCategory1, user, false );
        categories.add( childCategory1 );

        //Verify the cache -> THE SAVE SHOULD ADD NOTHING TO CACHE, JUST THE LOAD
        cachedCategory = categoryCache.get( childCategory1.getCategoryId() );
        assertNull( cachedCategory );
        //The find should add the category to the cache
        foundCategory = categoryAPI.find( childCategory1.getCategoryId(), user, false );
        assertNotNull( foundCategory );
        //Now it should be in cache
        cachedCategory = categoryCache.get( childCategory1.getCategoryId() );
        assertNotNull( cachedCategory );
        assertEquals( cachedCategory, childCategory1 );

        //---------------------------------------------------------------
        //New Child category
        Category childCategory2 = new Category();
        childCategory2.setCategoryName( "Drama" + time );
        childCategory2.setKey( "drama" + time );
        childCategory2.setCategoryVelocityVarName( "drama" + time );
        childCategory2.setSortOrder( (String) null );
        childCategory2.setKeywords( null );
        //Saving it
        categoryAPI.save( parentCategory, childCategory2, user, false );
        categories.add( childCategory2 );

        //SUB-CATEGORY: Adding another level
        Category subCategory = new Category();
        subCategory.setCategoryName( "Drama_Sublevel1" + time );
        subCategory.setKey( "drama_Sublevel1" + time );
        subCategory.setCategoryVelocityVarName( "dramaSubLevel1" + time );
        subCategory.setSortOrder( (String) null );
        subCategory.setKeywords( null );
        //Saving it
        categoryAPI.save( childCategory2, subCategory, user, false );

        HibernateUtil.commitTransaction();

        //***************************************************************

        //PARENT CATEGORY
        //Verify If we find the children for the parent category we just added categories
        List<Category> cachedCategories = categoryCache.getChildren( parentCategory );
        assertNull( cachedCategories );//Verify the cache -> We should have nothing on cache at this point
        List<Category> children = categoryAPI.getChildren( parentCategory, user, true );
        assertNotNull( children );
        assertTrue( children.size() > 0 );
        assertTrue( children.size() == 2 );
        //Now it should be something in cache
        cachedCategories = categoryCache.getChildren( parentCategory );
        assertNotNull( cachedCategories );
        assertTrue( cachedCategories.size() == 2 );

        //---------------------------------------------------------------
        //CATEGORY 1
        //Verify If we find the parent for the categories we just added categories
        cachedCategories = categoryCache.getParents( childCategory1 );
        assertNull( cachedCategories );//Verify the cache -> We should have nothing on cache at this point
        List<Category> parents = categoryAPI.getParents( childCategory1, user, false );
        assertNotNull( parents );
        assertTrue( parents.size() > 0 );
        assertEquals( parents.get( 0 ), parentCategory );
        //Now it should be something in cache
        cachedCategories = categoryCache.getParents( childCategory1 );
        assertNotNull( cachedCategories );
        assertTrue( cachedCategories.size() == 1 );

        //---------------------------------------------------------------
        //CATEGORY 2
        parents = categoryAPI.getParents( childCategory2, user, false );
        assertNotNull( parents );
        assertTrue( parents.size() > 0 );
        assertEquals( parents.get( 0 ), parentCategory );
        //Verify If we find the children for this child category
        cachedCategories = categoryCache.getChildren( childCategory2 );
        assertNull( cachedCategories );//Verify the cache -> We should have nothing on cache at this point
        children = categoryAPI.getChildren( childCategory2, user, true );
        assertNotNull( children );
        assertTrue( children.size() > 0 );
        assertTrue( children.size() == 1 );
        //Now it should be something in cache
        cachedCategories = categoryCache.getChildren( childCategory2 );
        assertNotNull( cachedCategories );
        assertTrue( cachedCategories.size() == 1 );

        //---------------------------------------------------------------
        //SUB-CATEGORY
        //Verify If we find the parent for the sub-category we just added
        cachedCategories = categoryCache.getParents( subCategory );
        assertNull( cachedCategories );//Verify the cache -> We should have nothing on cache at this point
        parents = categoryAPI.getParents( subCategory, user, false );
        assertNotNull( parents );
        assertTrue( parents.size() > 0 );
        assertEquals( parents.get( 0 ), childCategory2 );
        //Now it should be something in cache
        cachedCategories = categoryCache.getParents( subCategory );
        assertNotNull( cachedCategories );
        assertTrue( cachedCategories.size() == 1 );

        //***************************************************************
        //Lets add another subcategory to verify we are cleaning the caches

        //SUB-CATEGORY: Adding another subcategory
        Category subCategory2 = new Category();
        subCategory2.setCategoryName( "Drama_Sublevel1_2" + time );
        subCategory2.setKey( "drama_Sublevel1_2" + time );
        subCategory2.setCategoryVelocityVarName( "dramaSubLevel1_2" + time );
        subCategory2.setSortOrder( (String) null );
        subCategory2.setKeywords( null );
        //Saving it
        categoryAPI.save( childCategory2, subCategory2, user, false );

        //Verify the parent of the one we just saved
        parents = categoryAPI.getParents( subCategory2, user, false );
        assertNotNull( parents );
        assertTrue( parents.size() > 0 );
        assertEquals( parents.get( 0 ), childCategory2 );

        //Verify If the children list was updated
        cachedCategories = categoryCache.getChildren( childCategory2 );
        assertNull( cachedCategories );//Verify the cache -> We should have nothing on cache at this point
        children = categoryAPI.getChildren( childCategory2, user, true );
        assertNotNull( children );
        assertTrue( children.size() > 0 );
        assertTrue( children.size() == 2 );
        //Now it should be something in cache
        cachedCategories = categoryCache.getChildren( childCategory2 );
        assertNotNull( cachedCategories );
        assertTrue( cachedCategories.size() == 2 );

        //************************DELETE*********************************
        //Delete the category
        categoryAPI.delete( childCategory2, user, false );
        //Verify the cache
        cachedCategories = categoryCache.getChildren( parentCategory );
        assertNull( cachedCategories );//Verify the cache -> The delete should clean the cache
        children = categoryAPI.getChildren( parentCategory, user, true );
        assertNotNull( children );
        assertTrue( children.size() > 0 );
        assertTrue( children.size() == 1 );

        Category category = categoryCache.get( childCategory2.getCategoryId() );
        assertNull( category );//Shouldn't exits
        cachedCategories = categoryCache.getChildren( childCategory2 );
        assertNull( cachedCategories );//Shouldn't exist
    }

    @Test
    public void testSortChildren() {

        final CategoryAPI categoryAPI = APILocator.getCategoryAPI();
        final CategoryCache categoryCache = CacheLocator.getCategoryCache();

        final String categoryAKey = "categoryA";
        final String categoryBKey = "categoryB";
        final String categoryCKey = "categoryC";

        Category parentCategory = null;
        Category childCategoryA = null;
        Category childCategoryB = null;
        Category childCategoryC = null;

        try {
            //Create Parent Category.
            parentCategory = new Category();
            parentCategory.setCategoryName( "Parent Category" );
            parentCategory.setKey( "parent" );
            parentCategory.setCategoryVelocityVarName( "parent" );
            parentCategory.setSortOrder( (String) null );
            parentCategory.setKeywords( null );

            categoryAPI.save( null, parentCategory, user, false );

            Category foundCategory = categoryAPI.find( parentCategory.getCategoryId(), user, false );
            assertNotNull( foundCategory );

            //Create First Child Category.
            childCategoryA = new Category();
            childCategoryA.setCategoryName( "Category A" );
            childCategoryA.setKey( categoryAKey );
            childCategoryA.setCategoryVelocityVarName( "categoryA" );
            childCategoryA.setSortOrder( 1 );
            childCategoryA.setKeywords( null );

            categoryAPI.save( parentCategory, childCategoryA, user, false );

            foundCategory = categoryAPI.find( childCategoryA.getCategoryId(), user, false );
            assertNotNull( foundCategory );

            //Create Second Child Category.
            childCategoryB = new Category();
            childCategoryB.setCategoryName( "Category B" );
            childCategoryB.setKey( categoryBKey );
            childCategoryB.setCategoryVelocityVarName( "categoryB" );
            childCategoryB.setSortOrder( 2 );
            childCategoryB.setKeywords( null );

            categoryAPI.save( parentCategory, childCategoryB, user, false );

            foundCategory = categoryAPI.find( childCategoryB.getCategoryId(), user, false );
            assertNotNull( foundCategory );

            //Create Third Child Category.
            childCategoryC = new Category();
            childCategoryC.setCategoryName( "Category C" );
            childCategoryC.setKey( categoryCKey );
            childCategoryC.setCategoryVelocityVarName( "categoryC" );
            childCategoryC.setSortOrder( 3 );
            childCategoryC.setKeywords( null );

            categoryAPI.save( parentCategory, childCategoryC, user, false );

            foundCategory = categoryAPI.find( childCategoryC.getCategoryId(), user, false );
            assertNotNull( foundCategory );

            //Check that the original order follows SortOrder value.
            List<Category> children = categoryAPI.findChildren( user, parentCategory.getInode(), false, null );
            assertEquals( 3, children.size() );
            assertEquals( categoryAKey, children.get( 0 ).getKey() );
            assertEquals( categoryBKey, children.get( 1 ).getKey() );
            assertEquals( categoryCKey, children.get( 2 ).getKey() );

            //Reorder.
            childCategoryA.setSortOrder( 3 );
            childCategoryB.setSortOrder( 2);
            childCategoryC.setSortOrder( 1 );

            //Saving all the children.
            categoryAPI.save( parentCategory, childCategoryA, user, false );
            categoryAPI.save( parentCategory, childCategoryB, user, false );
            categoryAPI.save( parentCategory, childCategoryC, user, false );

            assertNull( categoryCache.get( childCategoryA.getCategoryId() ) );
            assertNull( categoryCache.get( childCategoryB.getCategoryId() ) );
            assertNull( categoryCache.get( childCategoryC.getCategoryId() ) );

            //This call will put the children on the cache.
            categoryAPI.sortChildren( parentCategory.getInode() );

            //Check new order.
            assertEquals( new Integer( 3 ), categoryCache.get( childCategoryA.getCategoryId() ).getSortOrder() );
            assertEquals( new Integer( 2 ), categoryCache.get( childCategoryB.getCategoryId() ).getSortOrder() );
            assertEquals( new Integer( 1 ), categoryCache.get( childCategoryC.getCategoryId() ).getSortOrder() );

        } catch ( Exception e ) {
            fail( e.getMessage() );
        } finally {
            try {
                //Deleting Child Categories.
                if ( childCategoryA != null ){
                    categoryAPI.delete( childCategoryA, user, false );
                }
                if ( childCategoryB != null ){
                    categoryAPI.delete( childCategoryB, user, false );
                }
                if ( childCategoryC != null ){
                    categoryAPI.delete( childCategoryC, user, false );
                }
                if ( parentCategory != null ){
                    //Delete Parent Category.
                    categoryAPI.delete( parentCategory, user, false );
                }
            } catch ( Exception e ){
                fail( e.getMessage() );
            }
        }
    }

    /**
     * Creates an Structure object for a later use in the tests
     *
     * @param name
     * @param structureVelocityVarName
     * @throws com.dotmarketing.exception.DotHibernateException
     *
     * @throws com.dotmarketing.exception.DotSecurityException
     *
     */
    protected static Structure createStructure ( String name, String structureVelocityVarName ) throws DotDataException, DotSecurityException {

        PermissionAPI permissionAPI = APILocator.getPermissionAPI();

        //Set up a test folder
        Folder testFolder = APILocator.getFolderAPI().createFolders( "/" + new Date().getTime() + "/", defaultHost, user, false );
        permissionAPI.permissionIndividually( permissionAPI.findParentPermissionable( testFolder ), testFolder, user, false );

        //Create the structure
        Structure testStructure = new Structure();

        testStructure.setDefaultStructure( false );
        testStructure.setDescription( "JUnit Test Structure Description." );
        testStructure.setHost( defaultHost.getIdentifier() );
        testStructure.setFolder( testFolder.getInode() );
        testStructure.setName( name );
        testStructure.setOwner( user.getUserId() );
        testStructure.setDetailPage( "" );
        testStructure.setStructureType( Structure.STRUCTURE_TYPE_CONTENT );
        testStructure.setVelocityVarName( structureVelocityVarName );

        //Saving the structure
        StructureFactory.saveStructure( testStructure );
        CacheLocator.getContentTypeCache().add( testStructure );

        return testStructure;
    }

}