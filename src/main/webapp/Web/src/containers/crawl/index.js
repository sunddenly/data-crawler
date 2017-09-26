import React, { Component } from 'react';
import { connect } from 'dva';
import {Select, Button, message, Progress} from 'antd';
import { bindActionCreators } from 'redux';
import {PageContainer} from '../../components/sider-layout';
import model from '../../models/crawl';
import  './index.less';

const Option = Select.Option;
const actions = model.actions;

@connect(state => state.crawl, dispatch => ({
    actions: bindActionCreators(actions, dispatch)
}))
class Crawl extends Component {


    constructor(props) {
        super(props);
        this.state = {
            loading: false,
        }
    }

    componentDidMount() {
        const { fetchCity } = this.props.actions;
        fetchCity();
    }

    handleSelect = (value, option) => {
        const {handleSelect} = this.props.actions;
        handleSelect(value);
    }

    handleSearch = () => {
        if(this.props.selectedCity) {
            const {handleSearch} = this.props.actions;
            handleSearch(this.props.selectedCity);
        }
        else {
            message.error('请先选择城市名称');
        }
    }

    renderProgress() {
        const {
            progress,
            loading,
            hotelMount,
            selectedCity,
        } = this.props;
        if (loading || progress === 100) {
            const { searchProgress } = this.props.actions;
            if (progress < 100) {
                setTimeout(searchProgress, 100, selectedCity);
            }
            return (
                <div>
                     <Progress className="progress" type="circle" percent={progress || 0}/>
                     <p style={{textAlign: 'center', marginBottom: 30}}>酒店总数为{hotelMount}</p>
                </div>
            );
        } else {
            return <p className="content">请选择城市后点击抓取，查看进度</p>
        }
    }



    renderCity() {
        const {cityData, selectedCity, loading} = this.props;
        const options = [];
        for(let i = 0; i < cityData.length; i++) {
            options.push(<Option key={cityData[i].id || i.toString(36) + i}>{cityData[i].cityName + '(' + cityData[i].pinYin + ')'}</Option>)
        }
        return (
            <Select
                defaultValue={selectedCity || null}
                showSearch
                disabled={loading}
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
        const {
            loading,
        } = this.props;
        return (
        <PageContainer>
            <div>
                选择城市： {this.renderCity()}
            <Button
                type='primary'
                style={{marginLeft: 20}}
                onClick={this.handleSearch}
                loading={loading}
                >
                爬取数据
            </Button>
            </div>
            {this.renderProgress()}
        </PageContainer>
        )
    }
}

export default Crawl;
