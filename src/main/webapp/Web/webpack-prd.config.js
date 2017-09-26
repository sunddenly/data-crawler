const webpack = require('webpack');
const webpackConfig = require('./webpack-config-base');

webpackConfig.plugins.push(new webpack.optimize.UglifyJsPlugin({
    compress: {
        warnings: false,
    },
}));

webpackConfig.plugins.push(new webpack.LoaderOptionsPlugin({
    minimize: true,
}));


module.exports = Object.assign({}, webpackConfig);
