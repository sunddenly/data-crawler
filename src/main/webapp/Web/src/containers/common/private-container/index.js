import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { inject, observer } from 'mobx-react';
// import Layout from '../layout';

@inject('userStore')
@observer
export default class PrivateContainer extends Component {

    static defaultProps = {
        placeholder: <div />,
        enable: true,
    };

    static propTypes = {
        placeholder: PropTypes.oneOfType([
            PropTypes.element,
            PropTypes.string,
        ]),
        enable: PropTypes.bool,
    };

    componentDidMount() {

        const {
            userStore: {
                logined,
                getUser,
            },
            enable,
        } = this.props;

        if (enable && !logined) {
            getUser();
        }
    }

    render() {

        const {
            userStore: {
                logined,
            },
            enable,
            placeholder,
            children,
        } = this.props;

        if (enable && !logined) {
            return placeholder;
        }

        return children;
    }
}
