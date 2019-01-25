package com.dotcms.graphql.datafetcher;

import com.dotcms.graphql.DotGraphQLContext;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.portlets.contentlet.model.Contentlet;
import com.dotmarketing.portlets.contentlet.transform.ContentletToMapTransformer;
import com.liferay.portal.model.User;

import java.util.List;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

public class ContentletDataFetcher implements DataFetcher<List<Contentlet>> {
    @Override
    public List<Contentlet> get(final DataFetchingEnvironment environment) throws Exception {
        final User user = ((DotGraphQLContext) environment.getContext()).getUser();
        final String query = environment.getArgument("query");
        final List<Contentlet> contentletList = APILocator.getContentletAPI().search(query, 0, -1, null,
            user, true);
        return new ContentletToMapTransformer(contentletList).hydrate();
    }
}
