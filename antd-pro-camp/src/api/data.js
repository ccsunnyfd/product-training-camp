import { axios } from '@/utils/request'

const api = {
  uploadImg: '/uploadStream',
  removeObject: '/removeObject'
}

const s3Url = 'https://zos.alipayobjects.com/'

export { api, s3Url }

export function uploadImg (formData) {
  return axios.request({
    url: api.uploadImg,
    data: formData,
    method: 'post'
  })
}

export function removeObject (objectName) {
  return axios.request({
    url: api.removeObject,
    method: 'post',
    params: {
      object: objectName
    }
  })
}

export function getUserList (parameter) {
  return axios({
    url: api.user,
    method: 'get',
    params: parameter
  })
}

export const getTableData = () => {
  return axios.request({
    url: 'get_table_data',
    method: 'get'
  })
}

export const getDragList = () => {
  return axios.request({
    url: 'get_drag_list',
    method: 'get'
  })
}

export const errorReq = () => {
  return axios.request({
    url: 'error_url',
    method: 'post'
  })
}

export const saveErrorLogger = info => {
  return axios.request({
    url: 'save_error_logger',
    data: info,
    method: 'post'
  })
}

export const submitBasicInfo = basicInfo => {
  return axios.request({
    url: 'product/add',
    method: 'post',
    data: basicInfo
  })
}

export const submitExample = example => {
  return axios.request({
    url: 'example/add',
    method: 'post',
    data: example
  })
}

export const getOrgData = () => {
  return axios.request({
    url: 'get_org_data',
    method: 'get'
  })
}

export const getTreeSelectData = () => {
  return axios.request({
    url: 'get_tree_select_data',
    method: 'get'
  })
}
