<!-- @format -->

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <meta name="robots" content="noindex" />
        <meta name="referrer" content="origin" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title>DotCMS GraphQL Playground</title>

        <link rel="icon" href="favicon.ico" />
        <link type="text/css" href="/html/portlet/ext/graphql/graphiql.min.css" rel="stylesheet" />
        <style>
            body {
                height: 100vh;
                margin: 0;
                overflow: hidden;
            }
            #splash {
                color: #333;
                display: flex;
                flex-direction: column;
                font-family: system, -apple-system, 'San Francisco', '.SFNSDisplay-Regular', 'Segoe UI', Segoe,
                    'Segoe WP', 'Helvetica Neue', helvetica, 'Lucida Grande', arial, sans-serif;
                height: 100vh;
                justify-content: center;
                text-align: center;
            }
            .topBar .title {
                display: none;
            }
            .graphiql-container .execute-button-wrap {
                margin-left: 0;
            }
        </style>
    </head>
    <body>
        <div id="splash">Loading&hellip;</div>
        <script src="/html/portlet/ext/graphql/es6-promise.auto.min.js"></script>
        <script src="/html/portlet/ext/graphql/react.production.min.js"></script>
        <script src="/html/portlet/ext/graphql/react-dom.production.min.js"></script>
        <script src="/html/portlet/ext/graphql/graphiql.min.js"></script>
        <script>
            // Parse the search string to get url parameters.
            var search = window.location.search;
            var parameters = {
                query: `# dotCMS GraphQL Playground
# 
# Example Query:
#
# query ContentAPI {
#   ProductCollection(query: "+title:snow", limit: 10, offset: 0, sortBy: "score") {
#     title
#     urlMap
#     category {
#       name
#       inode
#     }
#     retailPrice
#     image {
#       versionPath
#     }
#   }
# }
#
# Keyboard shortcuts:
#
#  Prettify Query:  Shift-Ctrl-P (or press the prettify button above)
#
#     Merge Query:  Shift-Ctrl-M (or press the merge button above)
#
#       Run Query:  Ctrl-Enter (or press the play button above)
#
#   Auto Complete:  Ctrl-Space (or just start typing)
#
`
            };
            search
                .substr(1)
                .split('&')
                .forEach(function (entry) {
                    var eq = entry.indexOf('=');
                    if (eq >= 0) {
                        parameters[decodeURIComponent(entry.slice(0, eq))] = decodeURIComponent(entry.slice(eq + 1));
                    }
                });

            // if variables was provided, try to format it.
            if (parameters.variables) {
                try {
                    parameters.variables = JSON.stringify(JSON.parse(parameters.variables), null, 2);
                } catch (e) {
                    // Do nothing, we want to display the invalid JSON as a string, rather
                    // than present an error.
                }
            }

            // When the query and variables string is edited, update the URL bar so
            // that it can be easily shared
            function onEditQuery(newQuery) {
                parameters.query = newQuery;
                updateURL();
            }
            function onEditVariables(newVariables) {
                parameters.variables = newVariables;
                updateURL();
            }
            function onEditOperationName(newOperationName) {
                parameters.operationName = newOperationName;
                updateURL();
            }
            function updateURL() {
                var newSearch =
                    '?' +
                    Object.keys(parameters)
                        .filter(function (key) {
                            return Boolean(parameters[key]);
                        })
                        .map(function (key) {
                            return encodeURIComponent(key) + '=' + encodeURIComponent(parameters[key]);
                        })
                        .join('&');
                history.replaceState(null, null, newSearch);
            }

            function graphQLFetcher(graphQLParams) {
                // This example expects a GraphQL server at the path /graphql.
                // Change this to point wherever you host your GraphQL server.
                return fetch(parameters.fetchURL || '/api/v1/graphql', {
                    method: 'post',
                    headers: {
                        Accept: 'application/json',
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(graphQLParams),
                })
                    .then(function (response) {
                        return response.text();
                    })
                    .then(function (responseBody) {
                        try {
                            return JSON.parse(responseBody);
                        } catch (error) {
                            return responseBody;
                        }
                    });
            }

            // Render <GraphiQL /> into the body.
            ReactDOM.render(
                React.createElement(GraphiQL, {
                    fetcher: graphQLFetcher,
                    query: parameters.query,
                    variables: parameters.variables,
                    operationName: parameters.operationName,
                    onEditQuery: onEditQuery,
                    onEditVariables: onEditVariables,
                    onEditOperationName: onEditOperationName,
                }),
                document.body
            );
        </script>
    </body>
</html>
