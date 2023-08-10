// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** genChat POST /api/ai/chat */
export async function genChatUsingPOST(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.genChatUsingPOSTParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseString_>('/api/ai/chat', {
    method: 'POST',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** genPurdue POST /api/ai/genPurdue */
export async function genPurdueUsingPOST(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.genPurdueUsingPOSTParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseString_>('/api/ai/genPurdue', {
    method: 'POST',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** genStory POST /api/ai/genStory */
export async function genStoryUsingPOST(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.genStoryUsingPOSTParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseStoryResponse_>('/api/ai/genStory', {
    method: 'POST',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}
