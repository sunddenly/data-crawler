import React, { Component } from 'react';
import { connect } from 'dva';
import SiderLayout from '../../../components/sider-layout';
import LogoImg from './logo';
import '../../../styles/common.less';
import './index.less';

@connect(state => state.adminLayout)
class Layout extends Component {

    onCollapse = () => {
        this.props.dispatch({
            type : 'adminLayout/changeMenuCollapsed',
        });
    };

    render() {
        const {
            title,
            siderMenus,
            headerMenus,
            logo,
            logoTitle,
            menuCollapsed,
            children,
        } = this.props;

        const logoImg = (
            <LogoImg
                url={logo}
                title={logoTitle}
                collapsed={menuCollapsed}
            />
        );

        return (
            <SiderLayout
                headerMenus={headerMenus}
                headerContent={title}
                siderMenus={siderMenus}
                logo={logoImg}
                collapsed={menuCollapsed}
                onCollapse={this.onCollapse}
            >
                {children}
            </SiderLayout>
        );
    }
}

export default Layout;
