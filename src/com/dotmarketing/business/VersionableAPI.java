package com.dotmarketing.business;

import java.util.Date;
import java.util.List;

import com.dotmarketing.beans.Identifier;
import com.dotmarketing.beans.VersionInfo;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.exception.DotSecurityException;
import com.dotmarketing.portlets.contentlet.model.ContentletLangVersionInfo;
import com.liferay.portal.model.User;

public interface VersionableAPI {

	/**
	 * Finds the working version based on any version of the object being passed in
	 * @param inode
	 * @param user
	 * @param respectAnonPermissions
	 * @return
	 * @throws DotDataException
	 * @throws DotStateException
	 * @throws DotSecurityException
	 */
	public Versionable findWorkingVersion(Versionable inode, User user, boolean respectAnonPermissions) throws DotDataException, DotStateException,DotSecurityException;
	/**
	 * Finds the working version based on the identifier
	 * @param id
	 * @param user
	 * @param respectAnonPermissions
	 * @return
	 * @throws DotDataException
	 * @throws DotStateException
	 * @throws DotSecurityException
	 */
	public Versionable findWorkingVersion(Identifier id, User user, boolean respectAnonPermissions)  throws DotDataException, DotStateException,DotSecurityException;
	/**
	 * Finds the working version based on the identifier's id
	 * @param id
	 * @param user
	 * @param respectAnonPermissions
	 * @return
	 * @throws DotDataException
	 * @throws DotStateException
	 * @throws DotSecurityException
	 */
	public Versionable findWorkingVersion(String id, User user, boolean respectAnonPermissions) throws DotDataException, DotStateException,DotSecurityException;
	/**
	 * Finds the live version based on any version of the object being passed in
	 * @param inode
	 * @param user
	 * @param respectAnonPermissions
	 * @return
	 * @throws DotDataException
	 * @throws DotStateException
	 * @throws DotSecurityException
	 */
	public Versionable findLiveVersion(Versionable inode, User user, boolean respectAnonPermissions)  throws DotDataException, DotStateException,DotSecurityException;
	
	/**
	 * Finds the live version based on the identifier
	 * @param id
	 * @param user
	 * @param respectAnonPermissions
	 * @return
	 * @throws DotDataException
	 * @throws DotStateException
	 * @throws DotSecurityException
	 */
	public Versionable findLiveVersion(Identifier id, User user, boolean respectAnonPermissions)  throws DotDataException, DotStateException,DotSecurityException;
	/**
	 * Finds the live version based on the identifier's id
	 * @param id
	 * @param user
	 * @param respectAnonPermissions
	 * @return
	 * @throws DotDataException
	 * @throws DotStateException
	 * @throws DotSecurityException
	 */
	public Versionable findLiveVersion(String id, User user, boolean respectAnonPermissions)  throws DotDataException, DotStateException,DotSecurityException;
	/**
	 * Finds all versions based on an versionable
	 * @param inode
	 * @param user
	 * @param respectAnonPermissions
	 * @return
	 * @throws DotDataException
	 * @throws DotStateException
	 * @throws DotSecurityException
	 */
	public List<Versionable>  findAllVersions(Versionable inode)  throws DotDataException, DotStateException,DotSecurityException;
	/**
	 * Finds all versions based on an id
	 * @param inode
	 * @param user
	 * @param respectAnonPermissions
	 * @return
	 * @throws DotDataException
	 * @throws DotStateException
	 * @throws DotSecurityException
	 */
	public List<Versionable>  findAllVersions(Identifier id)  throws DotDataException, DotStateException,DotSecurityException;
	
	/**
	 * Finds all versions based on an id
	 * @param inode
	 * @param user
	 * @param respectAnonPermissions
	 * @return
	 * @throws DotDataException
	 * @throws DotStateException
	 * @throws DotSecurityException
	 */
	public List<Versionable>  findAllVersions(String id)  throws DotDataException, DotStateException,DotSecurityException;
	
	/**
	 * Finds all versions based on any version of the object being passed in
	 * @param inode
	 * @param user
	 * @param respectAnonPermissions
	 * @return
	 * @throws DotDataException
	 * @throws DotStateException
	 * @throws DotSecurityException
	 */
	public List<Versionable>  findAllVersions(Versionable inode, User user, boolean respectAnonPermissions)  throws DotDataException, DotStateException,DotSecurityException;
	/**
	 * Finds all versions based on an identifier
	 * @param inode
	 * @param user
	 * @param respectAnonPermissions
	 * @return
	 * @throws DotDataException
	 * @throws DotStateException
	 * @throws DotSecurityException
	 */

	public List<Versionable>  findAllVersions(Identifier id, User user, boolean respectAnonPermissions)  throws DotDataException, DotStateException,DotSecurityException;
	/**
	 * Finds all versions based on an identifier.id
	 * @param inode
	 * @param user
	 * @param respectAnonPermissions
	 * @return
	 * @throws DotDataException
	 * @throws DotStateException
	 * @throws DotSecurityException
	 */
	public List<Versionable>  findAllVersions(String id, User user, boolean respectAnonPermissions)  throws DotDataException, DotStateException,DotSecurityException;
	/**
	 * Finds the deleted version based on any version of the object being passed in
	 * @param inode
	 * @param user
	 * @param respectAnonPermissions
	 * @return
	 * @throws DotDataException
	 * @throws DotStateException
	 * @throws DotSecurityException
	 */
	public Versionable findDeletedVersion(Versionable inode, User user, boolean respectAnonPermissions) throws DotDataException, DotStateException,DotSecurityException;
	/**
	 * Finds the deleted version based on the identifier
	 * @param id
	 * @param user
	 * @param respectAnonPermissions
	 * @return
	 * @throws DotDataException
	 * @throws DotStateException
	 * @throws DotSecurityException
	 */
	public Versionable findDeletedVersion(Identifier id, User user, boolean respectAnonPermissions)  throws DotDataException, DotStateException,DotSecurityException;
	/**
	 * Finds the deleted version based on the identifier's id
	 * @param id
	 * @param user
	 * @param respectAnonPermissions
	 * @return
	 * @throws DotDataException
	 * @throws DotStateException
	 * @throws DotSecurityException
	 */
	public Versionable findDeletedVersion(String id, User user, boolean respectAnonPermissions) throws DotDataException, DotStateException,DotSecurityException;
	
	/**
	 * Tells if the versionable is live
	 * 
	 * @param versionable
	 * @return true if it is live. false if not
	 * @throws DotDataException
	 * @throws DotStateException
	 * @throws DotSecurityException
	 */
	public boolean isLive(Versionable versionable) throws DotDataException, DotStateException,DotSecurityException;
	
	/**
	 * Set this versionable as the live version for its identifier
	 * 
	 * @param versionable versionable to be set as the live version
	 * @throws DotDataException
	 * @throws DotStateException
	 * @throws DotSecurityException
	 */
	public void setLive(Versionable versionable) throws DotDataException, DotStateException,DotSecurityException;
	
	/**
	 * Remove the reference to the live version for this identifier. This is useful to unpublish 
	 * an asset.
	 * 
	 * @param identifier identifier of the asset to be left without live version
	 * @throws DotDataException
	 * @throws DotStateException
	 * @throws DotSecurityException
	 */
	public void removeLive(String identifier) throws DotDataException, DotStateException,DotSecurityException;
	
	/**
     * Remove the reference to the live version for this identifier. This is useful to unpublish 
     * an asset. This is the contentlet specific method as we need the language_id for those cases 
     * 
     * @param identifier identifier of the asset to be left without live version
     * @param lang language id of the contentlet
     * @throws DotDataException
     * @throws DotStateException
     * @throws DotSecurityException
     */
    public void removeLive(String identifier, long lang) throws DotDataException, DotStateException,DotSecurityException;
	
	/**
	 * Tells if the versionable is the working version for its identifier
	 * 
	 * @param versionable
	 * @return true if it is the working version. False if not
	 * @throws DotDataException
	 * @throws DotStateException
	 * @throws DotSecurityException
	 */
	public boolean isWorking(Versionable versionable) throws DotDataException, DotStateException,DotSecurityException;
	
	/**
	 * Sets the versionable as the working version for its identifier
	 * 
	 * @param versionable
	 * @throws DotDataException
	 * @throws DotStateException
	 * @throws DotSecurityException
	 */
	public void setWorking(Versionable versionable) throws DotDataException, DotStateException,DotSecurityException;
	
	/**
	 * Tells if the versionable with the specified identifier is locked
	 * 
	 * @param identifier identifier inode of the versionable
	 * @return true if it is locked, false if not
	 * @throws DotDataException
	 * @throws DotStateException
	 * @throws DotSecurityException
	 */
	public boolean isLocked(String identifier) throws DotDataException, DotStateException,DotSecurityException;
	
	/**
	 * Returns the userId of the owner of the asset's lock
	 * 
	 * @param identifier
	 * @return
	 * @throws DotDataException
	 * @throws DotStateException
	 * @throws DotSecurityException
	 */
	public String getLockedBy(String identifier) throws DotDataException, DotStateException,DotSecurityException;
	
	/**
	 * Returns the date when the asset were locked
	 * 
	 * @param identifier
	 * @return
	 * @throws DotDataException
	 * @throws DotStateException
	 * @throws DotSecurityException
	 */
	public Date getLockedOn(String identifier) throws DotDataException, DotStateException,DotSecurityException;
	
	/**
	 * Allows to change locked status for the versionable with the specified identifier
	 * 
	 * @param identifier identifier inode of the versionable
	 * @param locked status to be set
	 * @param user lock owner
	 * @throws DotDataException
	 * @throws DotStateException
	 * @throws DotSecurityException
	 */
	public void setLocked(String identifier, boolean locked, User user) throws DotDataException, DotStateException,DotSecurityException;
	
	/**
	 * Tells if the versionable with the specified identifier is deleted
	 * 
	 * @param identifier
	 * @return true if it is deleted, false if not
	 * @throws DotDataException
	 * @throws DotStateException
	 * @throws DotSecurityException
	 */
	public boolean isDeleted(String identifier) throws DotDataException, DotStateException,DotSecurityException;
	
	/**
	 * Allows to delete (when true) of undelete (when false) a versionable with the specified ID
	 * 
	 * @param identifier
	 * @param deleted true to delete, false to undelete
	 * @throws DotDataException
	 * @throws DotStateException
	 * @throws DotSecurityException
	 */
	public void setDeleted(String identifier, boolean deleted) throws DotDataException, DotStateException,DotSecurityException;
	
	
	/**
	 * Will return the @ContentletLangVersionInfo holder for the given identifier
	 * @param identifier
	 * @param lang
	 * @return
	 * @throws DotDataException
	 * @throws DotStateException
	 */
	public ContentletLangVersionInfo getContentletLangVersionInfo(String identifier, long lang) throws DotDataException,
	DotStateException;
	/**
	 * Will return the @VersionInfo holder for the given identifier
	 * @param identifier
	 * @return
	 * @throws DotDataException
	 * @throws DotStateException
	 */
	public  VersionInfo getVersionInfo(String identifier) throws DotDataException, DotStateException;
	
	public void deleteVersionInfo(String identifier) throws DotDataException;
	
	public void deleteContentletVersionInfo(String identifier, long lang) throws DotDataException;
}
