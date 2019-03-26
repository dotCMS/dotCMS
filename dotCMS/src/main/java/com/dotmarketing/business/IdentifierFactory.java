package com.dotmarketing.business;

import com.dotmarketing.beans.Host;
import com.dotmarketing.beans.Identifier;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.portlets.folders.model.Folder;
import java.util.List;

/**
 * Provides data source level access to information related to Identifiers in dotCMS. Every piece of
 * content you create in the application will be associated to a unique ID that never changes.
 *
 * @author root
 * @version 1.x
 * @since Mar 22, 2012
 */
public abstract class IdentifierFactory {

  /**
   * Retrieves all identifiers matching a URI pattern.
   *
   * @param assetType
   * @param uri - Can contain a * at the beginning or end.
   * @param include - Should find all that match pattern if true or all that do not match pattern if
   *     false.
   * @param site
   * @return
   * @throws DotDataException
   */
  protected abstract List<Identifier> findByURIPattern(
      final String assetType, String uri, boolean include, Host site) throws DotDataException;

  /**
   * @param webasset
   * @param folder
   * @throws DotDataException
   */
  protected abstract void updateIdentifierURI(Versionable webasset, Folder folder)
      throws DotDataException;

  /**
   * Retrieves the identifier matching the URI by looking in cache first, then in database. It will
   * load the cache for future use.
   *
   * @param site
   * @param uri
   */
  protected abstract Identifier findByURI(final Host site, String uri) throws DotDataException;

  /**
   * Retrieves the identifier matching the URI by looking in cache first, then in database. It will
   * load the cache for future use.
   *
   * @param siteId
   * @param uri
   */
  protected abstract Identifier findByURI(final String siteId, String uri) throws DotDataException;

  /**
   * Retrieves the identifier matching the URI by looking in cache. Returns null if not found.
   *
   * @param site
   * @param uri
   */
  protected abstract Identifier loadByURIFromCache(Host site, String uri);

  /**
   * Retrieves the identifier matching the URI by looking in the database. Returns null if not
   * found.
   *
   * @param identifier
   */
  protected abstract Identifier loadFromDb(String identifier)
      throws DotDataException, DotStateException;

  /**
   * Retrieves the identifier matching the URI by looking in the database. Returns null if not
   * found.
   *
   * @param versionable
   */
  protected abstract Identifier loadFromDb(Versionable versionable) throws DotDataException;

  /**
   * Retrieves the identifier matching the URI by looking in the cache. Returns null if not found.
   *
   * @param identifier
   */
  protected abstract Identifier loadFromCache(String identifier);

  /**
   * Retrieves the identifier matching the URI by looking in the cache. Returns null if not found.
   *
   * @param versionable
   */
  protected abstract Identifier loadFromCache(Versionable versionable);

  /**
   * Retrieves the identifier matching the Inode by looking in the cache. Returns null if not found.
   *
   * @param inode
   */
  protected abstract Identifier loadFromCacheFromInode(String inode);

  /**
   * Retrieves the identifier matching the URI by looking in the cache. Returns null if not found.
   *
   * @param site
   * @param uri
   */
  protected abstract Identifier loadFromCache(Host site, String uri);

  /**
   * Retrieves the identifier matching the URI by looking in cache first, then in database. It will
   * load the cache for future use.
   *
   * @param versionable
   */
  protected abstract Identifier find(Versionable versionable) throws DotDataException;

  /**
   * Retrieves the identifier matching the URI by looking in cache first, then in database. It will
   * load the cache for future use.
   *
   * @param identifier
   */
  protected abstract Identifier find(final String identifier)
      throws DotStateException, DotDataException;

  /**
   * Creates a new Identifier for a given versionable asset under a given folder. The ID value will
   * be randomly generated.
   *
   * @param webasset - The asset that will be created.
   * @param folder - The folder that the asset will be created in.
   * @return The {@link Identifier} of the new asset.
   * @throws DotDataException An error occurred when interacting with the data source.
   */
  protected abstract Identifier createNewIdentifier(Versionable webasset, Folder folder)
      throws DotDataException;

  /**
   * Creates a new Identifier for a given versionable asset under a given folder. In this method,
   * the ID value will <b>NOT</b> be randomly generated as it is specified as a parameter.
   *
   * @param webasset - The asset that will be created.
   * @param folder - The folder that the asset will be created in.
   * @param existingId - The ID of the new Identifier.
   * @return The {@link Identifier} of the new asset.
   * @throws DotDataException An error occurred when interacting with the data source.
   */
  protected abstract Identifier createNewIdentifier(
      Versionable webasset, Folder folder, String existingId) throws DotDataException;

  /**
   * Creates a new Identifier for a given versionable asset under a given site. The ID value will be
   * randomly generated.
   *
   * @param versionable - The asset that will be created.
   * @param site - The site that the asset will be created in.
   * @return The {@link Identifier} of the new asset.
   * @throws DotDataException An error occurred when interacting with the data source.
   */
  protected abstract Identifier createNewIdentifier(Versionable versionable, Host site)
      throws DotDataException;

  /**
   * Creates a new Identifier for a given versionable asset under a given site. In this method, the
   * ID value will <b>NOT</b> be randomly generated as it is specified as a parameter.
   *
   * @param versionable - The asset that will be created.
   * @param site - The site that the asset will be created in.
   * @param existingId - The ID of the new Identifier.
   * @return The {@link Identifier} of the new asset.
   * @throws DotDataException An error occurred when interacting with the data source.
   */
  protected abstract Identifier createNewIdentifier(
      Versionable versionable, Host site, String existingId) throws DotDataException;

  /**
   * @return
   * @throws DotDataException
   */
  protected abstract List<Identifier> loadAllIdentifiers() throws DotDataException;

  /**
   * @param identifierInode
   * @return
   */
  protected abstract boolean isIdentifier(String identifierInode);

  /**
   * @param identifier
   * @return
   * @throws DotDataException
   */
  protected abstract Identifier saveIdentifier(final Identifier identifier) throws DotDataException;

  /**
   * Deletes all relationships with this identifier. Accordingly, the object will be removed from
   * the identifier table through a DB trigger
   *
   * @param ident
   */
  protected abstract void deleteIdentifier(Identifier ident) throws DotDataException;

  /**
   * @param siteId
   * @param parent_path
   * @return
   * @throws DotDataException
   */
  protected abstract List<Identifier> findByParentPath(final String siteId, String parent_path)
      throws DotDataException;

  /**
   * This method hits the DB, table identifier to get the Asset Type.
   *
   * @param identifier - The type of Identifier.
   * @return Type of the Identifier that matches parameter. This method hits the DB.
   * @throws DotDataException
   */
  protected abstract String getAssetTypeFromDB(String identifier) throws DotDataException;
}
