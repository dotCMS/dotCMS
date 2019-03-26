package com.dotmarketing.portlets.structure.factories;

import com.dotcms.contenttype.model.type.ContentTypeIf;
import com.dotmarketing.business.Cachable;
import com.dotmarketing.business.DotCacheException;
import com.dotmarketing.portlets.structure.model.Relationship;
import java.util.List;

public abstract class RelationshipCache implements Cachable {

  // ### READ ###
  public abstract Relationship getRelationshipByInode(String inode) throws DotCacheException;

  public abstract Relationship getRelationshipByName(String name) throws DotCacheException;

  public abstract void putRelationshipByInode(Relationship rel);

  public abstract void removeRelationshipByInode(Relationship rel);

  public abstract void clearCache();

  public List<Relationship> getRelationshipsByStruct(ContentTypeIf struct)
      throws DotCacheException {
    // TODO Auto-generated method stub
    return null;
  }

  public void putRelationshipsByStruct(ContentTypeIf struct, List<Relationship> rels)
      throws DotCacheException {
    // TODO Auto-generated method stub

  }

  public void removeRelationshipsByStruct(ContentTypeIf struct) throws DotCacheException {
    // TODO Auto-generated method stub

  }

  public void putRelationshipsByType(ContentTypeIf type, List<Relationship> rels) {
    // TODO Auto-generated method stub

  }

  public void removeRelationshipsByType(ContentTypeIf type) {
    // TODO Auto-generated method stub

  }

  public List<Relationship> getRelationshipsByType(ContentTypeIf type) throws DotCacheException {
    // TODO Auto-generated method stub
    return null;
  }
}
