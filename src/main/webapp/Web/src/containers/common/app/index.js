import React, { Component } from 'react';
import PrivateContainer from '../private-container';
import AdminLayout from '../admin-layout';

class App extends Component {
    render() {
        return (
            <PrivateContainer enable={false}>
                <AdminLayout>
                </AdminLayout>
            </PrivateContainer>
        );
    }
}

export default App;
