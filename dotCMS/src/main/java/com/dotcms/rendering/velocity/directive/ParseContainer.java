package com.dotcms.rendering.velocity.directive;

import com.dotcms.personalization.PersonalizationUtil;
import com.dotmarketing.beans.MultiTree;
import com.dotmarketing.util.UtilMethods;
import jnr.ffi.Struct;
import org.apache.velocity.context.Context;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.TemplateInitException;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.parser.node.Node;

import java.io.Writer;
import java.util.Optional;
/**
 * Could includes a container by id (long or shorty) or a path (with or without host "without will use the current host").
 */
public class ParseContainer extends DotDirective {

	public  static final String DEFAULT_UUID_VALUE = MultiTree.LEGACY_RELATION_TYPE;
	private static final long serialVersionUID     = 1L;

	@Override
	public final String getName() {
		return "parseContainer";
	}

	public void init(RuntimeServices rs, InternalContextAdapter context, Node node) throws TemplateInitException {
		super.init(rs, context, node);

	}

	@Override
	String resolveTemplatePath(final Context context, final Writer writer, final RenderParams params,
			final String[] arguments) {

		final TemplatePathStrategyResolver templatePathResolver = TemplatePathStrategyResolver.getInstance();
		final Optional<TemplatePathStrategy> strategy           = templatePathResolver.get(context, params, arguments);

		this.processContentletListPerPersona(context, arguments);

		return strategy.isPresent()?
				strategy.get().apply(context, params, arguments):
				templatePathResolver.getDefaultStrategy().apply(context, params, arguments);
	}

	// depending on the persona selected (if any) will set the contentlist default
	private void processContentletListPerPersona (final Context context, final String[] arguments) {

		final String id 			 = arguments[0];
		final String personalization = PersonalizationUtil.getContainerPersonalization();
		final String uniqueId        = arguments.length > 1 && UtilMethods.isSet(arguments[1])? arguments[1] :  DEFAULT_UUID_VALUE;
		final String containerId     = (String) context.get("containerIdentifier"+id);
		final String key = "contentletList" + containerId + uniqueId;

		context.put(key, context.get(key + personalization));

	}

}
