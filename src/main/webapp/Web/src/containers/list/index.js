import React, { Component } from 'react';
import { connect } from 'dva';
import {Select, Button, Table, Icon, message, Modal, Pagination} from 'antd';
import { bindActionCreators } from 'redux';
import {PageContainer} from '../../components/sider-layout';
import model from '../../models/list';
import './index.less';

const Option = Select.Option;
const actions = model.actions;

@connect(state => state['list'], dispatch => ({
    actions: bindActionCreators(actions, dispatch)
}))

class List extends Component {

    constructor(props) {
        super(props);
        this.state ={
            selectedCity: null,
            pagination: {
                defaultCurrent: 1,
                current: 1,
                defaultPageSize: 10,
            },

        }
    }

    componentDidMount() {
        const { fetchCity } = this.props.actions;
        fetchCity();
    }

    handleSelect = (value, option) => {
        this.setState({
            selectedCity: value,
        })
    }

    handleSearch = () => {
        if(this.state.selectedCity) {
            let item = {};
            item['cityId'] = this.state.selectedCity;
            item['pageNumber'] = this.state.pagination.current;
            const {fetchHotel} = this.props.actions;
            fetchHotel(item);
        }
        else {
            message.error('请先选择城市名称');
        }
    }

    handlePageChange = (page, pageSize) => {
        console.log(page);
        this.setState({
            pagination:{
                current: page,
            }
        })
        let item = {};
        item['cityId'] = this.state.selectedCity;
        item['pageNumber'] = page;
        const {fetchHotel} = this.props.actions;
        fetchHotel(item);
    }


    renderModal = (text) => {
        const {
            hotelDetail
        } = this.props;
        const {fetchHotelDetail} = this.props.actions;
        fetchHotelDetail(text.id);
        const columns = [
            {
                title:  '序号',
                dataIndex: 'id',
                key: 'id',
                width: 100,
            },
            {
                title:  '房型',
                dataIndex: 'type',
                key: 'type',
                width: 300,
            },
            {
                title:  '床型',
                dataIndex: 'bedType',
                key: 'bedType',
                width: 200,
            },
            {
                title:  '支持免费取消',
                dataIndex: 'freeCancle',
                key: 'freeCancle',
                width: 200,
            },
            {
                title:  '付款方式',
                dataIndex: 'payWay',
                key: 'payWay',
                width: 200,
            },
            {
                title:  '价格',
                dataIndex: 'price',
                key: 'price',
                width: 100,
            },
        ]
        if(hotelDetail[text.id]) {
            const dataSource = hotelDetail[text.id];
            Modal.info({
                title: '酒店详情',
                width: '700',
                content: (
                    <Table
                    columns={columns}
                    dataSource={dataSource}
                    rowkey={record => record.id}
                    />
                ),
            });
        }

    }

    renderTable() {
        const {
            loading,
            hotelData,
            lastUpdateTime,
            selectedCity,
            totalCount,
        } = this.props;
        if (!loading && !selectedCity) {
            return (
                <p className="content">请选择城市后点击查询，查看结果</p>
            )
        }
        else {
            const columns = [
                {
                    title:  '酒店id',
                    dataIndex: 'hotelId',
                    key: 'hotelId',
                    width: 100,
                },
                {
                    title:  '酒店名称',
                    dataIndex: 'hotelName',
                    key: 'hotelName',
                    width: 400,
                },
                {
                    title:  '规模',
                    dataIndex: 'hotelType',
                    key: 'hotelType',
                    width: 200,
                },
                {
                    title:  '评分',
                    dataIndex: 'score',
                    key: 'score',
                    width: 200,
                },
                {
                    title:  '地址',
                    dataIndex: 'address',
                    key: 'address',
                    width: 600,
                },
                {
                    title:  '最低价',
                    dataIndex: 'floorPrice',
                    key: 'floorPrice',
                    width: 200,
                },
                {
                    title:  '链接',
                    dataIndex: 'url',
                    key: 'url',
                    width: 200,
                    render: (text, record) => {
                        return (
                            <a href={text}>链接</a>
                        )
                    }
                },
                // {
                //     title:  '操作',
                //     key: 'operation',
                //     width: 200,
                //     render: (text, record) => (
                //         <p>
                //             <Button onClick={this.renderModal.bind(text, record)}>详情</Button>
                //         </p>
                //     )
                // },
            ];
            return (
                <div>
                    <p style={{textAlign: 'center', marginTop: 30}}>{'最新更新时间是：' + lastUpdateTime }</p>
                    <Table
                        columns={columns}
                        dataSource={hotelData}
                        rowkey={record => record.id}
                        pagination={false}
                        loading={loading}
                    />
                    <Pagination
                        current={this.state.pagination.current}
                        defaultPageSize={this.state.pagination.defaultPageSize}
                        total={totalCount || 100}
                        onChange={this.handlePageChange}
                        showTotal={total => `共有${total}条酒店数据`}
                    />
                </div>
            )
        }
    }

    renderCity() {
        const {cityData, selectedCity} = this.props;
        const options = [];
        if(Array.isArray(cityData)) {
            for(let i = 0; i < cityData.length; i++) {
                options.push(<Option key={cityData[i].id || i.toString(36) + i}>{cityData[i].cityName + '(' + cityData[i].pinYin + ')'}</Option>)
            }
        }

        return (
            <Select
                defaultValue={selectedCity || null}
                showSearch
                style={{ width: 200 }}
                placeholder="Select a city"
                onSelect={this.handleSelect}
                filterOption={(input, option) => {
                    return option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0}}
            >
                {options}
            </Select>
        );
    }

    render() {
       return (
        <PageContainer>
            <div>
             选择城市： {this.renderCity()}
            <Button type='primary' style={{marginLeft: 20}} onClick={this.handleSearch}>数据查询</Button>
            </div>
            {this.renderTable()}

        </PageContainer>
       )
    }
}

export default List;
