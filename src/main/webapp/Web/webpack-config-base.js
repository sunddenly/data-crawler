const webpack = require('webpack');
const path = require('path');
const rucksack = require('rucksack-css');
const autoprefixer = require('autoprefixer');
const ExtractTextPlugin = require('extract-text-webpack-plugin');

const extractCSS = new ExtractTextPlugin('[name].css');

const NODE_ENV = process.env.NODE_ENV;

module.exports = {
    entry: {
        index: './src/entries/index/index.js',
    },
    output: {
        path: path.join(__dirname, 'dist'),
        filename: '[name].js',
        // publicPath: '/dist/',
        sourceMapFilename: '[file].map',
    },
    resolve: {
        modules: [
            path.join(__dirname, 'node_modules'),
        ],
        extensions: [
            '.js',
        ],
    },
    module: {
        rules: [
            {
                test: /\.js$/,
                exclude: [
                    /node_modules/,
                    path.join(__dirname, '..', 'server'),
                ],
                loader: path.join(__dirname, 'node_modules', 'babel-loader'),
                query: {
                    cacheDirectory: path.join(__dirname, '.babel_cache'),
                    presets: [
                        ['es2015'],
                        'react',
                        'stage-0',
                    ],
                    plugins: [
                        'add-module-exports',
                        'transform-decorators-legacy',
                        'transform-runtime',
                        ['import', {
                            libraryName: 'antd',
                            style: true,
                        }],
                    ],
                    babelrc: false,
                },
            },
            {
                test: /\.css$/,
                loader: extractCSS.extract([path.join(__dirname, 'node_modules', 'css-loader')]),
            },
            {
                test: /\.less$/,
                loader: extractCSS.extract([
                    path.join(__dirname, 'node_modules', 'css-loader'),
                    path.join(__dirname, 'node_modules', 'postcss-loader'),
                    path.join(__dirname, 'node_modules', 'less-loader'),
                ]),
            },
            {
                test: /\.woff(2)?(\?v=[0-9]\.[0-9]\.[0-9])?$/,
                loader: path.join(__dirname, 'node_modules', 'url-loader?limit=10000&minetype=application/font-woff'),
            },
            {
                test: /\.ttf(\?v=\d+\.\d+\.\d+)?$/,
                loader: path.join(__dirname, 'node_modules', 'url-loader?limit=10000&minetype=application/octet-stream'),
            },
            {
                test: /\.svg(\?v=\d+\.\d+\.\d+)?$/,
                loader: path.join(__dirname, 'node_modules', 'url-loader?limit=10000&minetype=image/svg+xml'),
            },
            {
                test: /\.(eot|png|jpg|jpeg|gif)(\?v=\d+\.\d+\.\d+)?$/,
                loader: path.join(__dirname, 'node_modules', 'url-loader?limit=10000'),
            },
            {
                test: /\.html?$/,
                loader: path.join(__dirname, 'node_modules', 'file-loader?name=[name].[ext]'),
            },
        ],
    },
    plugins: [
        new webpack.DefinePlugin({
            'process.env': {
                NODE_ENV: JSON.stringify(NODE_ENV),
            },
        }),
        new webpack.optimize.CommonsChunkPlugin({
            name: 'common',
            filename: 'common.js',
        }),
        extractCSS,
        new webpack.LoaderOptionsPlugin({
            options: {
                context: __dirname,
                postcss: [
                    rucksack(),
                    autoprefixer({
                        browsers: ['last 2 versions', 'Firefox ESR', '> 1%', 'ie >= 8', 'iOS >= 8', 'Android >= 4'],
                    }),
                ],
            },
        }),
    ],
};
