import React from 'react';
import { Image } from 'antd';

const App: React.FC = () => (
  <div style={{
    float: "left"
  }}>
    <Image.PreviewGroup
      preview={{
        onChange: (current, prev) => console.log(`current index: ${current}, prev index: ${prev}`),
      }}
    >
      <Image width={200} src="https://cdn3.printidea.art/picstyle/不限定.jpg" />
      <Image width={200} src="https://cdn1.printidea.art/image/2023/8/1/01eeba3ecdfaa0a90088b1c774d87768.jpeg"/>
      <Image width={200} src="https://cdn1.printidea.art/image/2023/8/1/b927d668cae5671bc9f0f7b5ea95fb85.jpeg"/>
      <Image width={200} src="https://cdn1.printidea.art/image/2023/7/24/feb0b4d903d70aa64b3a613906a3aab4.jpg"/>
      <Image width={200} src="https://cdn1.printidea.art/image/2023/7/24/0dba41df966d5e0a8c333a20a67d58be.jpg"/>
      <Image width={200} src="https://cdn1.printidea.art/image/2023/7/24/94dd3c1b71743a52b6115214fc425d41.jpeg"/>
      <Image width={200} src="https://cdn1.printidea.art/image/2023/5/12/c43be490ed0628097ff19621ee745bc9.jpg"/>
      <Image width={200} src="https://cdn1.printidea.art/image/2023/7/24/158d13aad896e5974e0ec7135e2b97b0.jpeg"/>
      <Image width={200} src="https://cdn1.printidea.art/image/2023/7/24/93a9ebef22b776df46ed057c202f298f.jpg"/>
      <Image width={200} src="https://cdn1.printidea.art/image/2023/8/1/1fc1252c0bc9691101c94ffd4a280ea6.jpeg"/>
      <Image width={200} src="https://cdn1.printidea.art/image/2023/8/1/86ab67c0b61cae52dc2fb9e9514116e2.jpeg"/>
      <Image width={200} src="https://cdn3.printidea.art/picstyle/%E7%B2%BE%E8%87%B4%E6%91%84%E5%BD%B1.png"/>
      <Image width={200} src="https://cdn1.printidea.art/image/2023/7/24/4784d85e089a85c9df8d899f04df9076.jpg"/>
      <Image width={200} src="https://cdn1.printidea.art/image/2023/7/24/31a920ea6ca3a377b48407234e2761fb.jpg"/>
      <Image width={200} src="https://cdn1.printidea.art/picstyle/portrait.jpg"/>
      <Image width={200} src="https://cdn1.printidea.art/image/2023/7/7/a2ac160554f0af00174f424b0f51f7d1.png"/>
      <Image width={200} src="https://cdn1.printidea.art/image/2023/6/20/46b2f9659b4b79a137a09518119e5381.png"/>
      <Image width={200} src="https://cdn1.printidea.art/image/2023/5/11/005861a75284ff73ec425c462dfb980e.jpeg"/>
      <Image width={200} src="https://cdn3.printidea.art/picstyle/%E6%9A%97%E5%BD%A9.png"/>
      <Image width={200} src="https://cdn3.printidea.art/picstyle/%E9%80%9A%E7%94%A8%E6%A8%A1%E5%9E%8B.png"/>
      <Image width={200} src="https://cdn3.printidea.art/picstyle/%E7%BE%8E%E6%BC%AB.png"/>
      <Image width={200} src="https://cdn3.printidea.art/picstyle/%E5%A4%8D%E5%8F%A4.jpg"/>
      <Image width={200} src="https://cdn1.printidea.art/picstyle/intelligence.png"/>
      <Image width={200} src="https://cdn1.printidea.art/picstyle/%E8%B6%B3%E7%90%83%E5%AE%9D%E8%B4%9D.jpg"/>
      <Image width={200} src="https://cdn1.printidea.art/picstyle/%E5%BC%82%E6%AC%A1%E5%85%83%E5%A4%B4%E5%83%8F.jpg"/>
      <Image width={200} src="https://cdn3.printidea.art/picstyle/%E6%89%8B%E7%BB%98%E5%A2%9E%E5%BC%BA.jpg"/>
      <Image width={200} src="https://cdn1.printidea.art/picstyle/%E5%AE%A4%E5%86%85%E8%AE%BE%E8%AE%A1.jpg"/>
      <Image width={200} src="https://cdn1.printidea.art/image/2023/7/24/2847d06dac6ef3cc0a7fcb4b7172632f.jpg"/>
      <Image width={200} src="https://cdn1.printidea.art/image/2023/7/24/db7fd96ab5d8c65d2d8badc1af2f0888.jpeg"/>
      <Image width={200} src="https://cdn1.printidea.art/image/2023/7/24/7769b0026e11abbf97f4bc7a7d79055c.jpg"/>
      <Image width={200} src="https://cdn1.printidea.art/image/2023/7/24/9c976dc3a8a37703fae2d4555143fa03.jpg"/>
      <Image width={200} src="https://cdn1.printidea.art/image/2023/7/24/f04d2388651a06df3d829fad62df84c9.jpg"/>
      <Image width={200} src="https://cdn1.printidea.art/image/2023/7/24/ec8cb45a7090da13164e0ce71ba9e0aa.jpg"/>
      <Image width={200} src="https://cdn1.printidea.art/image/2023/7/24/959d245f70a4c7a5289454bb46339d86.jpg"/>
      <Image width={200} src="https://cdn1.printidea.art/image/2023/7/24/1d1110c60010ff4b2c156f89c50122f6.jpg"/>
      <Image width={200} src="https://cdn1.printidea.art/image/2023/7/26/0d7ec14545683e71875a51056da27e13.jpeg"/>
      <Image width={200} src="https://cdn1.printidea.art/image/2023/7/24/195226856620cf85cbb27fed9a74a15b.jpg"/>
      <Image width={200} src="https://cdn1.printidea.art/image/2023/7/25/41d73c9fa38ccc3932295d907d903c60.jpg"/>
      <Image width={200} src="https://cdn1.printidea.art/image/2023/7/24/82b40327c1fe58dcbdcd36d25ef69215.jpg"/>
      <Image width={200} src="https://images.wondershare.cn/aigc/home/2023/album-img1-2.png"/>
    </Image.PreviewGroup>
  </div>
);

export default App;