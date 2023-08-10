import { CHART_TYPE } from '@/constants/chart/chartType';
import {
listMyChartVOByPageUsingPOST,
reloadChartByAiUsingGET
} from '@/services/yubi/chartController';
import ProList from '@ant-design/pro-list';
import { ProListMetas } from '@ant-design/pro-list/lib';
import { ActionType } from '@ant-design/pro-table';
import { useInterval } from 'ahooks';
import {
Button,
Collapse,
message,
Modal,
Result,
Tag,
} from 'antd';
import ReactECharts from 'echarts-for-react';
import React,{ useEffect,useRef,useState } from 'react';

const TestMyChartPage: React.FC = () => {
  //默认搜索参数
  const initSearchParams = {
    name: '',
    chartType: '',
    current: 1,
    pageSize: 6,
    sortField: 'createTime',
    sortOrder: 'desc',
  };

  const [searchParams, setSearchParams] = useState<API.ChartQueryRequest>({ ...initSearchParams });
  const [chartList, setChartList] = useState<API.Chart[]>();
  const [total, setTotal] = useState<number>(0);
  const [loading, setLoading] = useState<boolean>(false);
  const [chartId, setChartId] = useState<number>(0);
  const actionRef = useRef<ActionType>();

  /**
   * 加载数据
   */
  const loadData = async () => {
    setLoading(true);
    try {
      const res = await listMyChartVOByPageUsingPOST(searchParams);
      const data = res?.data;
      if (res.code === 0 && data) {
        // message.success('加载成功');
        setChartList(data?.records ?? []);
        setTotal(data.total ?? 0);
      } else {
        message.error('获取图表列表失败');
      }
    } catch (e: any) {
      message.error('获取图表列表失败', e.message);
    }
    setLoading(false);
  };

  /**
   * 手动重新生成图表
   * @param params
   */
  const reloadChart = async (params: number) => {
    if (params === undefined) {
      message.error('id为空');
      return;
    }
    const data = {
      chartId: params,
    };
    try {
      const res = await reloadChartByAiUsingGET(data);
      if (res.code === 0 && data) {
        message.success('请求成功，图表生成中请稍后');
      } else {
        message.error('重新生成图表失败');
      }
    } catch (e: any) {
      message.error('重新生成图表失败', e.message);
    }
  };

  useEffect(() => {
    loadData();
  }, [searchParams]);

  //每30s加载一次数据
  useInterval(() => {
    loadData();
  }, 30000);

  /**
   * 处理图表类型
   */
  const handleType = () => {
    return CHART_TYPE.reduce((obj: any, item) => {
      obj[item.value] = item.label;
      return obj;
    }, {});
  };

  /**
   * 列表元素
   */
  const metas: ProListMetas<API.Chart> = {
    id: {
      title: 'id',
      dataIndex: 'id',
      valueType: 'index',
      search: false,
    },
    title: {
      title: '图表名称',
      dataIndex: 'name',
    },
    subTitle: {
      title: '图表类型',
      dataIndex: 'chartType',
      valueEnum: handleType(),
      render: (_, item) => {
        return <Tag color="#5BD8A6">{'图表类型：' + item.chartType}</Tag>;
      },
    },
    content: {
      search: false,
      render: (_, item) => {
        return (
          <div style={{ flex: 1 }}>
            <div>
              <p>{item.goal}</p>
            </div>
            {item.execMessage === "等待中" && (
              <div>
                <Result status="warning" title="等待处理" subTitle={item.execMessage} />
              </div>
            )}
            {item.execMessage === "生成中" && (
              <div>
                <Result status="info" title="图表生成中" subTitle={item.execMessage} />
              </div>
            )}
            {item.execMessage === "失败" && (
              <div>
                <Result
                  status="error"
                  title="图表生失败"
                  subTitle={item.execMessage}
                  extra={[
                    <Button
                      key="tryAgain"
                      type="primary"
                      danger
                      onClick={() => {
                        reloadChart(item.id as number);
                      }}
                    >
                      请重试
                    </Button>,
                  ]}
                />
              </div>
            )}
            {item.execMessage === "成功" && (
              <div style={{ width: '100%' }}>
                <ReactECharts option={JSON.parse(item.genChart ?? '{}')} />
              </div>
            )}
            <div>
              <Collapse
                bordered={false}
                items={[
                  {
                    key: item.id,
                    label: 'Ai分析结论',
                    children: <p>{item.genResult}</p>,
                  },
                ]}
              />
            </div>
          </div>
        );
      },
    }
  };

  return (
    <div className="my-chart-page">
      <ProList<API.Chart>
        grid={{ gutter: 16, column: 2 }}
        ghost={true}
        loading={loading}
        actionRef={actionRef}
        search={{
          labelWidth: 'auto',
          defaultCollapsed: false,
        }}
        request={async (params) => {
          setSearchParams({
            ...searchParams,
            name: params.name,
            chartType: params.chartType,
          });
        }}
        rowKey="id"
        pagination={{
          onChange: (page, pageSize) => {
            // console.log(page, pageSize);
            setSearchParams({
              ...searchParams,
              name: '',
              chartType: '',
              current: page,
              pageSize,
            });
          },
          current: searchParams?.current,
          pageSize: searchParams?.pageSize,
          total: total,
        }}
        dataSource={chartList}
        metas={metas}
      />
    </div>
  );
};
export default TestMyChartPage;
