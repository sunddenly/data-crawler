import React from 'react';
import PropTypes from 'prop-types';
import './logo.less';

const LogoImg = ({ url, title, collapsed }) => (
    <div className="layout-logoimg">
        <img src={url} alt="logo" />
        {collapsed ? null : (
            <span>{title}</span>
        )}
    </div>
    );

LogoImg.propTypes = {
    url: PropTypes.string,
};

export default LogoImg;
