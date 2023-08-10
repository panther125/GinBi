import { IMAGES } from '@/constants';
import { PageContainer } from '@ant-design/pro-components';
import { useModel } from '@umijs/max';
import { Card, Carousel, Divider, Image,theme } from 'antd';
import React, { useEffect, useState } from 'react';

/**
 * 每个单独的卡片，为了复用样式抽成了组件
 * @param param0
 * @returns
 */
const InfoCard: React.FC<{
  title: string;
  index: number;
  desc: string;
  href: string;
}> = ({ title, href, index, desc }) => {
  const { useToken } = theme;
  const { token } = useToken();

  return (
    <div
      style={{
        backgroundColor: token.colorBgContainer,
        boxShadow: token.boxShadow,
        borderRadius: '8px',
        fontSize: '14px',
        color: token.colorTextSecondary,
        lineHeight: '22px',
        padding: '16px 19px',
        minWidth: '220px',
        flex: 1,
      }}
    >
      <div
        style={{
          display: 'flex',
          gap: '4px',
          alignItems: 'center',
        }}
      >
        <div
          style={{
            width: 48,
            height: 48,
            lineHeight: '22px',
            backgroundSize: '100%',
            textAlign: 'center',
            padding: '8px 16px 16px 12px',
            color: '#FFF',
            fontWeight: 'bold',
            backgroundImage:
              'url(\'https://gw.alipayobjects.com/zos/bmw-prod/daaf8d50-8e6d-4251-905d-676a24ddfa12.svg\')',
          }}
        >
          {index}
        </div>
        <div
          style={{
            fontSize: '16px',
            color: token.colorText,
            paddingBottom: 8,
          }}
        >
          {title}
        </div>
      </div>
      <div
        style={{
          fontSize: '14px',
          color: token.colorTextSecondary,
          textAlign: 'justify',
          lineHeight: '22px',
          marginBottom: 8,
        }}
      >
        {desc}
      </div>
      <a href={href} target="_blank" rel="noreferrer">
        了解更多 {'>'}
      </a>
    </div>
  );
};

const Welcome: React.FC = () => {
  const { token } = theme.useToken();
  // 加载图片
  const [loadedImages, setLoadedImages] = useState([]);
    useEffect(() => {
      Promise.all(IMAGES)
        // @ts-ignore
        .then((images) => setLoadedImages(images))
        .catch((error) => console.error(error));
    }, []);
  const { initialState } = useModel('@@initialState');
  return (
    <PageContainer>
      <Card
        style={{
          borderRadius: 8,
        }}
        bodyStyle={{
          backgroundImage:
            initialState?.settings?.navTheme === 'realDark'
              ? 'background-image: linear-gradient(75deg, #1A1B1F 0%, #191C1F 100%)'
              : 'background-image: linear-gradient(75deg, #FBFDFF 0%, #F5F7FF 100%)',
        }}
      >
        <div
          style={{
            backgroundPosition: '100% -30%',
            backgroundRepeat: 'no-repeat',
            backgroundSize: '274px auto',
            backgroundImage:
              'url(\'https://gw.alipayobjects.com/mdn/rms_a9745b/afts/img/A*BuFmQqsB2iAAAAAAAAAAAAAAARQnAQ\')',
          }}
        >
          <div
            style={{
              fontSize: '20px',
              color: token.colorTextHeading,
            }}
          >
            欢迎使用 琴酒 BI
          </div>
          <p
            style={{
              fontSize: '14px',
              color: token.colorTextSecondary,
              lineHeight: '22px',
              marginTop: 16,
              marginBottom: 32,
              width: '65%',
            }}
          >
          </p>
          <div
            style={{
              display: 'flex',
              flexWrap: 'wrap',
              gap: 16,
            }}
          >
            <InfoCard
              index={3}
              href="https://github.com/panther125"
              title="项目介绍"
              desc="这是一个创新的数据分析工具，与传统的BI完全不同。我们的项目利用AI技术，以完全新颖的方式帮助用户进行数据分析，节约人力成本并提高效率。分别采用了线程池和消息队列来提升用户的体验和加强信息的可靠性,后端自定义 Prompt 预设模板并封装用户输入的数据和分析诉求，通过对接 AIGC 接口生成可视化图表由于 AIGC 的输入 Token 限制，使用 Easy Excel 解析用户上传的 XLSX 表格数据文件并压缩为 CSV，实测提高了 20% 的单次输入数据量、并节约了成本。"
            />
            <InfoCard
              index={1}
              title="什么是BI?"
              href={''}
              desc="BI是商业智能（Business Intelligence）的缩写。它是一种通过收集、分析和呈现数据来帮助企业做出更好决策的技术和过程。BI通常涉及使用各种数据仓库、数据挖掘和数据分析工具，以提取有关企业绩效、市场趋势、客户行为等方面的信息。这些数据可以被转化为可视化图表、报表和仪表盘，可协助企业管理层了解他们所运营的公司的情况，并根据这些信息制定更明智的商业决策。"
            />
            <InfoCard
              index={2}
              title="BI 平台的优点"
              href="add_chart"
              desc="自动化数据分析：项目采用AI技术，能够自动化地从原始数据中生成符合要求的图表和分析结论。用户只需要输入分析目标和原始数据，无需具备专业的数据分析知识，即可完成数据分析的过程。可视化结果：通过使用AI接口生成分析结果，该项目能够实现将数据以可视化的方式展示出来，使用户更容易理解和解读数据，从而做出更明智的决策。"
            />
          </div>
        </div>
      </Card>
      <Divider style={{ color: 'blue' }}>AIGC猫咪</Divider>
        <Carousel autoplay autoplaySpeed={10000}>
            {loadedImages.map((image, index) => (
              <div key={index}>
                <Image src={image.default} />
              </div>
            ))}
          </Carousel>
    </PageContainer>
  );
};

export default Welcome;
