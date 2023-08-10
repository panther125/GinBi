export default [
  { path: '/user', layout: false, routes: [
    { path: '/user/login', component: './User/Login' },
    { path: '/user/register', component: './User/Register' },
    { path: '/user/settings', component: './User/Settings' },
  ] },
  { path: '/', redirect: '/welcome' },
  { path: '/welcome', name: '首页', icon: 'homeOutlined', component: './Welcome' },
  { path: '/add_chart', name: '智能分析', icon: 'barChart', component: './AddChart' },
  { path: '/add_chart_async', name: '智能分析（异步）', icon: 'barChart', component: './AddChartAsync' },
  { path: '/add_chart_MQ', name: '智能分析（MQ）', icon: 'barChart', component: './AddChartMQ' },
  { path: '/my_chart', name: '我的图表', icon: 'pieChart', component: './MyChart' },
  { path: '/story',name: 'Ai聊天',icon: 'AliwangwangOutlined', component: './story' },
  { path: '/images',name: 'Ai绘画',icon: 'InstagramOutlined', component: './images' },
  {
    path: '/admin',
    icon: 'crown',
    access: 'canAdmin',
    routes: [
      { path: '/admin', name: '管理页面', redirect: '/admin/sub-page' },
      { path: '/admin/sub-page', name: '管理页面2', component: './Admin' },
    ],
  },
  { path: '/', redirect: '/welcome' },
  { path: '*', layout: false, component: './404' },
];
