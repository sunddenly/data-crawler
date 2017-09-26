import dva from 'dva';
import './index.html';

import routes from './routes';

import AdminLayoutModel from '../../models/admin-layout';
import List from '../../models/list';
import Crawl from '../../models/crawl';

const app = dva();

app.router(routes);

app.model(AdminLayoutModel);
app.model(List);
app.model(Crawl);

export default app;
