var path = require("path");
var ExtractTextPlugin = require("extract-text-webpack-plugin");

module.exports = [
    {
        // uncomment line below for quick rebuilds during development
        //devtool: "eval",

        // uncomment line below during development if you want to have full debugging available
        //devtool: "sourcemap",
        name: "browser",
        entry: {
            app: [
                "./app/front/main-entry.tsx"
            ]
        },
        output: {
            path: path.join(__dirname, "public/javascripts"),
            filename: "[name].bundle.js",
            publicPath: "/javascripts"
        },
        resolve: {
            extensions: ["", ".ts", ".tsx", ".js", ".html"],
            modulesDirectories: ["node_modules"]
        },
        module: {
            loaders: [
                {
                    test: /\.(ts|tsx)$/,
                    loader: "awesome-typescript"
                }, {
                    test: /\.css$/,
                    loader: ExtractTextPlugin.extract("css")
                }
            ]
        },
        plugins: [
            function () {
                this.plugin("done", function (stats) {
                    if (stats.compilation.errors && stats.compilation.errors.length && process.argv.indexOf("--watch") === -1) {
                        console.log(stats.compilation.errors);
                        process.exit(1); // or throw new Error('webpack build failed.');
                    }
                });
            },
            new ExtractTextPlugin("bundle.css", {
                allChunks: true,
                disable: false
            })
        ],
        node: {
            dns: "mock",
            net: "mock"
        }
    }
];
