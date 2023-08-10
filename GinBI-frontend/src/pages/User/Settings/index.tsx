import { Button, Card, Form, Input, message, Radio } from 'antd';

import React, { useEffect, useState } from 'react';
import { history } from '@umijs/max';
import { getLoginUserUsingGET, updateUserUsingPOST } from '@/services/yubi/userController';

;

/**
 * 设置页面
 * @constructor
 */

const onFinishFailed = (errorInfo: any) => {
  console.log('Failed:', errorInfo);
};

const Settings: React.FC = () => {
  const [user, setUser] = useState<API.UserVO>();
  const [isSuccess, setIsSuccess] = useState(false);

  const onFinish = async (values: any) => {
    try {
      const res = await updateUserUsingPOST({
        id: user?.id,
        userName: user?.userName,
        userAvatar: user?.userAvatar,
        phoneNum: user?.phoneNum,
        email: user?.email,
        leftCount: user?.leftCount,
      });
      if (res.code === 40000) {
        message.error(res.message);
        return;
      }
      setIsSuccess(true);
      setTimeout(() => {
        history.push('/');
        window.location.reload();
      }, 1000);
    } catch (e: any) {
      message.error(e.message);
    }
    console.log('ok');
  };

  useEffect(() => {
    if (isSuccess) {
      message.success('修改成功');
      setIsSuccess(false);
    }
  }, [isSuccess]);


  const fetchData = async () => {
    try {
      const user = await getLoginUserUsingGET();
      setUser(user?.data);
    } catch (e: any) {
      //message.error();
    }
  };
  /*  console.log(user);*/


  useEffect(() => {
    fetchData();
  }, []);

  return (
    <div style={{ display: 'flex', justifyContent: 'center' }}>
      <Card bordered={true} title={'个人设置'} style={{ width: 600 }}>
        <Form
          name="basic"
          labelCol={{ span: 8 }}
          wrapperCol={{ span: 16 }}
          style={{ maxWidth: 600 }}
          initialValues={{ remember: true }}
          onFinish={onFinish}
          onFinishFailed={onFinishFailed}
          autoComplete="off"
        >
          <Form.Item
            label="账号名"
            initialValue={user?.userAccount}
            required={false}
          >
            {user?.userAccount}
          </Form.Item>

          <Form.Item label="用户名">
            <Input value={user?.userName} onChange={(e) => setUser({ ...user, userName: e.target.value })} />
          </Form.Item>

          <Form.Item label="用户头像">
            <Input value={user?.userAvatar} onChange={(e) => setUser({ ...user, userAvatar: e.target.value })} />
          </Form.Item>

          <Form.Item label="用户手机号">
            <Input value={user?.phoneNum} onChange={(e) => setUser({ ...user, phoneNum: e.target.value })} />
          </Form.Item>

          <Form.Item label="用户邮箱">
            <Input value={user?.email} onChange={(e) => setUser({ ...user, email: e.target.value })} />
          </Form.Item>


          {/*          <Form.Item label="手机号" name="phone">
            <Input />
          </Form.Item>*/}
          <Form.Item label="用户等级">
            <Input
              placeholder={user?.userRole == 'admin' ? '管理员' : '普通用户'}
              disabled={true}
              onChange={(e) => setUser({ ...user, userRole: e.target.value })}
            ></Input>
          </Form.Item>

          <Form.Item label="剩余积分" labelCol={{ span: 8 }} wrapperCol={{ span: 4 }}>
            <Input
              value={user?.leftCount}
              disabled={true}
              onChange={(e) => setUser({ ...user, userRole: e.target.value })}
            />
            
          </Form.Item>
          <Form.Item wrapperCol={{ offset: 8, span: 16 }}>
            <Button type="primary" htmlType="submit" onClick={onFinish}>
              更新
            </Button>

            <span style={{ marginRight: 200 }} />

            <Button type="primary" htmlType="submit" href="/">
              返回
            </Button>
          </Form.Item>
        </Form>
      </Card>
    </div>
  );
};
export default Settings;
