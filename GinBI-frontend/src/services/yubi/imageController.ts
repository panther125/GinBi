// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** getAiPic GET /api/pics */
export async function getAiPicUsingGET(options?: { [key: string]: any }) {
  return request<API.BaseResponseListString_>('/api/pics', {
    method: 'GET',
    ...(options || {}),
  });
}
