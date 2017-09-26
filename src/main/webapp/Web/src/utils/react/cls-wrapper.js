import React from 'react';
import classnames from 'classnames';

export default clsName => (({ className, children }) => (
    <div className={classnames(clsName, className)}>{children}</div>
));
