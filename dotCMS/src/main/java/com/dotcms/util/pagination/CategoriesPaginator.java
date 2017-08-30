package com.dotcms.util.pagination;

import java.util.Collection;
import java.util.Map;

import com.dotcms.repackage.com.google.common.annotations.VisibleForTesting;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.exception.DotRuntimeException;
import com.dotmarketing.exception.DotSecurityException;
import com.dotmarketing.portlets.categories.business.CategoryAPI;
import com.dotmarketing.portlets.categories.business.PaginatedCategories;
import com.dotmarketing.portlets.categories.model.Category;
import com.liferay.portal.model.User;

/**
 * Created by freddyrodriguez on 8/16/17.
 */
public class CategoriesPaginator implements Paginator<Category> {

    private CategoryAPI categoryAPI;
    private int lastTotalRecords;

    @VisibleForTesting
    public CategoriesPaginator(final CategoryAPI categoryAPI){
        this.categoryAPI = categoryAPI;
    }

    public CategoriesPaginator(){
        this(APILocator.getCategoryAPI());
    }

    @Override
    public long getTotalRecords(final String filter) {
        return lastTotalRecords;
    }

    @Override
    public Collection<Category> getItems(final User user, final String filter, final int limit, final int offset,
                                         final String orderby, final OrderDirection direction, final Map<String, Object> extraParams) {
        try {
            String categoriesSort = null;

            if (orderby != null) {
                categoriesSort = direction == OrderDirection.DESC ? "-" + orderby : orderby;
            }

            final PaginatedCategories topLevelCategories = categoryAPI.findTopLevelCategories(user, false, offset, limit, filter, categoriesSort);
            lastTotalRecords = topLevelCategories.getTotalCount();

            return topLevelCategories.getCategories();
        } catch (DotDataException|DotSecurityException e) {
            throw new DotRuntimeException(e);
        }
    }
}
