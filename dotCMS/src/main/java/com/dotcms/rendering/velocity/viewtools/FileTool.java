package com.dotcms.rendering.velocity.viewtools;
import com.dotmarketing.beans.Identifier;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.business.DotStateException;
import com.dotmarketing.business.IdentifierAPI;
import com.dotmarketing.business.UserAPI;

import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.exception.DotSecurityException;
import com.dotmarketing.portlets.contentlet.model.Contentlet;
import com.dotmarketing.portlets.contentlet.model.ContentletVersionInfo;
import com.dotmarketing.portlets.fileassets.business.FileAsset;
import com.dotmarketing.portlets.fileassets.business.IFileAsset;
import com.dotmarketing.portlets.languagesmanager.business.LanguageAPI;
import com.dotmarketing.util.InodeUtils;
import com.dotmarketing.util.UtilMethods;

import com.liferay.util.StringPool;
import java.io.File;
import java.util.Optional;
import org.apache.velocity.tools.view.tools.ViewTool;

public class FileTool implements ViewTool {

	private static final UserAPI userAPI = APILocator.getUserAPI();
	private static final LanguageAPI languageAPI = APILocator.getLanguageAPI();
	private static final IdentifierAPI identifierAPI = APILocator.getIdentifierAPI();

	public void init(Object initData) {

	}

	public IFileAsset getFile(String identifier, boolean live) throws DotStateException, DotDataException, DotSecurityException{
		return getFile(identifier, live, languageAPI.getDefaultLanguage().getId());
	}

	public IFileAsset getFile(String identifier, boolean live, long languageId) throws DotDataException, DotStateException, DotSecurityException{
		final String conInode = getContentInode(identifier, live, languageId);
		FileAsset file  = APILocator.getFileAssetAPI().fromContentlet(APILocator.getContentletAPI().find(conInode,  userAPI.getSystemUser(), false));
	    return file;
	}

	private String  getContentInode(String identifier, boolean live, long languageId)
			throws DotDataException {
		Identifier id = identifierAPI.find(identifier);
		Optional<ContentletVersionInfo> cvi = APILocator.getVersionableAPI().getContentletVersionInfo(id.getId(),
				languageId);

		if(!cvi.isPresent()) {
			throw new DotDataException("Can't find Content-version-info. Identifier: " + id.getId() + ". Lang:" + languageId);
		}
		String conInode = !live ? cvi.get().getWorkingInode() : cvi.get().getLiveInode();
		return conInode;
	}

	public Contentlet getFileAsContentlet(String identifier, boolean live, long languageId) throws DotDataException, DotStateException, DotSecurityException{
		try {
			final IFileAsset file = getFile(identifier, live, languageId);
			return (FileAsset) file;
		} catch(DotStateException e) {
			final String conInode = getContentInode(identifier, live, languageId);
			return APILocator.getContentletAPI().find(conInode, userAPI.getSystemUser(), false);
		}
	}

	public String getURI(FileAsset file){
		return getURI(file, -1);
	}

	public String getURI(FileAsset file, long languageId){
		String langStr = languageId>0?"?language_id="+languageId:"";

		if(file != null && InodeUtils.isSet(file.getIdentifier())){
            return UtilMethods.espaceForVelocity("/contentAsset/raw-data/" + file.getIdentifier() + "/fileAsset" + langStr);
        }else{
			return "";
		}
	}

	public String getURI(final Contentlet contentlet, long languageId){
		String uri = StringPool.BLANK;

		if (contentlet instanceof FileAsset) {
			uri = getURI((FileAsset) contentlet, languageId);
		} else if (contentlet.isDotAsset()){
			uri = UtilMethods.espaceForVelocity(
					String.format("dA/%s", contentlet.getIdentifier())
			);
		}
		return uri;
	}
	
	public IFileAsset getNewFile(){
	    return new FileAsset(); 
	}

}
