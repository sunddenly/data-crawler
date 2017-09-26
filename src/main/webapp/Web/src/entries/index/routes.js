import React from 'react';
import { Router, Route, IndexRedirect } from 'dva/router';

import AdminLayout from '../../containers/common/admin-layout';
import Dashboard from '../../containers/dashboard';
import List from '../../containers/list';
import Crawl from '../../containers/crawl';

export default function ({ history }) {
    return (
        <Router history={history}>
            <Route path="/" component={AdminLayout} >
                <IndexRedirect to="list" />
                <Route path="list" component={List} />
                <Route path="crawl" component={Crawl}/>
            </Route>
        </Router>
    );
}
