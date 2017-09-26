const webpack = require('webpack');
const webpackConfig = require('./webpack-config-base');

// webpackConfig.plugins.push(new webpack.HotModuleReplacementPlugin());

webpackConfig.module.rules[0].query.plugins.push('dva-hmr');

module.exports = Object.assign(webpackConfig, {
    devtool: '#eval',
});
